package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.Utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class StoragePanelController {

    @FXML
    private VBox itemsContainer;

    @FXML
    private void initialize() {
        loadClothes();
    }

    private void loadClothes() {
        Storage currentStorage = SessionManager.getInstance().getCurrentStorage();
        if (currentStorage != null) {
            ClothesDAO clothesDAO = new ClothesDAO();
            List<Clothes> clothesList = clothesDAO.findByStorageId(currentStorage.getId_storage());

            for (Clothes clothes : clothesList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminThumb.fxml"));
                    VBox thumb = loader.load();
                    ThumbControllerAdmin controller = loader.getController();
                    controller.setGarment(clothes);
                    itemsContainer.getChildren().add(thumb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}