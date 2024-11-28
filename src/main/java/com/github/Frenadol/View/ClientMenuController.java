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

        // Set the action for the Add to Cart button
        addToCartButton.setOnAction(event -> addToCart());
    }

    private void initializeSpinner() {
        // Create a new IntegerSpinnerValueFactory and set it to the Spinner
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1); // Min, Max, and Default value
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

                // Cuando se haga clic en la miniatura, mostrar los detalles del producto
                box.setOnMouseClicked(event -> {
                    selectedClothes = clothes;  // Asignar la prenda seleccionada
                    System.out.println("Producto seleccionado: " + clothes.getName_clothes());  // Mensaje para verificar
                    showProductDetails(clothes);  // Llamar al método para mostrar los detalles
                });

                ClothesPane.add(box, columns++, rows);

                // Si alcanzamos el número máximo de columnas, pasamos a la siguiente fila
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
            // Get the value from the Spinner with a null check
            Integer cantidad = cantidadSpinner.getValue();

            // Ensure that the Spinner has a valid value
            if (cantidad == null || cantidad <= 0) {
                System.out.println("Por favor, selecciona una cantidad válida.");
                return;  // Exit if no valid quantity
            }

            // Create a confirmation dialog
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmar adición al carrito");
            confirmationAlert.setHeaderText("¿Estás seguro de añadir al carrito?");
            confirmationAlert.setContentText("¿Deseas añadir " + cantidad + " de " + selectedClothes.getName_clothes() + " al carrito?");

            // Wait for the user to click OK or Cancel
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Verificar si la prenda ya está en el carrito
                    if (!shoppingCart.contains(selectedClothes)) {
                        shoppingCart.add(selectedClothes);
                        System.out.println("Prenda añadida al carrito: " + selectedClothes.getName_clothes());
                    } else {
                        System.out.println("La prenda ya está en el carrito.");
                    }

                    // Add the selected clothes and quantity to the session manager
                    SessionManager sessionManager = SessionManager.getInstance();
                    sessionManager.addDetail(selectedClothes, cantidad); // Pass the correct quantity

                    // Optionally, you can also create a new Client_Clothes object to store in the database
                    Client_Clothes clientClothes = new Client_Clothes();
                    clientClothes.setClothes(selectedClothes);
                    clientClothes.setCantidad(cantidad);
                    clientClothes.setDate("");  // Ignore the date for now

                    // Store clientClothes in the database or the shopping cart

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
            // Cargar la vista de ShoppingCart
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShoppingCart.fxml"));
            Stage stage = (Stage) ClothesPane.getScene().getWindow();  // Obtener la ventana actual

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
