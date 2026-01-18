package com.esprit.studentcrud.controller;

import com.esprit.studentcrud.dao.StudentDAO;
import com.esprit.studentcrud.dao.StudentDAOImpl;
import com.esprit.studentcrud.model.Student;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.IntegerStringConverter;

public class StudentController {

    // ===== TABLE =====
    @FXML private TableView<Student> tableStudents;
    @FXML private TableColumn<Student, Number> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, Number> colAge;
    @FXML private TableColumn<Student, String> colEmail;

    // ===== FORM =====
    @FXML private TextField txtName;
    @FXML private TextField txtAge;
    @FXML private TextField txtEmail;

    // ===== DAO =====
    private final StudentDAO dao = new StudentDAOImpl();
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        // ✅ No reflection: always works even if FXML columns become "raw"
        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colAge.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));

        loadStudents();

        tableStudents.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, selected) -> {
                    if (selected != null) {
                        txtName.setText(selected.getName());
                        txtAge.setText(String.valueOf(selected.getAge()));
                        txtEmail.setText(selected.getEmail());
                    }
                });

        // Optional: prevent non-numbers in age field
        txtAge.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), null, change ->
                change.getControlNewText().matches("\\d*") ? change : null
        ));
    }

    private void loadStudents() {
        students.setAll(dao.findAll());
        tableStudents.setItems(students);
    }

    @FXML
    private void onAdd() {

        String name = txtName.getText().trim();
        String ageText = txtAge.getText().trim();
        String email = txtEmail.getText().trim();

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            alert("All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            alert("Invalid email format.\nExample: name@mail.com");
            txtEmail.requestFocus();
            return;
        }

        Student s = new Student(
                name,
                Integer.parseInt(ageText),
                email
        );

        dao.insert(s);
        loadStudents();
        onClear();
    }


    @FXML
    private void onUpdate() {
        Student selected = tableStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Select a student first.");
            return;
        }

        selected.setName(txtName.getText().trim());
        selected.setAge(Integer.parseInt(txtAge.getText().trim()));
        selected.setEmail(txtEmail.getText().trim());

        dao.update(selected);
        loadStudents();
        onClear();
    }

    @FXML
    private void onDelete() {
        Student selected = tableStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alert("Select a student first.");
            return;
        }

        dao.delete(selected.getId());
        loadStudents();
        onClear();
    }

    @FXML
    private void onRefresh() {
        loadStudents();
    }

    @FXML
    private void onClear() {
        txtName.clear();
        txtAge.clear();
        txtEmail.clear();
        tableStudents.getSelectionModel().clearSelection();
    }

    private void alert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Info");
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
    //Mail Verif
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

}
