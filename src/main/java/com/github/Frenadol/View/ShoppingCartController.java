package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClientDAO;
import com.github.Frenadol.Dao.Clientes_PrendasDAO;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Utils.SessionManager;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ShoppingCartController {

    @FXML
    private TableView<Client_Clothes> shoppingCartTableView;
    private ClientDAO clientDAO = new ClientDAO();

    @FXML
    private TableColumn<Client_Clothes, javafx.scene.image.Image> imageColumn;

    @FXML
    private TableColumn<Client_Clothes, String> nameColumn;

    @FXML
    private TableColumn<Client_Clothes, Double> priceColumn;

    @FXML
    private TableColumn<Client_Clothes, Integer> quantityColumn;

    @FXML
    private Label totalPriceLabel;

    @FXML
    private Label walletBalanceLabel;

    private ObservableList<Client_Clothes> shoppingCart;

    private Clientes_PrendasDAO clientesPrendasDAO = new Clientes_PrendasDAO();

    /**
     * Initializes the controller by reloading the cart and updating the wallet balance.
     */
    @FXML
    public void initialize() {
        reloadCart();
        updateWalletBalance();
    }

    /**
     * Loads the shopping cart for the current client and updates the total price.
     */
    private void loadShoppingCart() {
        int clientId = SessionManager.getInstance().getCurrentClient().getId_user();
        List<Client_Clothes> shoppingCartList = clientesPrendasDAO.viewShoppingCart(clientId);
        shoppingCart = FXCollections.observableArrayList(shoppingCartList);
        setShoppingCart(shoppingCart);
        updateTotalPrice();
    }

    /**
     * Sets the shopping cart items in the table view.
     *
     * @param shoppingCart The list of items in the shopping cart.
     */
    public void setShoppingCart(ObservableList<Client_Clothes> shoppingCart) {
        this.shoppingCart = shoppingCart;

        nameColumn.setCellValueFactory(clothes ->
                new SimpleObjectProperty<>(clothes.getValue().getClothes().getName_clothes())
        );

        priceColumn.setCellValueFactory(clothes ->
                new SimpleObjectProperty<>(clothes.getValue().getClothes().getPrice_clothes())
        );

        imageColumn.setCellValueFactory(clothes -> {
            byte[] visualData = clothes.getValue().getClothes().getClothes_Visual();
            if (visualData != null) {
                InputStream is = new ByteArrayInputStream(visualData);
                javafx.scene.image.Image image = new javafx.scene.image.Image(is);
                return new SimpleObjectProperty<>(image);
            }
            return null;
        });

        imageColumn.setCellFactory(column -> new TableCell<Client_Clothes, javafx.scene.image.Image>() {
            @Override
            protected void updateItem(javafx.scene.image.Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    setGraphic(imageView);
                }
            }
        });

        quantityColumn.setCellValueFactory(clothes ->
                new SimpleObjectProperty<>(clothes.getValue().getCantidad())
        );

        shoppingCartTableView.setItems(shoppingCart);
    }

    /**
     * Updates the total price label based on the items in the shopping cart.
     */
    private void updateTotalPrice() {
        double totalPrice = shoppingCart.stream()
                .mapToDouble(item -> item.getClothes().getPrice_clothes() * item.getCantidad())
                .sum();
        totalPriceLabel.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    /**
     * Reloads the shopping cart by loading the items again.
     */
    @FXML
    public void reloadCart() {
        loadShoppingCart();
    }

    /**
     * Handles the action to finish the order. Validates the total price and removes items from the cart.
     */
    @FXML
    public void handleFinishOrder() {
        if (shoppingCart.isEmpty()) {
            showAlert("Error", "No hay productos en el carrito.", Alert.AlertType.ERROR);
            return;
        }

        String totalPriceText = totalPriceLabel.getText().replace("Total: $", "").replace(",", ".");
        double totalPrice = Double.parseDouble(totalPriceText);

        if (totalPrice != 0) {
            showAlert("Error", "No puedes terminar el pedido hasta que el total sea $0.00.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate purchaseDate = LocalDate.now();
        LocalDate deliveryDate = purchaseDate.plusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar Pedido");
        confirmationAlert.setHeaderText("¿Estás seguro de terminar el pedido?");
        confirmationAlert.setContentText("Total: $" + String.format("%.2f", totalPrice) + "\nFecha de entrega: " + deliveryDate.format(formatter));
        Optional<ButtonType> response = confirmationAlert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.OK) {
            // Remove items from the shopping cart in the database
            int clientId = SessionManager.getInstance().getCurrentClient().getId_user();
            for (Client_Clothes item : shoppingCart) {
                clientesPrendasDAO.removeClothesFromCart(clientId, item.getClothes().getId_clothes());
            }

            // Clear the shopping cart and reload it
            shoppingCart.clear();
            reloadCart();

            // Reset the hasPaid status
            SessionManager.getInstance().resetHasPaid();

            showAlert("Éxito", "Pedido terminado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Displays an alert with the given title, message, and alert type.
     *
     * @param title     The title of the alert.
     * @param message   The message to display in the alert.
     * @param alertType The type of the alert.
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the action to go back to the client menu.
     */
    @FXML
    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) shoppingCartTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            reloadCart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the payment of the order. Validates the total price, checks the client's wallet balance,
     * updates the balance, sets the total price to 0, and saves the order details to a PDF file.
     */
    @FXML
    public void handlePayOrder() {
        if (SessionManager.getInstance().hasPaid()) {
            showAlert("Error", "Ya has pagado este pedido.", Alert.AlertType.ERROR);
            return;
        }

        String totalPriceText = totalPriceLabel.getText().replace("Total: $", "").replace(",", ".");
        double totalPrice = Double.parseDouble(totalPriceText);

        if (totalPrice == 0) {
            showAlert("Error", "No hay productos en el carrito.", Alert.AlertType.ERROR);
            return;
        }

        int clientId = SessionManager.getInstance().getCurrentClient().getId_user();
        double clientBalance = SessionManager.getInstance().getCurrentClient().getWallet();

        if (clientBalance < totalPrice) {
            showAlert("Error", "Saldo insuficiente para completar el pedido.", Alert.AlertType.ERROR);
            return;
        }

        SessionManager.getInstance().getCurrentClient().setWallet(clientBalance - totalPrice);
        clientDAO.updateWallet(clientId, SessionManager.getInstance().getCurrentClient().getWallet());
        updateWalletBalance();

        // Set total price to 0 after successful payment
        totalPriceLabel.setText("Total: $0.00");

        // Mark the order as paid
        SessionManager.getInstance().setHasPaid(true);

        // Save order details to a PDF file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Pedido");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PdfWriter writer = new PdfWriter(file)) {
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph("Total: $" + String.format("%.2f", totalPrice)));
                LocalDate deliveryDate = LocalDate.now().plusDays(2);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                document.add(new Paragraph("Fecha de entrega: " + deliveryDate.format(formatter)));
                document.add(new Paragraph("\nDetalles del pedido:\n"));

                Table table = new Table(4);
                table.addCell("Prenda");
                table.addCell("Cantidad");
                table.addCell("Precio");
                table.addCell("Imagen");

                for (Client_Clothes item : shoppingCart) {
                    table.addCell(item.getClothes().getName_clothes());
                    table.addCell(String.valueOf(item.getCantidad()));
                    table.addCell(String.format("%.2f", item.getClothes().getPrice_clothes()));

                    byte[] visualData = item.getClothes().getClothes_Visual();
                    if (visualData != null) {
                        ImageData imageData = ImageDataFactory.create(visualData);
                        Image image = new Image(imageData);
                        image.setWidth(100);
                        image.setHeight(100);
                        table.addCell(image);
                    } else {
                        table.addCell("No Image");
                    }
                }

                document.add(table);
                document.close();

                showAlert("Éxito", "Pedido pagado y guardado correctamente.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Error", "Error al guardar el pedido.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Éxito", "Pedido pagado correctamente.", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Updates the wallet balance label with the current client's wallet balance.
     */
    private void updateWalletBalance() {
        double clientBalance = SessionManager.getInstance().getCurrentClient().getWallet();
        walletBalanceLabel.setText("Wallet Balance: $" + String.format("%.2f", clientBalance));
    }
}