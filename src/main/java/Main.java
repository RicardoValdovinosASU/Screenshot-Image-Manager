import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("views/MainContentView.fxml"));
        Pane leftSidePane = FXMLLoader.load(getClass().getResource("views/LeftSidePaneView.fxml"));
        Pane rightSidePane = FXMLLoader.load(getClass().getResource("views/RightSidePaneView.fxml"));
        root.getChildren().addAll(leftSidePane, rightSidePane);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
