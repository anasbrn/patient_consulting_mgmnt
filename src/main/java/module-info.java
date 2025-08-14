module org.cabinetbch.patient_consulting_mgmnt {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.cabinetbch.patient_consulting_mgmnt to javafx.fxml;
    exports org.cabinetbch.patient_consulting_mgmnt;
}