package com.github.Frenadol.View;

import com.github.Frenadol.Model.Clothes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ThumbControllerAdmin {

    @FXML
    private Label itemName;

    @FXML
    private Label productPriceLabel;

    @FXML
    private ImageView productImageView;

    private Clothes clothesItem;

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

    @FXML
    private void handleViewProperties() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsProducts.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            DetailsProductsController controller = loader.getController();
            controller.displayProductDetails(clothesItem.getId_clothes());
            stage.setScene(scene);
            stage.setTitle("Detalles del Producto");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeProperties() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangueProperties.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());
            ChanguePropertiesController controller = loader.getController();
            controller.loadClothesItem(clothesItem.getId_clothes()); // Pasar la ID de la prenda
            stage.setScene(scene);
            stage.setTitle("Cambiar Propiedades");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}