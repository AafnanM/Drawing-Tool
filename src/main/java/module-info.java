module com.drawingtool {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.drawingtool to javafx.fxml;
    exports com.drawingtool;
}
