package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Clothes;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class ClientMenuController {

    @FXML
    private TableView<Clothes> tableView;

    @FXML
    private TableColumn<Clothes, Image> imagenColumn;

    @FXML
    private TableColumn<Clothes, Float> precioColumn; // Mantén el tipo Float para mostrar el precio

    private ObservableList<Clothes> clothesList;
    private ObservableList<Clothes> shoppingCart;

    @FXML
    private Button addToCartButton;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        clothesList = FXCollections.observableArrayList();
        shoppingCart = FXCollections.observableArrayList();

        // Configuración de la columna de imagen
        imagenColumn.setCellValueFactory(clothes -> {
            byte[] visualData = clothes.getValue().getClothes_Visual();
            if (visualData != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(visualData);
                Image image = new Image(bis);
                return new SimpleObjectProperty<>(image);
            } else {
                System.out.println("No se ha podido cargar la imagen");
                return null;
            }
        });

        // Configuración de la celda para mostrar la imagen
        imagenColumn.setCellFactory(column -> new TableCell<Clothes, Image>() {
            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    ImageView imageView = new ImageView(item);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    setGraphic(imageView);
                } else {
                    setGraphic(null);
                }
            }
        });

        // Configuración de la columna de precio utilizando el getter getPrice_clothes()
        precioColumn.setCellValueFactory(cellData -> {
            return new SimpleObjectProperty<>(cellData.getValue().getPrice_clothes());
        });

        // Cargar prendas desde la base de datos
        ClothesDAO clothesDAO = new ClothesDAO();
        List<Clothes> clothes = clothesDAO.findAll();
        clothesList.addAll(clothes);

        tableView.setItems(clothesList);

        // Acción para añadir al carrito desde el botón
        addToCartButton.setOnAction(event -> addToCart());
    }

    @FXML
    private void addToCart() {
        // Obtener la prenda seleccionada del TableView
        Clothes selectedClothes = tableView.getSelectionModel().getSelectedItem();

        if (selectedClothes != null) {
            // Verificar si la prenda ya está en el carrito
            if (!shoppingCart.contains(selectedClothes)) {
                shoppingCart.add(selectedClothes);
                System.out.println("Prenda añadida al carrito: " + selectedClothes.getName_clothes());
            } else {
                System.out.println("La prenda ya está en el carrito.");
            }
        } else {
            // Si no se ha seleccionado ninguna prenda
            System.out.println("Por favor, selecciona una prenda.");
        }
    }

    // Método para abrir el carrito de compras
    @FXML
    private void openShoppingCart() {
        try {
            // Cargar la vista de ShoppingCart
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/github/Frenadol/View/ShoppingCart.fxml"));
            Stage stage = (Stage) tableView.getScene().getWindow();  // Obtener la ventana actual

            // Cargar la escena
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            // Obtener el controlador de la vista ShoppingCart
            ShoppingCartController shoppingCartController = loader.getController();

            // Pasar el carrito de compras al ShoppingCartController
            shoppingCartController.setShoppingCart(shoppingCart); // Pasamos las prendas del carrito

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
