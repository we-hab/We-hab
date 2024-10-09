module edu.qut.cab302.wehab {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires bcrypt;
    requires java.desktop;
    requires org.json;
    requires kernel;
    requires layout;
    requires jdk.compiler;


    opens edu.qut.cab302.wehab to javafx.fxml;
    exports edu.qut.cab302.wehab;
    exports edu.qut.cab302.wehab.medication;
    opens edu.qut.cab302.wehab.medication to javafx.fxml;
    exports edu.qut.cab302.wehab.workout;
    opens edu.qut.cab302.wehab.workout to javafx.fxml;
    exports edu.qut.cab302.wehab.pdf_report;
    opens edu.qut.cab302.wehab.pdf_report to javafx.fxml;
    exports edu.qut.cab302.wehab.mood_ratings;
    opens edu.qut.cab302.wehab.mood_ratings to javafx.fxml;
    exports edu.qut.cab302.wehab.database;
    opens edu.qut.cab302.wehab.database to javafx.fxml;
    opens edu.qut.cab302.wehab.user_account to javafx.fxml;
    exports edu.qut.cab302.wehab.user_account;
    exports edu.qut.cab302.wehab.reminders;
    opens edu.qut.cab302.wehab.reminders to javafx.fxml;
    exports edu.qut.cab302.wehab.settings;
    opens edu.qut.cab302.wehab.settings to javafx.fxml;
    exports edu.qut.cab302.wehab.dashboard;
    opens edu.qut.cab302.wehab.dashboard to javafx.fxml;
}