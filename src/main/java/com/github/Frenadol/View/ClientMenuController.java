package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientMenuController {
    @FXML
    private GridPane ClothesPane;
    @FXML
    private Spinner<Integer> cantidadSpinner;
    @FXML
    private TableColumn<Clothes, Float> precioColumn;
    private ObservableList<ClothesDAO.ClothesLazyAll> clothesList;
    private ObservableList<Clothes> shoppingCart;
    private ClothesDAO.ClothesLazyAll selectedClothes;

    @FXML
    private Button addToCartButton;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView imageView;
    @FXML
    private Button cleanButton;
    @FXML
    private Button searchButton;

    @FXML
    public void initialize() {
        clothesList = FXCollections.observableArrayList();
        shoppingCart = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();

        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);



    }

    @FXML
    private void cleanSearch() {
        searchTextField.clear();
        clothesList = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();
        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);
    }

    @FXML
    private void searchClothes() {
        String searchQuery = searchTextField.getText();
        if (searchQuery.isEmpty()) {
            System.out.println("Por favor, ingresa un texto de búsqueda.");
            return;
        }
        ClothesDAO clothesDAO = new ClothesDAO();
        List<Clothes> searchedResults = clothesDAO.findClothesByName(searchQuery);
        if (searchedResults.isEmpty()) {
            System.out.println("No se encontraron resultados");
            return;
        }
        mostrarProductos(searchedResults);
    }



    public void mostrarProductos(List<? extends Clothes> clothesList) {
        ClothesPane.getChildren().clear();
        final int MAX_COLUMNS = 3;
        int columns = 0;
        int rows = 0;

        for (Clothes clothes : clothesList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Thumb.fxml"));
                VBox box = loader.load();
                ThumbController thumbController = loader.getController();
                thumbController.setGarment(clothes);

                box.setOnMouseClicked(event -> {
                    selectedClothes = (ClothesDAO.ClothesLazyAll) clothes;
                    System.out.println("Producto seleccionado: " + clothes.getName_clothes());
                    showProductDetails((ClothesDAO.ClothesLazyAll) clothes);
                });

                ClothesPane.add(box, columns++, rows);

                if (columns == MAX_COLUMNS) {
                    columns = 0;
                    rows++;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void openShoppingCart() {
        try {
            System.out.println("Abriendo el carrito de compras...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            Stage stage = (Stage) ClothesPane.getScene().getWindow();
            System.out.println("Ventana actual obtenida.");

            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
            System.out.println("Escena cambiada correctamente.");

            ShoppingCartController shoppingCartController = loader.getController();

            List<Client_Clothes> detalles = SessionManager.getInstance().getDetails();
            ObservableList<Client_Clothes> details = FXCollections.observableArrayList(detalles);

            if (details != null && !details.isEmpty()) {
                shoppingCartController.setShoppingCart(details);
            } else {
                System.out.println("Lista de detalles nula o vacía");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showProductDetails(ClothesDAO.ClothesLazyAll clothesLazy) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsProducts.fxml"));
            AnchorPane productDetailPane = loader.load();

            DetailsProductsController detailsProductsController = loader.getController();
            ClothesDAO clothesDAO = new ClothesDAO();
            Clothes clothes = clothesDAO.findClothesById(clothesLazy.getId_clothes());
            detailsProductsController.setProductDetails(clothes);

            Stage detailStage = new Stage();
            Scene scene = new Scene(productDetailPane);
            detailStage.setScene(scene);
            detailStage.setTitle("Detalle del Producto");
            detailStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}