package org.cabinetbch.patient_consulting_mgmnt.dao;

import org.cabinetbch.patient_consulting_mgmnt.entities.Consultation;
import org.cabinetbch.patient_consulting_mgmnt.entities.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDaoImpl implements ConsultationDao {
    @Override
    public void create(Consultation consultation) {
        String sql = "INSERT INTO consultations (patient_id, description, date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, consultation.getPatient().getId());
            stmt.setString(2, consultation.getDescription());
            stmt.setDate(3, Date.valueOf(consultation.getDate().toLocalDate()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Consultation> findByPatient(Long patientId) {
        List<Consultation> consultations = new ArrayList<>();
        String sqlConsult = "SELECT * FROM consultations WHERE patient_id = ?";
        String sqlPatient = "SELECT * FROM patients WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            // First, get the patient
            Patient patient = null;
            try (PreparedStatement stmtPatient = conn.prepareStatement(sqlPatient)) {
                stmtPatient.setLong(1, patientId);
                ResultSet rsPatient = stmtPatient.executeQuery();
                if (rsPatient.next()) {
                    patient = new Patient(
                            rsPatient.getLong("id"),
                            rsPatient.getString("firstName"),
                            rsPatient.getString("lastName"),
                            rsPatient.getString("phone")
                    );
                }
            }

            // Then, get consultations for that patient
            try (PreparedStatement stmtConsult = conn.prepareStatement(sqlConsult)) {
                stmtConsult.setLong(1, patientId);
                ResultSet rsConsult = stmtConsult.executeQuery();
                while (rsConsult.next()) {
                    consultations.add(new Consultation(
                            rsConsult.getLong("id"),
                            rsConsult.getString("description"),
                            rsConsult.getDate("date"),
                            patient // attach the patient object
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return consultations;
    }
}
