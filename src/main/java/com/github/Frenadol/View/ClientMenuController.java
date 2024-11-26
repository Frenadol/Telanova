package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {

    @FXML
    private TableView<Clothes> tableView;

    @FXML
    private TableColumn<Clothes, Image> imagenColumn;

    @FXML
    private TableColumn<Clothes, Double> precioColumn;

    private ObservableList<Clothes> clothesList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clothesList = FXCollections.observableArrayList();

        imagenColumn.setCellValueFactory(clothes -> {
            byte[] visualData = clothes.getValue().getClothes_Visual();
            if (visualData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(visualData);
                Image image = new Image(bis);
                return new SimpleObjectProperty<Image>(image);
            } else {
                System.out.println("No se ha podido cargar la imagen");
                return null;
            }
        });

        imagenColumn.setCellFactory(column -> new TableCell<Clothes, Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    imageView.setOnMouseClicked(event -> {
                        Stage stage = new Stage();
                        ImageView imageViewFull = new ImageView(item);
                        StackPane layout = new StackPane(imageViewFull);
                        Scene scene = new Scene(layout);
                        stage.setScene(scene);
                        stage.show();
                    });
                    setGraphic(imageView);
                } else {
                    setGraphic(null);
                }
            }
        });

        precioColumn.setCellValueFactory(new PropertyValueFactory<>("price_clothes"));

        ClothesDAO clothesDAO = new ClothesDAO();
        List<Clothes> clothes = clothesDAO.findAll();
        clothesList.addAll(clothes);

        tableView.setItems(clothesList);
    }
}