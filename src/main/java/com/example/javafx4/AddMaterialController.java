package com.example.javafx4;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class AddMaterialController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Spinner<Integer> costid;

    @FXML
    private TextField nameid;

    @FXML
    private Spinner<Integer> quantityid;

    @FXML
    private Spinner<Integer> spinerMinQuantity;

    @FXML
    private TextField typeid;

    private static final String URL = "jdbc:postgresql://localhost:5432/JavaFX4";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @FXML
    void initialize() {
        quantityid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));
        costid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));
        spinerMinQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));


        addBtn.setOnAction(event -> addMaterial());
        backBtn.setOnAction(event -> back());

    }

    private void addMaterial() {
        String type = typeid.getText();
        String name = nameid.getText();

        Integer quantity = quantityid.getValue();
        Integer cost = costid.getValue();
        Integer minQuantity = spinerMinQuantity.getValue();

        if (type.isEmpty() || name.isEmpty() || quantity == null || cost == null || minQuantity == null) {
            System.out.println("Пожалуйста, заполните все поля.");
            return;
        }

        String sql = "INSERT INTO materials (type, name, quantity, cost, minimalquantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, name);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, cost);
            pstmt.setInt(5, minQuantity);

            pstmt.executeUpdate();
            System.out.println("Материал добавлен успешно!");

            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при добавлении материала: " + e.getMessage());
        }
    }

    private void clearFields() {
        typeid.clear();
        nameid.clear();
        quantityid.getValueFactory().setValue(0);
        costid.getValueFactory().setValue(0);
        spinerMinQuantity.getValueFactory().setValue(0);
    }

    private void back() {
    Stage stage = (Stage) backBtn.getScene().getWindow();
    stage.close();
    }
}
