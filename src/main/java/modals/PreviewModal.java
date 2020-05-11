package modals;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import screenshots.Screenshot;

public class PreviewModal {
    private static final Stage stage = new Stage();
    private static final int CONTENT_WIDTH = 750;
    private static final int CONTENT_HEIGHT = 800;

    public static void showPreview(Screenshot screenshot) {
        if (stage.isShowing()) {
            stage.hide();
        }
        ImageView preview = new ImageView(screenshot);
        preview.setPreserveRatio(true);
        preview.fitWidthProperty().bind(stage.widthProperty());
        BorderPane root = new BorderPane();
        root.setCenter(preview);
        Scene scene;
        if (screenshot.getWidth() <= CONTENT_WIDTH || screenshot.getHeight() <= CONTENT_HEIGHT) {
            scene = new Scene(root, screenshot.getWidth(), screenshot.getHeight());
        } else {
            scene = new Scene(root, screenshot.getWidth() / 2, screenshot.getHeight() / 2);
        }
        stage.setScene(scene);
        stage.show();
    }
}
