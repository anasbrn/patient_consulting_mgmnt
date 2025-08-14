package org.cabinetbch.patient_consulting_mgmnt;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.cabinetbch.patient_consulting_mgmnt.dao.PatientDao;
import org.cabinetbch.patient_consulting_mgmnt.dao.PatientDaoImpl;
import org.cabinetbch.patient_consulting_mgmnt.dao.ConsultationDao;
import org.cabinetbch.patient_consulting_mgmnt.dao.ConsultationDaoImpl;
import org.cabinetbch.patient_consulting_mgmnt.entities.Patient;
import org.cabinetbch.patient_consulting_mgmnt.entities.Consultation;

import java.sql.Date;

public class Main extends Application {

    private final PatientDao patientDao = new PatientDaoImpl();
    private final ConsultationDao consultationDao = new ConsultationDaoImpl();

    private final TableView<Patient> patientTable = new TableView<>();
    private final TableView<Consultation> consultationTable = new TableView<>();

    private final ObservableList<Patient> patientList = FXCollections.observableArrayList();
    private final ObservableList<Consultation> consultationList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {

        // === Patient Table ===
        TableColumn<Patient, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));

        TableColumn<Patient, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));

        TableColumn<Patient, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));

        patientTable.getColumns().addAll(firstNameCol, lastNameCol, phoneCol);
        patientTable.setItems(patientList);

        // === Consultation Table ===
        TableColumn<Consultation, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));

        TableColumn<Consultation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));

        consultationTable.getColumns().addAll(descCol, dateCol);
        consultationTable.setItems(consultationList);

        // Load initial data
        refreshPatients();

        // When patient selected → show consultations
        patientTable.getSelectionModel().selectedItemProperty().addListener((obs, old, selected) -> {
            if (selected != null) {
                refreshConsultations(selected);
            } else {
                consultationList.clear();
            }
        });

        // === Form fields for patient ===
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        Button addPatientBtn = new Button("Add Patient");
        addPatientBtn.setOnAction(e -> {
            if (!firstNameField.getText().isEmpty()) {
                patientDao.create(new Patient(firstNameField.getText(), lastNameField.getText(), phoneField.getText()));
                refreshPatients();
                firstNameField.clear();
                lastNameField.clear();
                phoneField.clear();
            }
        });

        Button editPatientBtn = new Button("Edit Patient");
        editPatientBtn.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selected.setFirstName(firstNameField.getText());
                selected.setLastName(lastNameField.getText());
                selected.setPhone(phoneField.getText());
                patientDao.update(selected);
                refreshPatients();
            }
        });

        Button deletePatientBtn = new Button("Delete Patient");
        deletePatientBtn.setOnAction(e -> {
            Patient selected = patientTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                patientDao.delete(selected.getId());
                refreshPatients();
                consultationList.clear();
            }
        });

        // === Form fields for consultation ===
        TextField consultationDescField = new TextField();
        consultationDescField.setPromptText("Consultation Description");

        DatePicker consultationDatePicker = new DatePicker();

        Button addConsultationBtn = new Button("Add Consultation");
        addConsultationBtn.setOnAction(e -> {
            Patient selectedPatient = patientTable.getSelectionModel().getSelectedItem();
            if (selectedPatient != null && !consultationDescField.getText().isEmpty() && consultationDatePicker.getValue() != null) {
                consultationDao.create(new Consultation(
                        consultationDescField.getText(),
                        Date.valueOf(consultationDatePicker.getValue()),
                        selectedPatient
                ));
                refreshConsultations(selectedPatient);
                consultationDescField.clear();
                consultationDatePicker.setValue(null);
            }
        });

        HBox patientForm = new HBox(10, firstNameField, lastNameField, phoneField, addPatientBtn, editPatientBtn, deletePatientBtn);
        HBox consultForm = new HBox(10, consultationDescField, consultationDatePicker, addConsultationBtn);

        VBox root = new VBox(10,
                new Label("Patients"), patientTable,
                patientForm,
                new Label("Consultations for Selected Patient"), consultationTable,
                consultForm
        );

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Patient & Consultation Management");
        primaryStage.show();
    }

    private void refreshPatients() {
        patientList.setAll(patientDao.getAll());
    }

    private void refreshConsultations(Patient patient) {
        consultationList.setAll(consultationDao.findByPatient(patient.getId()));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
