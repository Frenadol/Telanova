package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Dao.StorageDAO;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class AddClothesController {

    @FXML
    private TextField Garment_name;
    @FXML
    private ComboBox<String> GarmentSizeComboBox;
    @FXML
    private ComboBox<String> GarmentColorComboBox;
    @FXML
    private TextField GarmentDescriptionField;
    @FXML
    private TextField GarmentPriceField;
    @FXML
    private ImageView GarmentImageField;
    @FXML
    private ComboBox<String> GarmentCategoryComboBox;
    @FXML
    private TextField GarmentQuantityField;
    @FXML
    private ComboBox<Storage> StorageComboBox; // ComboBox for selecting storage

    private File imageFile;

    private ClothesDAO clothesDAO = new ClothesDAO();
    private StorageDAO storageDAO = new StorageDAO();
    private SessionManager sessionManager = SessionManager.getInstance();

    @FXML
    private void initialize() {
        populateComboBoxes();
        populateStorageComboBox();
    }

    private void populateComboBoxes() {
        GarmentSizeComboBox.getItems().addAll("S", "M", "L", "XL", "XXL");
        GarmentColorComboBox.getItems().addAll("Rojo", "Azul", "Verde", "Negro", "Blanco", "Amarillo", "Naranja", "Rosa", "Morado", "Marrón", "Gris", "Beige");
        GarmentCategoryComboBox.getItems().addAll("Deportiva", "Formal", "Informal", "Casual", "Exterior", "Interior");
    }

    private void populateStorageComboBox() {
        List<Storage> storages = storageDAO.findAllStorages();
        StorageComboBox.getItems().addAll(storages);
    }

    @FXML
    private void addImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageFile = selectedFile;
            Image image = new Image(selectedFile.toURI().toString());
            GarmentImageField.setImage(image);
        }
    }

    @FXML
    private void saveGarment() {
        Clothes garment = new Clothes();
        garment.setName_clothes(Garment_name.getText());

        String selectedSize = GarmentSizeComboBox.getValue();
        if (selectedSize != null && !selectedSize.isEmpty()) {
            garment.setSize_clothes(selectedSize);
        } else {
            showAlert("Error", "Debe seleccionar una talla", Alert.AlertType.ERROR);
            return;
        }

        String selectedColor = GarmentColorComboBox.getValue();
        if (selectedColor != null && !selectedColor.isEmpty()) {
            garment.setColor_clothes(selectedColor);
        } else {
            showAlert("Error", "Debe seleccionar un color", Alert.AlertType.ERROR);
            return;
        }

        garment.setDescription_clothes(GarmentDescriptionField.getText());
        try {
            garment.setPrice_clothes(Double.parseDouble(GarmentPriceField.getText()));
        } catch (NumberFormatException e) {
            showAlert("Error", "Debe ingresar un precio válido", Alert.AlertType.ERROR);
            return;
        }

        String selectedCategory = GarmentCategoryComboBox.getValue();
        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            garment.setCategory(selectedCategory);
        } else {
            showAlert("Error", "Debe seleccionar una categoría", Alert.AlertType.ERROR);
            return;
        }

        try {
            int cantidad = Integer.parseInt(GarmentQuantityField.getText());
            garment.setCantidad(cantidad);
        } catch (NumberFormatException e) {
            showAlert("Error", "Debe ingresar una cantidad válida", Alert.AlertType.ERROR);
            return;
        }

        Worker currentWorker = sessionManager.getCurrentWorker();
        garment.setWorker(currentWorker);

        if (imageFile != null) {
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                garment.setClothes_Visual(fis.readAllBytes());
            } catch (IOException e) {
                showAlert("Error", "Error al leer la imagen: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
        }

        Storage selectedStorage = StorageComboBox.getValue();
        if (selectedStorage != null) {
            clothesDAO.insertGarment(garment, selectedStorage.getId_storage());
            int idClothes = clothesDAO.getLastInsertedId();
            clothesDAO.insertCreatedClothes(idClothes, currentWorker.getId_user());
            showAlert("Éxito", "Prenda insertada correctamente", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Debe seleccionar un almacén", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}