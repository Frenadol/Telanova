package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

public class ChanguePropertiesController {

    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> sizeComboBox;
    @FXML
    private ComboBox<String> colorComboBox;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField imageField;
    @FXML
    private ImageView imageView;

    private Clothes clothesItem;
    private ClothesDAO clothesDAO;
    private File imageFile;

    public ChanguePropertiesController() {
        this.clothesDAO = new ClothesDAO();
    }

    public void setClothesItem(Clothes clothes) {
        this.clothesItem = clothes;
        nameField.setText(clothes.getName_clothes());
        sizeComboBox.setValue(clothes.getSize_clothes());
        colorComboBox.setValue(clothes.getColor_clothes());
        descriptionField.setText(clothes.getDescription_clothes());
        priceField.setText(String.valueOf(clothes.getPrice_clothes()));
        categoryComboBox.setValue(clothes.getCategory());
        quantityField.setText(String.valueOf(clothes.getCantidad()));
        if (clothes.getClothes_Visual() != null) {
            imageView.setImage(new Image(new ByteArrayInputStream(clothes.getClothes_Visual())));
            imageField.setText("Imagen Seleccionada");
        } else {
            imageField.setText("Sin Imagen");
        }
    }

    public void loadClothesItem(int clothesId) {
        Clothes clothes = clothesDAO.findClothesById(clothesId);
        setClothesItem(clothes);
    }

    @FXML
    private void initialize() {
        sizeComboBox.getItems().addAll("S", "M", "L", "XL", "XXL"); // Add sizes
        colorComboBox.getItems().addAll("Rojo", "Azul", "Verde", "Negro", "Blanco", "Amarillo", "Naranja", "Rosa", "Morado", "Marrón", "Gris", "Beige"); // Add colors
        categoryComboBox.getItems().addAll("Deportiva", "Formal", "Informal", "Casual", "Exterior", "Interior"); // Add categories
    }

    @FXML
    private void handleSave() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmar Guardado");
        confirmationAlert.setHeaderText("¿Estás seguro de guardar los cambios?");
        Optional<ButtonType> response = confirmationAlert.showAndWait();
        if (response.isPresent() && response.get() == ButtonType.OK) {
            boolean updated = false;

            if (!nameField.getText().equals(clothesItem.getName_clothes())) {
                clothesItem.setName_clothes(nameField.getText());
                updated = true;
            }
            if (!sizeComboBox.getValue().equals(clothesItem.getSize_clothes())) {
                clothesItem.setSize_clothes(sizeComboBox.getValue());
                updated = true;
            }
            if (!colorComboBox.getValue().equals(clothesItem.getColor_clothes())) {
                clothesItem.setColor_clothes(colorComboBox.getValue());
                updated = true;
            }
            if (!descriptionField.getText().equals(clothesItem.getDescription_clothes())) {
                clothesItem.setDescription_clothes(descriptionField.getText());
                updated = true;
            }
            try {
                double price = Double.parseDouble(priceField.getText());
                if (price != clothesItem.getPrice_clothes()) {
                    clothesItem.setPrice_clothes(price);
                    updated = true;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Debe ingresar un precio válido", Alert.AlertType.ERROR);
                return;
            }
            if (!categoryComboBox.getValue().equals(clothesItem.getCategory())) {
                clothesItem.setCategory(categoryComboBox.getValue());
                updated = true;
            }
            try {
                int quantity = Integer.parseInt(quantityField.getText());
                if (quantity != clothesItem.getCantidad()) {
                    clothesItem.setCantidad(quantity);
                    updated = true;
                }
            } catch (NumberFormatException e) {
                showAlert("Error", "Debe ingresar una cantidad válida", Alert.AlertType.ERROR);
                return;
            }
            if (imageFile != null) {
                try {
                    byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                    clothesItem.setClothes_Visual(imageBytes);
                    updated = true;
                } catch (IOException e) {
                    showAlert("Error", "Error al leer la imagen", Alert.AlertType.ERROR);
                    return;
                }
            }

            if (updated) {
                clothesDAO.updateClothes(clothesItem);
                showAlert("Éxito", "Los cambios se han guardado correctamente.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Información", "No se realizaron cambios.", Alert.AlertType.INFORMATION);
            }
        }
    }

    @FXML
    private void handleBack() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            imageFile = selectedFile;
            imageField.setText(selectedFile.getAbsolutePath());
            imageView.setImage(new Image(selectedFile.toURI().toString()));
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