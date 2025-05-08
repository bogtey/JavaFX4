package com.example.javafx4;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloController {
    @FXML
    private ListView<Material> listView;

    @FXML
    private Button toAddid;
    @FXML
    private Button updateid;
    @FXML
    private Button dellbtn;

    private static final String URL = "jdbc:postgresql://localhost:5432/JavaFX4";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";


    @FXML
    public void initialize() {
        loadMaterials();
        toAddid.setOnAction(event -> openAddMaterialWindow());
        updateid.setOnAction(event -> updateMaterials());

        dellbtn.setOnAction(event -> deleteSelectedMaterial());

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Material selectedMaterial = listView.getSelectionModel().getSelectedItem();
                if (selectedMaterial != null) {
                    openEditMaterialWindow(selectedMaterial);
                }
            }
        });
    }

    private void loadMaterials() {
        try {
            Class.forName("org.postgresql.Driver");

            try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM materials")) {

                while (rs.next()) {
                    Material material = new Material(
                            rs.getInt("id"),
                            rs.getString("type"),
                            rs.getString("name"),
                            rs.getInt("minimalquantity"),
                            rs.getInt("quantity"),
                            rs.getInt("cost")
                    );

                    listView.getItems().add(material);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер jdbc не найден");
        }
    }

    private void updateMaterials() {
        listView.getItems().clear();
        loadMaterials();
    }

    private void openAddMaterialWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx4/addmaterial.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Добавить материал");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEditMaterialWindow(Material material) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx4/editmaterial.fxml"));
            Parent root = loader.load();

            // Получаем контроллер редактирования и передаём выбранный материал
            EditMaterialController editController = loader.getController();
            editController.setMaterial(material);

            Stage stage = new Stage();
            stage.setTitle("Изменить информацию");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelectedMaterial() {
        Material selectedMaterial = listView.getSelectionModel().getSelectedItem();
        if (selectedMaterial != null) {

            deleteMaterialDatabase(selectedMaterial.getId());

            listView.getItems().remove(selectedMaterial);
        } else {
            System.out.println("Выберите материал для удаления");
        }
    }

    private void deleteMaterialDatabase(int materialId) {
        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
            Statement stmt = connection.createStatement()) {
                String sql = "DELETE FROM materials WHERE id = " + materialId;
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}