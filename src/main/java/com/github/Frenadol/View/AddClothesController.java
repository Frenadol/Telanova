package com.github.Frenadol.View;

import com.github.Frenadol.App;
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

    /** Initializes the controller. */
    @FXML
    private void initialize() {
        if (GarmentSizeComboBox.getItems().isEmpty()) {
            populateComboBoxes();
        }
        if (StorageComboBox.getItems().isEmpty()) {
            populateStorageComboBox();
        }
    }

    /** Populates the combo boxes with predefined values. */
    private void populateComboBoxes() {
        GarmentSizeComboBox.getItems().addAll("S", "M", "L", "XL", "XXL");
        GarmentColorComboBox.getItems().addAll("Rojo", "Azul", "Verde", "Negro", "Blanco", "Amarillo", "Naranja", "Rosa", "Morado", "Marrón", "Gris", "Beige");
        GarmentCategoryComboBox.getItems().addAll("Deportiva", "Formal", "Informal", "Casual", "Exterior", "Interior");
    }

    /** Populates the storage combo box with values from the database. */
    private void populateStorageComboBox() {
        List<Storage> storages = storageDAO.findAllStorages();
        StorageComboBox.getItems().addAll(storages);
    }

    /** Adds an image to the garment. */
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

    /** Navigates to the admin panel. */
    @FXML
    private void goToAdminPanel() {
        try {
            App.setRoot("View/AdminPanel");
        } catch (IOException e) {
            showAlert("Error", "Error al volver al panel de administración: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /** Saves the garment details. */
    @FXML
    private void saveGarment() {
        String garmentName = Garment_name.getText();
        if (garmentName == null || garmentName.isEmpty()) {
            showAlert("Error", "Debe ingresar un nombre para la prenda", Alert.AlertType.ERROR);
            return;
        }
        if (clothesDAO.garmentExists(garmentName)) {
            showAlert("Error", "Ya existe una prenda con el mismo nombre", Alert.AlertType.ERROR);
            return;
        }

        String selectedSize = GarmentSizeComboBox.getValue();
        if (selectedSize == null || selectedSize.isEmpty()) {
            showAlert("Error", "Debe seleccionar una talla", Alert.AlertType.ERROR);
            return;
        }

        String selectedColor = GarmentColorComboBox.getValue();
        if (selectedColor == null || selectedColor.isEmpty()) {
            showAlert("Error", "Debe seleccionar un color", Alert.AlertType.ERROR);
            return;
        }

        String description = GarmentDescriptionField.getText();
        if (description == null || description.isEmpty()) {
            showAlert("Error", "Debe ingresar una descripción", Alert.AlertType.ERROR);
            return;
        }

        String priceText = GarmentPriceField.getText();
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Debe ingresar un precio válido", Alert.AlertType.ERROR);
            return;
        }

        String selectedCategory = GarmentCategoryComboBox.getValue();
        if (selectedCategory == null || selectedCategory.isEmpty()) {
            showAlert("Error", "Debe seleccionar una categoría", Alert.AlertType.ERROR);
            return;
        }

        String quantityText = GarmentQuantityField.getText();
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Debe ingresar una cantidad válida", Alert.AlertType.ERROR);
            return;
        }

        if (imageFile == null) {
            showAlert("Error", "Debe seleccionar una imagen", Alert.AlertType.ERROR);
            return;
        }

        Storage selectedStorage = StorageComboBox.getValue();
        if (selectedStorage == null) {
            showAlert("Error", "Debe seleccionar un almacén", Alert.AlertType.ERROR);
            return;
        }

        Clothes garment = new Clothes();
        garment.setName_clothes(garmentName);
        garment.setSize_clothes(selectedSize);
        garment.setColor_clothes(selectedColor);
        garment.setDescription_clothes(description);
        garment.setPrice_clothes(price);
        garment.setCategory(selectedCategory);
        garment.setCantidad(quantity);

        Worker currentWorker = sessionManager.getCurrentWorker();
        garment.setWorker(currentWorker);

        try (FileInputStream fis = new FileInputStream(imageFile)) {
            garment.setClothes_Visual(fis.readAllBytes());
        } catch (IOException e) {
            showAlert("Error", "Error al leer la imagen: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        clothesDAO.insertGarment(garment, selectedStorage.getId_storage());
        int idClothes = clothesDAO.getLastInsertedId();
        clothesDAO.insertCreatedClothes(idClothes, currentWorker.getId_user());
        showAlert("Éxito", "Prenda insertada correctamente", Alert.AlertType.INFORMATION);
    }

    /** Shows an alert with the given title, message, and alert type. */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}