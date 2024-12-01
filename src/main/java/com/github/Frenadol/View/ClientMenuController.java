package com.github.Frenadol.View;

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
import java.util.List;
import java.util.Optional;

public class ClientMenuController {
    @FXML
    private GridPane ClothesPane;
    @FXML
    private Spinner<Integer> cantidadSpinner;
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

    private String savedCardNumber = "";
    private String savedCardExpiry = "";
    private String savedCardCVV = "";

    private ClientDAO clientDAO;

    @FXML
    public void initialize() {
        clientDAO = new ClientDAO();
        clothesList = FXCollections.observableArrayList();
        shoppingCart = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();

        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);

        updateWalletBalance();
    }

    public  void updateWalletBalance() {
        double balance = SessionManager.getInstance().getCurrentClient().getWallet();
        walletBalanceLabel.setText("Saldo: $" + String.format("%.2f", balance));
    }

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

        TextField cardExpiryField = new TextField(savedCardExpiry);
        cardExpiryField.setPromptText("Fecha de expiración (MM/AA)");

        TextField cardCVVField = new TextField(savedCardCVV);
        cardCVVField.setPromptText("CVV");

        grid.add(new Label("Cantidad:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Número de tarjeta:"), 0, 1);
        grid.add(cardNumberField, 1, 1);
        grid.add(new Label("Fecha de expiración:"), 0, 2);
        grid.add(cardExpiryField, 1, 2);
        grid.add(new Label("CVV:"), 0, 3);
        grid.add(cardCVVField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > 0) {
                    savedCardNumber = cardNumberField.getText();
                    savedCardExpiry = cardExpiryField.getText();
                    savedCardCVV = cardCVVField.getText();

                    SessionManager.getInstance().getCurrentClient().addMoneyToWallet(amount);
                    clientDAO.updateWallet(SessionManager.getInstance().getCurrentClient().getId_user(), SessionManager.getInstance().getCurrentClient().getWallet());
                    updateWalletBalance();
                    showAlert("Éxito", "Dinero añadido correctamente", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "La cantidad debe ser mayor a 0", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Cantidad inválida", Alert.AlertType.ERROR);
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void cleanSearch() {
        searchTextField.clear();
        clothesList = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();
        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);
    }

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

    public void mostrarProductos(List<? extends Clothes> clothesList) {
        ClothesPane.getChildren().clear();
        final int MAX_COLUMNS = 3;
        int columns = 0;
        int rows = 0;

        for (Clothes clothes : clothesList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Thumb.fxml"));
                VBox box = loader.load();
                ThumbController thumbController = loader.getController();
                thumbController.setGarment(clothes);

                box.setOnMouseClicked(event -> {
                    selectedClothes = (ClothesDAO.ClothesLazyAll) clothes;
                    showProductDetails((ClothesDAO.ClothesLazyAll) clothes);
                });

                ClothesPane.add(box, columns++, rows);

                if (columns == MAX_COLUMNS) {
                    columns = 0;
                    rows++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
}