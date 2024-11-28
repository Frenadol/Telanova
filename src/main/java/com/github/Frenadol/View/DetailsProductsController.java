package com.github.Frenadol.View;

import com.github.Frenadol.Model.Clothes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.ByteArrayInputStream;

public class DetailsProductsController {

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productDescription;

    @FXML
    private Label productPrice;

    public void setProductDetails(Clothes clothes) {
        // Configura los detalles de la prenda
        productName.setText(clothes.getName_clothes());
        productDescription.setText(clothes.getDescription_clothes()); // Asumiendo que Clothes tiene una descripción
        productPrice.setText("$" + clothes.getPrice_clothes());

        // Mostrar la imagen de la prenda
        byte[] imageBytes = clothes.getClothes_Visual();
        if (imageBytes != null) {
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            productImage.setImage(image);
        }
    }
}
