package com.example.javafx4;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Spinner;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.SpinnerValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EditMaterialController {
    @FXML
    private TextField edittypeid;

    @FXML
    private TextField editnameid;

    @FXML
    private Spinner<Integer> editquantityid;

    @FXML
    private Spinner<Integer> editcostid;

    @FXML
    private Spinner<Integer> editspinerMinQuantity;

    @FXML
    private Button editaddBtn;

    @FXML
    private Button editbackBtn;

    private int materialId;

    private static final String URL = "jdbc:postgresql://localhost:5432/JavaFX4";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    @FXML
    public void initialize() {
        // Инициализация спиннеров с диапазоном и возможность ручного ввода
        editquantityid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));
        editcostid.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));
        editspinerMinQuantity.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000, 0));

        editaddBtn.setOnAction(event -> saveChanges());
        editbackBtn.setOnAction(event -> closeWindow());
    }

    public void setMaterial(com.example.javafx4.Material material) {
        this.materialId = material.getId();
        edittypeid.setText(material.getType());
        editnameid.setText(material.getName());
        editquantityid.getValueFactory().setValue(material.getQuantity());
        editcostid.getValueFactory().setValue(material.getCost());
        editspinerMinQuantity.getValueFactory().setValue(material.getMinimalQuantity());
    }

    private void saveChanges() {
        String type = edittypeid.getText();
        String name = editnameid.getText();
        Integer quantity = editquantityid.getValue();
        Integer cost = editcostid.getValue();
        Integer minQuantity = editspinerMinQuantity.getValue();

        if (type.isEmpty() || name.isEmpty() || quantity == null || cost == null || minQuantity == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пожалуйста, заполните все поля.");
            return;
        }

        String sql = "UPDATE materials SET type=?, name=?, quantity=?, cost=?, minimalquantity=? WHERE id=?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setString(2, name);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, cost);
            pstmt.setInt(5, minQuantity);
            pstmt.setInt(6, materialId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Успех", "Материал обновлен успешно.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Не удалось обновить материал.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при обновлении: " + e.getMessage());
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) editbackBtn.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}