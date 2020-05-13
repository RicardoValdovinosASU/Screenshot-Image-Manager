package eventhandlers;

import groups.GroupImageView;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

// TODO: Finish
public class GroupClickEvent {
    public static void borderPaneClickHandler(BorderPane borderPane) {
       borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
           handleSinglePrimaryClick(mouseEvent, borderPane);
           handlerDoublePrimaryClick(mouseEvent, borderPane);
           handleSecondaryClick(mouseEvent, borderPane);
       });
    }

    public static void handleSinglePrimaryClick(MouseEvent mouseEvent, BorderPane borderPane) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            System.out.println("group clicked!" + ((GroupImageView) borderPane.getCenter()).getName());
        }
    }

    public static void handlerDoublePrimaryClick(MouseEvent mouseEvent, BorderPane borderPane) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            System.out.println("group clicked!" + ((GroupImageView) borderPane.getCenter()).getName());
        }
    }

    private static void handleSecondaryClick(MouseEvent mouseEvent, BorderPane borderPane) {
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            System.out.println("group clicked!" + ((GroupImageView) borderPane.getCenter()).getName());
        }
    }
}
