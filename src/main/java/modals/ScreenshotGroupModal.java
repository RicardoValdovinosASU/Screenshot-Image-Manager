package modals;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ScreenshotGroupModal {
    public TextField groupNameTextField;
    private String groupName;

    public void showAndWait() {

    }

    public void onEnterButtonClicked(ActionEvent actionEvent) {
        groupName = groupNameTextField.getText();
        Stage stage = (Stage) groupNameTextField.getScene().getWindow();
        stage.close();
    }

    public void onCancelButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) groupNameTextField.getScene().getWindow();
        stage.close();
    }

    public void onGroupNameTextFieldKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            groupName = groupNameTextField.getText();
            Stage stage = (Stage) groupNameTextField.getScene().getWindow();
            stage.close();
        }
    }

    public String getGroupName() {
        return groupName;
    }
}
