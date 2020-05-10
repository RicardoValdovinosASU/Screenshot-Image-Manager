package controllers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class LeftSideBarViewController {
    public Pane leftSidePane;
    public Rectangle leftSideButton;
    private boolean isLeftHidden = false;
    private static final int TRANSITION_SPEED = 100;
    private static final int BUTTON_OFFSET = 20;

    public void onLeftSideButtonClicked(MouseEvent mouseEvent) {
        double leftSidePaneHiddenPosition = (leftSidePane.getLayoutX() - leftSidePane.getWidth()) + (leftSidePane.getWidth() * 0.25);
        double leftSideButtonHiddenPosition = leftSidePaneHiddenPosition - BUTTON_OFFSET;
        if (!isLeftHidden) {
            translateTransition(leftSidePane, TRANSITION_SPEED, 0, leftSidePaneHiddenPosition);
            translateTransition(leftSideButton, TRANSITION_SPEED, 0, leftSideButtonHiddenPosition);
            fadeTransition(leftSidePane, TRANSITION_SPEED, 1.0, 0.0);
            isLeftHidden = true;
        } else {
            translateTransition(leftSidePane, TRANSITION_SPEED, leftSidePaneHiddenPosition, 0);
            translateTransition(leftSideButton, TRANSITION_SPEED, leftSideButtonHiddenPosition, 0);
            fadeTransition(leftSidePane, TRANSITION_SPEED, 0.0, 1.0);
            isLeftHidden = false;
        }
    }

    public void onLeftSideButtonEntered(MouseEvent mouseEvent) {
        leftSideButton.fillProperty().setValue(Paint.valueOf("e0e0e0"));
    }

    public void onLeftSideButtonExited(MouseEvent mouseEvent) {
        leftSideButton.fillProperty().setValue(Paint.valueOf("c6c6c6"));
    }

    private void fadeTransition(Node node, int duration, double from, double to) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    private void translateTransition(Node node, int duration, double from, double to) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromX(from);
        translateTransition.setToX(to);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }
}
