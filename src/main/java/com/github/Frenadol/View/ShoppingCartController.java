package com.github.Frenadol.View;

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

    private ObservableList<Client_Clothes> shoppingCart;

    private Clientes_PrendasDAO clientesPrendasDAO = new Clientes_PrendasDAO();

    @FXML
    public void initialize() {
        reloadCart();
    }

    private void loadShoppingCart() {
        int clientId = SessionManager.getInstance().getCurrentClient().getId_user();
        List<Client_Clothes> shoppingCartList = clientesPrendasDAO.viewShoppingCart(clientId);
        shoppingCart = FXCollections.observableArrayList(shoppingCartList);
        setShoppingCart(shoppingCart);
        updateTotalPrice();
    }

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

    private void updateTotalPrice() {
        double totalPrice = shoppingCart.stream()
                .mapToDouble(item -> item.getClothes().getPrice_clothes() * item.getCantidad())
                .sum();
        totalPriceLabel.setText("Total: $" + String.format("%.2f", totalPrice));
    }

    @FXML
    public void reloadCart() {
        loadShoppingCart();
    }

    @FXML
    public void handleFinishOrder() {
        // Obtener el precio total del label y reemplazar la coma por un punto
        String totalPriceText = totalPriceLabel.getText().replace("Total: $", "").replace(",", ".");
        double totalPrice = Double.parseDouble(totalPriceText);

        int clientId = SessionManager.getInstance().getCurrentClient().getId_user();
        double clientBalance = SessionManager.getInstance().getCurrentClient().getWallet();

        if (clientBalance < totalPrice) {
            showAlert("Error", "Saldo insuficiente para completar el pedido.", Alert.AlertType.ERROR);
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
            // Actualizar el saldo del cliente
            SessionManager.getInstance().getCurrentClient().setWallet(clientBalance - totalPrice);

            // Guardar los detalles del pedido en un archivo PDF
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Pedido");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (PdfWriter writer = new PdfWriter(file)) {
                    PdfDocument pdfDoc = new PdfDocument(writer);
                    Document document = new Document(pdfDoc);

                    document.add(new Paragraph("Total: $" + String.format("%.2f", totalPrice)));
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

                    showAlert("Éxito", "Pedido guardado correctamente.", Alert.AlertType.INFORMATION);
                } catch (IOException e) {
                    showAlert("Error", "Error al guardar el pedido.", Alert.AlertType.ERROR);
                }
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
    @FXML
    public void handleOrderReceived() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar Recepción");
        confirmationAlert.setHeaderText("¿Has recibido el pedido?");
        confirmationAlert.setContentText("Si confirmas, los datos desaparecerán del carrito.");

        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            int clientId = SessionManager.getInstance().getCurrentClient().getId_user();
            for (Client_Clothes item : shoppingCart) {
                clientesPrendasDAO.removeClothesFromCart(clientId, item.getClothes().getId_clothes());
            }
            // Clear the shopping cart
            shoppingCart.clear();
            shoppingCartTableView.setItems(FXCollections.observableArrayList());
            totalPriceLabel.setText("Total: $0.00");

            showAlert("Éxito", "Pedido recibido y carrito vaciado.", Alert.AlertType.INFORMATION);
        }
    }
}