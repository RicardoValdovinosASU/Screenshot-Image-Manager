package controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import screenshots.Screenshot;
import screenshots.ScreenshotImageView;
import utils.Utils;

public class RightSidePaneController extends Controller {
    public Pane rightSidePane;
    public TextField screenshotNameTextField;
    public Label locationLabel;
    public Label dateCreatedLabel;
    public Label lastModifiedLabel;
    public Label screenshotLocationLabel;
    public Label screenshotCreatedLabel;
    public Label screenshotLastModifiedLabel;
    public Rectangle rightSideButton;
    private boolean isRightSideBarHidden = false;

    void displayInfo(ScreenshotImageView screenshotImageView) {
        Screenshot screenshot = screenshotImageView.getScreenshot();
        final int sidePanelSpace = 125;
        screenshotNameTextField.setText(screenshot.getName());
        screenshotLocationLabel.setText(screenshot.getLocation().replace("file:", ""));
        screenshotLocationLabel.setWrapText(true);
        screenshotLocationLabel.setMaxWidth(sidePanelSpace);
        screenshotCreatedLabel.setText(screenshot.getDateCreated());
        screenshotCreatedLabel.setWrapText(true);
        screenshotCreatedLabel.setMaxWidth(sidePanelSpace);
        screenshotLastModifiedLabel.setText(screenshot.getLastModified());
        screenshotLastModifiedLabel.setWrapText(true);
        screenshotLastModifiedLabel.setMaxWidth(sidePanelSpace);
        readjustLabels();
    }

    private void readjustLabels() {
        dateCreatedLabel.setLayoutY(screenshotLocationLabel.getLayoutY() + screenshotLocationLabel.getHeight() + 20);
        screenshotCreatedLabel.setLayoutY(screenshotLocationLabel.getLayoutY() + screenshotLocationLabel.getHeight() + 20);
        lastModifiedLabel.setLayoutY(screenshotCreatedLabel.getLayoutY() + screenshotCreatedLabel.getHeight() + 20);
        screenshotLastModifiedLabel.setLayoutY(screenshotCreatedLabel.getLayoutY() + screenshotCreatedLabel.getHeight() + 20);
        System.out.println(screenshotLocationLabel.getHeight());
    }

    public void onRightSideButtonClicked(MouseEvent mouseEvent) {
        double rightSidePaneHiddenPosition = rightSidePane.getWidth() - (rightSidePane.getWidth() * 0.25);
        double rightSideButtonHiddenPosition = rightSidePaneHiddenPosition + BUTTON_OFFSET;
        if (!isRightSideBarHidden) {
            Utils.translateTransition(rightSidePane, TRANSITION_SPEED, 0, rightSidePaneHiddenPosition);
            Utils.translateTransition(rightSideButton, TRANSITION_SPEED, 0, rightSideButtonHiddenPosition);
            Utils.fadeTransition(rightSidePane, TRANSITION_SPEED, 1.0, 0);
            isRightSideBarHidden = true;
        } else {
            Utils.translateTransition(rightSidePane, TRANSITION_SPEED, rightSidePaneHiddenPosition, 0);
            Utils.translateTransition(rightSideButton, TRANSITION_SPEED, rightSideButtonHiddenPosition, 0);
            Utils.fadeTransition(rightSidePane, TRANSITION_SPEED, 0, 1.0);
            isRightSideBarHidden = false;
        }
    }

    public void onRightSideButtonEntered(MouseEvent mouseEvent) {
        rightSideButton.fillProperty().setValue(Paint.valueOf("e0e0e0"));
    }

    public void onRightSideButtonExited(MouseEvent mouseEvent) {
        rightSideButton.fillProperty().setValue(Paint.valueOf("c6c6c6"));
    }
}
