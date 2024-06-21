package hw.app.chatserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatServerApplication.class.getResource("application.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chat Server Application");

        stage.setResizable(false);

        stage.iconifiedProperty().addListener((_, _, _) -> {
            stage.setIconified(false);
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}