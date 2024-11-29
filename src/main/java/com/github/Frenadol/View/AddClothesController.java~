package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Model.Worker;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AddClothesController {

    @FXML
    private TextField Garment_name;
    @FXML
    private ComboBox<String> GarmentSizeComboBox; // Usamos ComboBox para seleccionar la talla
    @FXML
    private TextField GarmentColorField;
    @FXML
    private TextField GarmentDescriptionField;
    @FXML
    private TextField GarmentPriceField;
    @FXML
    private ImageView GarmentImageField;

    private File imageFile;

    private ClothesDAO clothesDAO = new ClothesDAO();
    private SessionManager sessionManager = SessionManager.getInstance();

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

        // Obtener la talla seleccionada del ComboBox
        String selectedSize = GarmentSizeComboBox.getValue();
        if (selectedSize != null && !selectedSize.isEmpty()) {
            garment.setSize_clothes(selectedSize);
        } else {
            System.out.println("Debe seleccionar una talla");
        }

        garment.setColor_clothes(GarmentColorField.getText());
        garment.setDescription_clothes(GarmentDescriptionField.getText());
        garment.setPrice_clothes(Float.valueOf(GarmentPriceField.getText()));

        Worker currentWorker = sessionManager.getCurrentWorker();
        currentWorker.setId_user(currentWorker.getId_user());
        garment.setWorker(currentWorker);

        // Guardar la imagen si fue seleccionada
        if (imageFile != null) {
            try (FileInputStream fis = new FileInputStream(imageFile)) {
                garment.setClothes_Visual(fis.readAllBytes());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        // Guardar la prenda en la base de datos
        clothesDAO.insertGarment(garment);
        int idClothes = clothesDAO.getLastInsertedId();
        clothesDAO.insertCreatedClothes(idClothes, currentWorker.getId_user());
    }
}
