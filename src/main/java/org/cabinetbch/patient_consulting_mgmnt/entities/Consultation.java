package org.cabinetbch.patient_consulting_mgmnt.entities;

import java.sql.Date;

public class Consultation {
    private Long id;
    private String description;
    private Date date;
    private Patient patient;

    public Consultation() {
    }

    public Consultation(Long id, String description, Date date, Patient patient) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.patient = patient;
    }

    public Consultation(String description, Date date, Patient patient) {
        this.description = description;
        this.date = date;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", patient=" + patient +
                '}';
    }
}
