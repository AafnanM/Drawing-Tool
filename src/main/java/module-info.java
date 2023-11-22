module com.drawingtool {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.desktop;

    opens com.drawingtool to javafx.fxml;
    exports com.drawingtool;
}
