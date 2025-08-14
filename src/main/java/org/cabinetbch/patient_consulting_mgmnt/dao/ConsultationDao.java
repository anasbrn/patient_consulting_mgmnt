package org.cabinetbch.patient_consulting_mgmnt.dao;

import org.cabinetbch.patient_consulting_mgmnt.entities.Consultation;

import java.util.List;

public interface ConsultationDao {
    void create(Consultation consultation);
    List<Consultation> findByPatient(Long patientId);
}
