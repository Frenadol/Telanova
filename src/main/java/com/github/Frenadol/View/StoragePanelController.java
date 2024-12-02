package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.Utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StoragePanelController {

    @FXML
    private GridPane itemsContainer;

    @FXML
    private TableView<Result> resultsTable;

    @FXML
    private TableColumn<Result, String> attributeColumn;

    @FXML
    private TableColumn<Result, Integer> countColumn;

    private ClothesDAO clothesDAO;

    @FXML
    private void initialize() {
        clothesDAO = new ClothesDAO();
        loadClothes();

        attributeColumn.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    private void loadClothes() {
        Storage currentStorage = SessionManager.getInstance().getCurrentStorage();
        if (currentStorage != null) {
            List<Clothes> clothesList = clothesDAO.findByStorageId(currentStorage.getId_storage());

            final int MAX_COLUMNS = 5;
            int columns = 0;
            int rows = 0;

            for (Clothes clothes : clothesList) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminThumb.fxml"));
                    VBox thumb = loader.load();
                    ThumbControllerAdmin controller = loader.getController();
                    controller.setGarment(clothes);

                    itemsContainer.add(thumb, columns, rows);
                    columns++;

                    if (columns == MAX_COLUMNS) {
                        columns = 0;
                        rows++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleCountByCategory() {
        Map<String, Integer> counts = clothesDAO.countClothesByCategory();
        showCounts(counts);
    }

    @FXML
    private void handleCountBySize() {
        Map<String, Integer> counts = clothesDAO.countClothesBySize();
        showCounts(counts);
    }

    @FXML
    private void handleCountByColor() {
        Map<String, Integer> counts = clothesDAO.countClothesByColor();
        showCounts(counts);
    }

    private void showCounts(Map<String, Integer> counts) {
        ObservableList<Result> data = FXCollections.observableArrayList();
        counts.forEach((key, value) -> data.add(new Result(key, value)));
        resultsTable.setItems(data);
    }

    public static class Result {
        private final String attribute;
        private final Integer count;

        public Result(String attribute, Integer count) {
            this.attribute = attribute;
            this.count = count;
        }

        public String getAttribute() {
            return attribute;
        }

        public Integer getCount() {
            return count;
        }
    }
}