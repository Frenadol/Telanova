package com.github.Frenadol.View;

import com.github.Frenadol.App;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.SessionManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ShoppingCartController {

    @FXML
    private TableView<Client_Clothes> shoppingCartTableView;

    @FXML
    private TableColumn<Client_Clothes, Image> imageColumn;

    @FXML
    private TableColumn<Client_Clothes, String> nameColumn;

    @FXML
    private TableColumn<Client_Clothes, Double> priceColumn;

    @FXML
    private TableColumn<SessionManager, Integer> quantityColumn;

    private ObservableList<Client_Clothes> shoppingCart;

    public void setShoppingCart(ObservableList<Client_Clothes> shoppingCart) {
        this.shoppingCart = shoppingCart;

        nameColumn.setCellValueFactory(clothes -> {
            return new SimpleObjectProperty<String>(clothes.getValue().getClothes().getName_clothes());
        });

        priceColumn.setCellValueFactory(clothes -> {
            return new SimpleObjectProperty<Double>(clothes.getValue().getClothes().getPrice_clothes());
        });

        imageColumn.setCellValueFactory(clothes -> {
            byte[] visualData = clothes.getValue().getClothes().getClothes_Visual();
            if (visualData != null) {
                InputStream is = new ByteArrayInputStream(visualData);
                Image image = new Image(is);
                return new SimpleObjectProperty<Image>(image);
            }
            return null;
        });

        imageColumn.setCellFactory(column -> new TableCell<Client_Clothes, Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(100); // Establece el ancho de la imagen
                    imageView.setFitHeight(100); // Establece el alto de la imagen
                    setGraphic(imageView);
                }
            }
        });

        shoppingCartTableView.setItems(shoppingCart);
    }

    @FXML
    public void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClientMenu.fxml"));
            Parent root = loader.load();

            // Obtener la ventana actual
            Stage stage = (Stage) shoppingCartTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}