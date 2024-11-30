package com.examen.finalp3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

import java.io.*;

public class HelloController {

    @FXML
    private TableView<Activo> inventoryTable;
    @FXML
    private TableColumn<Activo, Integer> idColumn;
    @FXML
    private TableColumn<Activo, String> typeColumn;
    @FXML
    private TableColumn<Activo, String> brandColumn;
    @FXML
    private TableColumn<Activo, String> modelColumn;
    @FXML
    private TableColumn<Activo, String> serialColumn;
    @FXML
    private TableColumn<Activo, String> responsableColumn;

    private ObservableList<Activo> inventoryData = FXCollections.observableArrayList();
    private ObservableList<Activo> unsavedData = FXCollections.observableArrayList();

    private static final String FILE_PATH = "src/main/resources/inventory.csv";
    private int idCounter = 1;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().marcaProperty());
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modeloProperty());
        serialColumn.setCellValueFactory(cellData -> cellData.getValue().serialProperty());
        responsableColumn.setCellValueFactory(cellData -> cellData.getValue().responsableProperty());

        inventoryTable.setEditable(true);
        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Desktop", "Laptop", "Impresora", "Router", "Scanner", "Firewall", "Switch"));
        brandColumn.setCellFactory(ComboBoxTableCell.forTableColumn("Dell", "HP", "Lenovo", "Cisco", "Epson", "tplink"));
        modelColumn.setCellFactory(ComboBoxTableCell.forTableColumn("XPS", "EliteBook", "ThinkPad", "Catalyst", "Optiplex"));
        serialColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        responsableColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        loadInventoryFromFile();
    }

    @FXML
    private void onAddActivo() {
        Activo newActivo = new Activo(idCounter++, "Laptop", "Dell", "Modelo", "Serial", "Responsable");
        unsavedData.add(newActivo);
        inventoryTable.setItems(unsavedData);
    }

    @FXML
    private void onDeleteSelected() {
        Activo selectedActivo = inventoryTable.getSelectionModel().getSelectedItem();
        if (selectedActivo != null) {
            unsavedData.remove(selectedActivo);
            inventoryTable.setItems(unsavedData);
            showAlert("Activo Eliminado", "El activo seleccionado ha sido eliminado.");
        } else {
            showAlert("No hay selección", "Seleccione un activo para eliminar.");
        }
    }

    @FXML
    private void onShowFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Archivo de Inventario");
        File file = fileChooser.showOpenDialog(inventoryTable.getScene().getWindow());
        if (file != null) {
            inventoryData.clear();
            loadInventoryFromFile(file);
            inventoryTable.setItems(inventoryData);
        }
    }

    @FXML
    private void onSave() {
        if (!unsavedData.isEmpty()) {
            saveInventoryToFile();
            inventoryData.addAll(unsavedData);
            unsavedData.clear();
            inventoryTable.setItems(inventoryData);
            showAlert("Datos Guardados", "El inventario ha sido guardado correctamente.");
        } else {
            showAlert("Nada que guardar", "No hay datos nuevos que guardar.");
        }
    }

    private void loadInventoryFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data.length == 6) {
                        Activo activo = new Activo(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], data[5]);
                        inventoryData.add(activo);
                    }
                }
                idCounter = inventoryData.size() > 0 ? inventoryData.get(inventoryData.size() - 1).getId() + 1 : 1;
                inventoryTable.setItems(inventoryData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadInventoryFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 6) {
                    Activo activo = new Activo(Integer.parseInt(data[0]), data[1], data[2], data[3], data[4], data[5]);
                    inventoryData.add(activo);
                }
            }
            idCounter = inventoryData.size() > 0 ? inventoryData.get(inventoryData.size() - 1).getId() + 1 : 1;
            inventoryTable.setItems(inventoryData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveInventoryToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Activo activo : inventoryData) {
                writer.write(activo.toCSV() + "\n");
            }
            for (Activo activo : unsavedData) {
                writer.write(activo.toCSV() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
