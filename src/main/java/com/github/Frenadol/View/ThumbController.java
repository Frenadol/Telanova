package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class ThumbController {

    @FXML
    private Label itemName;
    @FXML
    private Button addToCartButton;

    @FXML
    private ImageView productImageView;

    private Clothes clothesItem;

    public void setGarment(Clothes clothes) {
        this.clothesItem = clothes;
        itemName.setText(clothes.getName_clothes());
        if (clothes.getClothes_Visual() != null && clothes.getClothes_Visual().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(clothes.getClothes_Visual());
            Image image = new Image(bis);
            productImageView.setImage(image);
        }
    }

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

            // Save the cart after adding the item
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}