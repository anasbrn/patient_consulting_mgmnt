package org.cabinetbch.patient_consulting_mgmnt.dao;

import org.cabinetbch.patient_consulting_mgmnt.entities.Patient;

import java.util.List;

public interface PatientDao {
    void create(Patient patient);
    List<Patient> getAll();
    Patient findById(Long id);
    void update(Patient patient);
    void delete(Long id);
}
