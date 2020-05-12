package utils;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Utils {
    private Utils() {

    }

    public static void fadeTransition(Node node, int duration, double from, double to) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), node);
        fadeTransition.setFromValue(from);
        fadeTransition.setToValue(to);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }

    public static void translateTransition(Node node, int duration, double from, double to) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(duration), node);
        translateTransition.setFromX(from);
        translateTransition.setToX(to);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }
}
