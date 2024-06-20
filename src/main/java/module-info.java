module hw.app.chatserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens hw.app.chatserver to javafx.fxml;
    exports hw.app.chatserver;
}