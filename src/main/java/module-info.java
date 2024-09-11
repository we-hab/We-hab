module edu.qut.cab302.wehab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires bcrypt;
    requires java.desktop;


    opens edu.qut.cab302.wehab to javafx.fxml;
    exports edu.qut.cab302.wehab;
}