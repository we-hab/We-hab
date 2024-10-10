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
    requires java.xml.crypto;
    requires java.xml;

    exports edu.qut.cab302.wehab.models.workout;
    opens edu.qut.cab302.wehab.models.workout to javafx.fxml;

    exports edu.qut.cab302.wehab.models.pdf_report;
    opens edu.qut.cab302.wehab.models.pdf_report to javafx.fxml;

    exports edu.qut.cab302.wehab.models.mood_ratings;
    opens edu.qut.cab302.wehab.models.mood_ratings to javafx.fxml;

    exports edu.qut.cab302.wehab.database;
    opens edu.qut.cab302.wehab.database to javafx.fxml;

    opens edu.qut.cab302.wehab.models.user_account to javafx.fxml;
    exports edu.qut.cab302.wehab.models.user_account;

    exports edu.qut.cab302.wehab.controllers.reminders;
    opens edu.qut.cab302.wehab.controllers.reminders to javafx.fxml;

    exports edu.qut.cab302.wehab.controllers.settings;
    opens edu.qut.cab302.wehab.controllers.settings to javafx.fxml;

    exports edu.qut.cab302.wehab.controllers.dashboard;
    opens edu.qut.cab302.wehab.controllers.dashboard to javafx.fxml;

    exports edu.qut.cab302.wehab.controllers.medication;
    opens edu.qut.cab302.wehab.controllers.medication to javafx.fxml;

    exports edu.qut.cab302.wehab.controllers.workout;
    opens edu.qut.cab302.wehab.controllers.workout to javafx.fxml;

    exports edu.qut.cab302.wehab.models.medication;
    opens edu.qut.cab302.wehab.models.medication to javafx.fxml;

    exports edu.qut.cab302.wehab.models.dao;
    opens edu.qut.cab302.wehab.models.dao to javafx.fxml;

    exports edu.qut.cab302.wehab.main;
    opens edu.qut.cab302.wehab.main to javafx.fxml;

    exports edu.qut.cab302.wehab.util;
    opens edu.qut.cab302.wehab.util to javafx.fxml;

    exports edu.qut.cab302.wehab.controllers.main;
    opens edu.qut.cab302.wehab.controllers.main to javafx.fxml;
}
