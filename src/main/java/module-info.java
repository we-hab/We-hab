module edu.qut.cab302.wehab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires bcrypt;
    requires java.desktop;
    requires org.json;
    requires kernel;
    requires layout;


    opens edu.qut.cab302.wehab to javafx.fxml;
    exports edu.qut.cab302.wehab;
    exports edu.qut.cab302.wehab.medication;
    opens edu.qut.cab302.wehab.medication to javafx.fxml;
}