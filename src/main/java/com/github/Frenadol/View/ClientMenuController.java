package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ClientMenuController {
    @FXML
    private GridPane ClothesPane;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TableColumn<Clothes, Float> precioColumn;
    private ObservableList<ClothesDAO.ClothesLazyAll> clothesList;
    private ObservableList<Clothes> shoppingCart;
    private ClothesDAO.ClothesLazyAll selectedClothes;

    @FXML
    private Button addToCartButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView imageView;
    @FXML
    private Button cleanButton;
    @FXML
    private Button searchButton;
    @FXML
    private Label walletBalanceLabel;
    @FXML
    private Button addMoneyButton;
    @FXML
    private Button logoutButton;

    private String savedCardNumber = "";
    private String savedCardExpiry = "";
    private String savedCardCVV = "";

    private ClientDAO clientDAO;

    /** Initializes the controller. */
    @FXML
    public void initialize() {
        clientDAO = new ClientDAO();
        clothesList = FXCollections.observableArrayList();
        shoppingCart = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();

        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);

        // Populate categoryComboBox with categories
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Deportiva", "Formal", "Informal", "Casual", "Exterior", "Interior"
        ));
        updateWalletBalance();
    }

    /** Searches clothes by selected category. */
    @FXML
    private void searchByCategory() {
        String selectedCategory = categoryComboBox.getValue();
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("Error", "Por favor, selecciona una categoría.", Alert.AlertType.ERROR);
            return;
        }
        ClothesDAO clothesDAO = new ClothesDAO();
        List<Clothes> searchedResults = clothesDAO.findClothesByCategory(selectedCategory);
        if (searchedResults.isEmpty()) {
            showAlert("Error", "No se encontraron resultados", Alert.AlertType.ERROR);
            return;
        }
        mostrarProductos(searchedResults);
    }

    /** Updates the wallet balance label with the current client's balance. */
    public void updateWalletBalance() {
        double balance = SessionManager.getInstance().getCurrentClient().getWallet();
        walletBalanceLabel.setText("Saldo: $" + String.format("%.2f", balance));
    }

    /** Shows a dialog to add money to the wallet. */
    @FXML
    private void showAddMoneyDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Añadir dinero");
        dialog.setHeaderText("Añadir dinero a la cartera");

        // Create the custom dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField amountField = new TextField();
        amountField.setPromptText("Cantidad");

        TextField cardNumberField = new TextField(savedCardNumber);
        cardNumberField.setPromptText("Número de tarjeta");
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardNumberField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 12) {
                cardNumberField.setText(oldValue);
            }
        });

        DatePicker cardExpiryPicker = new DatePicker();
        cardExpiryPicker.setPromptText("Fecha de expiración");
        cardExpiryPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        TextField cardCVVField = new TextField(savedCardCVV);
        cardCVVField.setPromptText("CVV");
        cardCVVField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                cardCVVField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (newValue.length() > 3) {
                cardCVVField.setText(oldValue);
            }
        });

        grid.add(new Label("Cantidad:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Número de tarjeta:"), 0, 1);
        grid.add(cardNumberField, 1, 1);
        grid.add(new Label("Fecha de expiración:"), 0, 2);
        grid.add(cardExpiryPicker, 1, 2);
        grid.add(new Label("CVV:"), 0, 3);
        grid.add(cardCVVField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String cardNumber = cardNumberField.getText();
                String cardCVV = cardCVVField.getText();
                LocalDate cardExpiry = cardExpiryPicker.getValue();

                if (amount > 0 && cardNumber.matches("\\d{12}") && cardCVV.matches("\\d{3}") && cardExpiry != null) {
                    savedCardNumber = cardNumber;
                    savedCardExpiry = cardExpiry.toString();
                    savedCardCVV = cardCVV;

                    SessionManager.getInstance().getCurrentClient().addMoneyToWallet(amount);
                    clientDAO.updateWallet(SessionManager.getInstance().getCurrentClient().getId_user(), SessionManager.getInstance().getCurrentClient().getWallet());
                    updateWalletBalance();
                    showAlert("Éxito", "Dinero añadido correctamente", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Por favor, rellena todos los campos correctamente.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Cantidad inválida", Alert.AlertType.ERROR);
            }
        }
    }

    /** Shows an alert dialog with the given title, message, and alert type. */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Clears the search text field and reloads all clothes. */
    @FXML
    private void cleanSearch() {
        searchTextField.clear();
        clothesList = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();
        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);
    }

    /** Searches clothes by name based on the search text field input. */
    @FXML
    private void searchClothes() {
        String searchQuery = searchTextField.getText();
        if (searchQuery.isEmpty()) {
            showAlert("Error", "Por favor, ingresa un texto de búsqueda.", Alert.AlertType.ERROR);
            return;
        }
        ClothesDAO clothesDAO = new ClothesDAO();
        List<Clothes> searchedResults = clothesDAO.findClothesByName(searchQuery);
        if (searchedResults.isEmpty()) {
            showAlert("Error", "No se encontraron resultados", Alert.AlertType.ERROR);
            return;
        }
        mostrarProductos(searchedResults);
    }

    /** Displays the list of clothes in the grid pane. */
    public void mostrarProductos(List<? extends Clothes> clothesList) {
        ClothesPane.getChildren().clear();
        final int MAX_COLUMNS = 4;
        int columns = 0;
        int rows = 0;

        for (Clothes clothes : clothesList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Thumb.fxml"));
                VBox box = loader.load();
                ThumbController thumbController = loader.getController();
                thumbController.setGarment(clothes);

                box.setOnMouseClicked(event -> {
                    if (clothes instanceof ClothesDAO.ClothesLazyAll) {
                        selectedClothes = (ClothesDAO.ClothesLazyAll) clothes;
                        showProductDetails(selectedClothes);
                    } else {
                        showProductDetails(clothes);
                    }
                });

                ClothesPane.add(box, columns, rows);
                columns++;

                if (columns == MAX_COLUMNS) {
                    columns = 0;
                    rows++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Shows the details of the selected clothes item. */
    @FXML
    private void showProductDetails(Clothes clothes) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsProducts.fxml"));
            AnchorPane productDetailPane = loader.load();

            DetailsProductsController detailsProductsController = loader.getController();
            detailsProductsController.setProductDetails(clothes);

            Stage detailStage = new Stage();
            Scene scene = new Scene(productDetailPane);
            detailStage.setScene(scene);
            detailStage.setTitle("Detalle del Producto");
            detailStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Opens the shopping cart view. */
    @FXML
    private void openShoppingCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            Stage stage = (Stage) ClothesPane.getScene().getWindow();

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            ShoppingCartController shoppingCartController = loader.getController();

            List<Client_Clothes> detalles = SessionManager.getInstance().getDetails();
            ObservableList<Client_Clothes> details = FXCollections.observableArrayList(detalles);

            if (details != null && !details.isEmpty()) {
                shoppingCartController.setShoppingCart(details);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Shows the details of the selected clothes item (lazy loading). */
    @FXML
    private void showProductDetails(ClothesDAO.ClothesLazyAll clothesLazy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsProducts.fxml"));
            AnchorPane productDetailPane = loader.load();

            DetailsProductsController detailsProductsController = loader.getController();
            ClothesDAO clothesDAO = new ClothesDAO();
            Clothes clothes = clothesDAO.findClothesById(clothesLazy.getId_clothes());
            detailsProductsController.setProductDetails(clothes);

            Stage detailStage = new Stage();
            Scene scene = new Scene(productDetailPane);
            detailStage.setScene(scene);
            detailStage.setTitle("Detalle del Producto");
            detailStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Cierre de Sesión");
        alert.setHeaderText("Estás a punto de cerrar sesión");
        alert.setContentText("¿Estás seguro de que deseas salir de la aplicación?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SessionManager.getInstance().clearSession();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.close();
        }
    }
}