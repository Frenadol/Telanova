package com.github.Frenadol.View;

import com.github.Frenadol.Dao.Clientes_PrendasDAO;
import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Optional;

public class ThumbController {

    @FXML
    private Label itemName;

    @FXML
    private ImageView productImageView;
    @FXML
    private Label productPriceLabel;

    private Clothes clothesItem;

    /**
     * Sets the garment details in the view.
     * @param clothes The clothes item to display.
     */
    public void setGarment(Clothes clothes) {
        this.clothesItem = clothes;
        itemName.setText(clothes.getName_clothes());
        productPriceLabel.setText(String.format("$%.2f", clothes.getPrice_clothes()));
        if (clothes.getClothes_Visual() != null && clothes.getClothes_Visual().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(clothes.getClothes_Visual());
            Image image = new Image(bis);
            productImageView.setImage(image);
        }
    }

    /**
     * Handles the addition of a garment to the shopping cart.
     * @param event The mouse event triggered by clicking the add to cart button.
     */
    @FXML
    private void handleAddToCart(MouseEvent event) {
        if (clothesItem == null) {
            showAlert("Error", "Por favor, selecciona una prenda.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Seleccionar cantidad");
        dialog.setHeaderText("Selecciona la cantidad de " + clothesItem.getName_clothes());
        dialog.setContentText("Cantidad:");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) {
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(result.get());
            if (cantidad <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Por favor, ingresa una cantidad válida.");
            return;
        }

        ClothesDAO clothesDAO = new ClothesDAO();
        int availableQuantity = clothesDAO.getAvailableQuantity(clothesItem.getId_clothes());
        if (cantidad > availableQuantity) {
            showAlert("Error", "No puede añadir más cantidad de esta prenda porque no hay más en stock");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar adición al carrito");
        confirmationAlert.setHeaderText("¿Estás seguro de añadir al carrito?");
        confirmationAlert.setContentText("¿Deseas añadir " + cantidad + " de " + clothesItem.getName_clothes() + " al carrito?");

        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            SessionManager.getInstance().addToCart(clothesItem, cantidad);
            System.out.println("Prenda añadida al carrito: " + clothesItem.getName_clothes());

            Client_Clothes clientClothes = new Client_Clothes();
            clientClothes.setClothes(clothesItem);
            clientClothes.setCantidad(cantidad);
            clientClothes.setDate("");

            SessionManager.getInstance().addDetail(clientClothes, cantidad);

            clothesDAO.updateQuantity(clothesItem.getId_clothes(), availableQuantity - cantidad);

            // Llamar al método addClothesToCart del DAO
            Clientes_PrendasDAO dao = new Clientes_PrendasDAO();
            int clientId = getClientId(); // Obtener el ID del cliente actual
            String purchaseDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
            // Verificar si la entrada ya existe y actualizar la cantidad si es necesario
            if (dao.entryExists(clientId, clothesItem.getId_clothes())) {
                dao.updateClothesQuantity(clientId, clothesItem.getId_clothes(), cantidad);
            } else {
                dao.addClothesToCart(clientId, clothesItem.getId_clothes(), purchaseDate, cantidad);
            }

            System.out.println("Prenda añadida a la base de datos.");
        }
    }

    /**
     * Gets the ID of the currently logged-in client.
     * @return The ID of the current client.
     */
    private int getClientId() {
        return SessionManager.getInstance().getCurrentClient().getId_user();
    }

    /**
     * Displays an alert with the given title and message.
     * @param title The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}