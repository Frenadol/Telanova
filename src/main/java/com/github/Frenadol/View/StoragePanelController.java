package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Model.Storage;
import com.github.Frenadol.Utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    private void initialize() {
        clothesDAO = new ClothesDAO();
        loadClothes();

        attributeColumn.setCellValueFactory(new PropertyValueFactory<>("attribute"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
    }

    /**
     * Loads the clothes from the current storage and displays them in the items container.
     */
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

    /**
     * Handles the action to count clothes by category and displays the results.
     */
    @FXML
    private void handleCountByCategory() {
        Map<String, Integer> counts = clothesDAO.countClothesByCategory();
        showCounts(counts);
    }

    /**
     * Handles the action to count clothes by size and displays the results.
     */
    @FXML
    private void handleCountBySize() {
        Map<String, Integer> counts = clothesDAO.countClothesBySize();
        showCounts(counts);
    }

    /**
     * Handles the action to count clothes by color and displays the results.
     */
    @FXML
    private void handleCountByColor() {
        Map<String, Integer> counts = clothesDAO.countClothesByColor();
        showCounts(counts);
    }

    /**
     * Displays the counts in the results table.
     *
     * @param counts The map containing the counts to display.
     */
    private void showCounts(Map<String, Integer> counts) {
        ObservableList<Result> data = FXCollections.observableArrayList();
        counts.forEach((key, value) -> data.add(new Result(key, value)));
        resultsTable.setItems(data);
    }

    @FXML
/**
 * Changes the current scene to the Admin Panel.
 */
    public void goToInitialMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPanel.fxml"));
            Stage stage = (Stage) itemsContainer.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Represents a result with an attribute and its count.
     */
    public static class Result {
        private final String attribute;
        private final Integer count;

        /**
         * Constructs a Result with the specified attribute and count.
         *
         * @param attribute The attribute of the result.
         * @param count     The count of the result.
         */
        public Result(String attribute, Integer count) {
            this.attribute = attribute;
            this.count = count;
        }

        /**
         * Gets the attribute of the result.
         *
         * @return The attribute of the result.
         */
        public String getAttribute() {
            return attribute;
        }

        /**
         * Gets the count of the result.
         *
         * @return The count of the result.
         */
        public Integer getCount() {
            return count;
        }
    }
}