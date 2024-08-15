module edu.qut.cab302.wehab {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.qut.cab302.wehab to javafx.fxml;
    exports edu.qut.cab302.wehab;
}