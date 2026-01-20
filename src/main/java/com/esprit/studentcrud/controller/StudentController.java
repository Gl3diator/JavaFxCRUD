package com.esprit.studentcrud.controller;

import com.esprit.studentcrud.dao.StudentDAO;
import com.esprit.studentcrud.dao.StudentDAOImpl;
import com.esprit.studentcrud.model.Student;
import com.esprit.studentcrud.util.EmailJsService;
import com.esprit.studentcrud.util.WhatsAppService;
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

    // ===== BUTTONS =====
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    // ===== STATUS =====
    @FXML private Label lblStatus;

    // ===== FORM =====
    @FXML private TextField txtName;
    @FXML private TextField txtAge;
    @FXML private TextField txtEmail;

    // ===== DAO =====
    private final StudentDAO dao = new StudentDAOImpl();
    private final ObservableList<Student> students = FXCollections.observableArrayList();

    // ===== SERVICES =====
    private final WhatsAppService wa = new WhatsAppService();
    private final EmailJsService mailer = new EmailJsService();

    @FXML
    private void initialize() {

        colId.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()));
        colName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        colAge.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getAge()));
        colEmail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));

        loadStudents();

        // initial state
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        lblStatus.setVisible(false);
        lblStatus.setText("");

        // selection listener: enable/disable + fill form
        tableStudents.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, selected) -> {
                    boolean hasSelection = selected != null;
                    btnUpdate.setDisable(!hasSelection);
                    btnDelete.setDisable(!hasSelection);

                    if (hasSelection) {
                        txtName.setText(selected.getName());
                        txtAge.setText(String.valueOf(selected.getAge()));
                        txtEmail.setText(selected.getEmail());
                    }
                });

        // only numbers in age field
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
        String ageStr = txtAge.getText().trim();
        String email = txtEmail.getText().trim().toLowerCase();

        if (name.isEmpty() || ageStr.isEmpty() || email.isEmpty()) {
            showError("Missing fields", "Please fill Name, Age and Email.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            showError("Invalid age", "Age must be a number.");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Invalid Email", "Please enter a valid email like: name@example.com");
            return;
        }

        if (dao.existsByEmail(email)) {
            showError("Duplicate Email", "This email already exists. Choose another one.");
            return;
        }

        Student s = new Student(0, name, age, email);

        try {
            dao.insert(s);

            loadStudents();
            onClear(); // clears + disables + hides old status

            wa.sendStudentAdded(name, age, email);
            mailer.sendStudentAdded(name, age, email);

            statusOk("Student added ✅ Email + WhatsApp sent");

        } catch (RuntimeException ex) {
            if ("DUPLICATE_EMAIL".equals(ex.getMessage())) {
                showError("Duplicate Email", "This email already exists. Choose another one.");
                return;
            }
            showError("Error", ex.getMessage());
        }
    }

    @FXML
    private void onUpdate() {
        Student selected = tableStudents.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String name = txtName.getText().trim();
        String ageStr = txtAge.getText().trim();
        String email = txtEmail.getText().trim().toLowerCase();

        if (name.isEmpty() || ageStr.isEmpty() || email.isEmpty()) {
            showError("Missing fields", "Please fill Name, Age and Email.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            showError("Invalid age", "Age must be a number.");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Invalid Email", "Please enter a valid email like: name@example.com");
            return;
        }

        if (dao.existsByEmailExceptId(email, selected.getId())) {
            showError("Duplicate Email", "This email is already used by another student.");
            return;
        }

        selected.setName(name);
        selected.setAge(age);
        selected.setEmail(email);

        try {
            dao.update(selected);
        } catch (RuntimeException ex) {
            if ("DUPLICATE_EMAIL".equals(ex.getMessage())) {
                showError("Duplicate Email", "This email is already used by another student.");
                return;
            }
            showError("Error", ex.getMessage());
            return;
        }

        loadStudents();
        onClear();
        statusOk("Student updated ✅");
    }

    @FXML
    private void onDelete() {
        Student selected = tableStudents.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No selection", "Select a student first.");
            return;
        }

        try {
            dao.delete(selected.getId());
        } catch (RuntimeException ex) {
            showError("Error", ex.getMessage());
            return;
        }

        loadStudents();
        onClear();
        statusOk("Student deleted ✅");
    }

    @FXML
    private void onRefresh() {
        loadStudents();
        statusOk("Refreshed ✅");
    }

    @FXML
    private void onClear() {
        txtName.clear();
        txtAge.clear();
        txtEmail.clear();
        tableStudents.getSelectionModel().clearSelection();

        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        lblStatus.setVisible(false);
        lblStatus.setText("");
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.trim();
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();

        statusWarn(msg);
    }

    private void statusOk(String msg) {
        lblStatus.setText(msg);
        lblStatus.setVisible(true);
        lblStatus.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
    }

    private void statusWarn(String msg) {
        lblStatus.setText(msg);
        lblStatus.setVisible(true);
        lblStatus.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
    }
}
