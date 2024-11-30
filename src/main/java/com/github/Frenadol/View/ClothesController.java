package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class ClothesController implements Initializable {

    @FXML
    private TableView<Clothes> clothesTable;

    @FXML
    private TableColumn<Clothes, Integer> idPrendaColumn;

    @FXML
    private TableColumn<Clothes, String> nombrePrendaColumn;

    @FXML
    private TableColumn<Clothes, String> tallaPrendaColumn;

    @FXML
    private TableColumn<Clothes, String> colorPrendaColumn;

    @FXML
    private TableColumn<Clothes, String> descripcionColumn;

    @FXML
    private TableColumn<Clothes, Double> precioColumn;

    @FXML
    private TableColumn<Clothes, ImageView> imagenPrendaColumn;

    private ClothesDAO clothesDao;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clothesDao = new ClothesDAO();
        idPrendaColumn.setCellValueFactory(new PropertyValueFactory<>("id_clothes"));
        nombrePrendaColumn.setCellValueFactory(new PropertyValueFactory<>("name_clothes"));
        tallaPrendaColumn.setCellValueFactory(new PropertyValueFactory<>("size_clothes"));
        colorPrendaColumn.setCellValueFactory(new PropertyValueFactory<>("color_clothes"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("description_clothes"));
        precioColumn.setCellValueFactory(new PropertyValueFactory<>("price_clothes"));
        imagenPrendaColumn.setCellValueFactory(characters -> {
            byte[] visualData = characters.getValue().getClothes_Visual();
            if (visualData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(visualData);
                Image image = new Image(bis);

                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);

                return new SimpleObjectProperty<>(imageView);
            } else {
                System.out.println("visualData es null");
                return null;
            }
        });

        ObservableList<Clothes> clothesList = FXCollections.observableArrayList(clothesDao.findAll());
        clothesTable.setItems(clothesList);
    }
}