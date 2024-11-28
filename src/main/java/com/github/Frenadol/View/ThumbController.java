package com.github.Frenadol.View;

import com.github.Frenadol.Model.Clothes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ThumbController implements Initializable {

    @FXML
    private ImageView productImageView;

    @FXML
    private Label productNameLabel;

    @FXML
    private Label productPriceLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setGarment(Clothes garments) {
        ByteArrayInputStream bait = new ByteArrayInputStream(garments.getClothes_Visual());
        Image img = new Image(bait);
        productImageView.setImage(img);
        productNameLabel.setText(garments.getName_clothes());
        productPriceLabel.setText("Precio: " + garments.getPrice_clothes());
    }
}