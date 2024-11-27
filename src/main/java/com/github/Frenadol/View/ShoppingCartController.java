package com.github.Frenadol.View;

import com.github.Frenadol.Model.Clothes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.ByteArrayInputStream;

public class ShoppingCartController {

    @FXML
    private TableView<Clothes> shoppingCartTableView;

    @FXML
    private TableColumn<Clothes, Image> imageColumn;

    @FXML
    private TableColumn<Clothes, String> nameColumn;

    @FXML
    private TableColumn<Clothes, Double> priceColumn;

    private ObservableList<Clothes> shoppingCart;  // Lista de prendas en el carrito

    // Método para recibir las prendas del carrito
    public void setShoppingCart(ObservableList<Clothes> shoppingCart) {
        this.shoppingCart = shoppingCart;

        // Configuración de las columnas de la tabla
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name_clothes"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price_clothes"));

        // Configuración de la columna de imagen
        imageColumn.setCellValueFactory(clothes -> {
            byte[] visualData = clothes.getValue().getClothes_Visual();
            if (visualData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(visualData);
                Image image = new Image(bis);
                return new SimpleObjectProperty<Image>(image);
            }
            return null;
        });

        imageColumn.setCellFactory(column -> new TableCell<Clothes, Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    setGraphic(imageView);
                } else {
                    setGraphic(null);
                }
            }
        });

        // Agregar las prendas al TableView del carrito
        shoppingCartTableView.setItems(shoppingCart);
    }
}
