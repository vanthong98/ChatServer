package hw.app.chatserver;

import hw.app.core.socket.ServerSocketHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import hw.app.core.main.Mediator;
import javafx.scene.input.MouseEvent;

public class ChatServerController {
    public TextField portTextBox;
    public Button startServerButton;
    public ListView<String> clientListView;
    public TextArea messageTextArea;
    public TextArea userMessageTextArea;
    public TextArea systemConsoleTextArea;
    public TextField selectedClientTextbox;
    public Button disconnectButton;
    public Button sendMessageButton;

    @FXML

    public void onStartServerButtonClick(ActionEvent actionEvent) {

        Mediator.systemConsoleTextArea = systemConsoleTextArea;
        Mediator.log("Starting server...");

        ObservableList<String> clientNames = FXCollections.observableArrayList();

        clientListView.setItems(clientNames);

        Mediator.connectedClientNames = clientNames;
        Mediator.messageTextArea = messageTextArea;

        var port = Integer.parseInt(portTextBox.getText());

        var serverSocketHandler = new ServerSocketHandler(port);
        serverSocketHandler.start();

        portTextBox.setDisable(true);
        startServerButton.setDisable(true);
        userMessageTextArea.setDisable(false);
        userMessageTextArea.setEditable(true);
    }

    public void onDisconnectButtonClick(ActionEvent actionEvent) {
        var clientName = selectedClientTextbox.getText();

        if (clientName.isEmpty()) {
            return;
        }

        Mediator.forceDisconnect(clientName);
        selectedClientTextbox.clear();
    }

    public void onSendMessageButtonClick(ActionEvent actionEvent) {
        var message = userMessageTextArea.getText();

        if (message == null || message.isEmpty()){
            return;
        }

        userMessageTextArea.clear();

        Mediator.sendGroupMessage("Server", message);
    }

    public void onClientListViewClick(MouseEvent mouseEvent) {
        var selectedClientName = clientListView.getSelectionModel().getSelectedItem();
        selectedClientTextbox.setEditable(true);
        selectedClientTextbox.setText(selectedClientName);
        selectedClientTextbox.setDisable(false);
    }
}