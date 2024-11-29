package com.github.Frenadol.View;

import com.github.Frenadol.Dao.ClothesDAO;
import com.github.Frenadol.Model.Client_Clothes;
import com.github.Frenadol.Model.Clothes;
import com.github.Frenadol.Model.Session;
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
import java.util.ArrayList;
import java.util.List;

public class ClientMenuController {
    @FXML
    private GridPane ClothesPane;
    @FXML
    private Spinner<Integer> cantidadSpinner;
    @FXML
    private TableColumn<Clothes, Float> precioColumn;
    private ObservableList<Clothes> clothesList;
    private ObservableList<Clothes> shoppingCart;
    private Clothes selectedClothes;

    @FXML
    private Button addToCartButton;

    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() {
        clothesList = FXCollections.observableArrayList();
        shoppingCart = FXCollections.observableArrayList();
        ClothesDAO clothesDAO = new ClothesDAO();

        List<ClothesDAO.ClothesLazyAll> clothes = clothesDAO.findAllClothesLazy();
        clothesList.addAll(clothes);
        mostrarProductos(clothesList);


        initializeSpinner();


        addToCartButton.setOnAction(event -> addToCart());
    }

    private void initializeSpinner() {

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        cantidadSpinner.setValueFactory(valueFactory);
    }

    public void mostrarProductos(List<Clothes> clothesList) {
        ClothesPane.getChildren().clear();  // Limpiar el GridPane antes de agregar nuevos elementos
        final int MAX_COLUMNS = 3;  // Número máximo de columnas
        int columns = 0;
        int rows = 0;

        for (Clothes clothes : clothesList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Thumb.fxml"));
                VBox box = loader.load();  // Cargar el contenedor de la miniatura
                ThumbController thumbController = loader.getController();
                thumbController.setGarment(clothes);  // Pasar el producto al controlador de la miniatura

                box.setOnMouseClicked(event -> {
                    selectedClothes = clothes;  // Asignar la prenda seleccionada
                    System.out.println("Producto seleccionado: " + clothes.getName_clothes());  // Mensaje para verificar
                    showProductDetails(clothes);  // Llamar al método para mostrar los detalles
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
    private void addToCart() {

        if (selectedClothes != null) {

            Integer cantidad = cantidadSpinner.getValue();

            if (cantidad == null || cantidad <= 0) {
                System.out.println("Por favor, selecciona una cantidad válida.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmar adición al carrito");
            confirmationAlert.setHeaderText("¿Estás seguro de añadir al carrito?");
            confirmationAlert.setContentText("¿Deseas añadir " + cantidad + " de " + selectedClothes.getName_clothes() + " al carrito?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (!shoppingCart.contains(selectedClothes)) {
                        shoppingCart.add(selectedClothes);
                        System.out.println("Prenda añadida al carrito: " + selectedClothes.getName_clothes());
                    } else {
                        System.out.println("La prenda ya está en el carrito.");
                    }

                    SessionManager sessionManager = SessionManager.getInstance();
                    sessionManager.addDetail(selectedClothes, cantidad);

                    Client_Clothes clientClothes = new Client_Clothes();
                    clientClothes.setClothes(selectedClothes);
                    clientClothes.setCantidad(cantidad);
                    clientClothes.setDate("");


                }
            });
        } else {
            // Si no se ha seleccionado ninguna prenda
            System.out.println("Por favor, selecciona una prenda.");
        }
    }

    // Método para abrir el carrito de compras
    @FXML
    private void openShoppingCart() {
        try {
            System.out.println("Abriendo el carrito de compras...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            Stage stage = (Stage) ClothesPane.getScene().getWindow();  // Obtener la ventana actual
            System.out.println("Ventana actual obtenida.");

            // Cargar la nueva escena
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);  // Establecer la nueva escena en la ventana
            stage.show();
            System.out.println("Escena cambiada correctamente.");

            // Obtener el controlador de la nueva vista
            ShoppingCartController shoppingCartController = loader.getController();

            ArrayList<Client_Clothes> detalles = SessionManager.getInstance().getDetails();
            ObservableList<Client_Clothes> details = FXCollections.observableArrayList(detalles);

            // Verificar si la lista de detalles es no nula y contiene objetos de tipo Client_Clothes
            if (details != null && !details.isEmpty()) {

            } else {
                System.out.println("Lista de detalles nula o vacía");
            }

            // Pasar el carrito de compras al ShoppingCartController
            shoppingCartController.setShoppingCart(details);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void showProductDetails(Clothes clothes) {
        try {
            // Cargar la vista de detalles
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsProducts.fxml"));
            AnchorPane productDetailPane = loader.load();

            // Obtener el controlador de la vista de detalles
            DetailsProductsController detailsProductsController = loader.getController();
            detailsProductsController.setProductDetails(selectedClothes); // Pasar el objeto completo de la prenda con su descripción

            // Crear una nueva ventana emergente (Stage) para mostrar los detalles
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
