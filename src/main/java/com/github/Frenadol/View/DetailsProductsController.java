package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class DetailsProductsController {

    @FXML
    private ImageView productImage;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private Label productDescription;
    @FXML
    private Label productSize;
    @FXML
    private Label productColor;
    @FXML
    private Label productCategory;
    @FXML
    private Label productQuantity;

    private ClothesDAO clothesDAO;

    public DetailsProductsController() {
        this.clothesDAO = new ClothesDAO();
    }

    public void displayProductDetails(int id) {
        Clothes clothes = clothesDAO.findClothesById(id);
        if (clothes != null) {
            setProductDetails(clothes);
        }
    }

    public void setProductDetails(Clothes clothes) {
        productName.setText(clothes.getName_clothes());
        productPrice.setText(String.format("$%.2f", clothes.getPrice_clothes()));
        productDescription.setText(clothes.getDescription_clothes());
        productSize.setText("Talla: " + clothes.getSize_clothes());
        productColor.setText("Color: " + clothes.getColor_clothes());
        productCategory.setText("Categoría: " + clothes.getCategory());
        productQuantity.setText("Cantidad disponible: " + clothes.getCantidad());

        if (clothes.getClothes_Visual() != null) {
            Image image = new Image(new ByteArrayInputStream(clothes.getClothes_Visual()));
            productImage.setImage(image);
        }
    }
}