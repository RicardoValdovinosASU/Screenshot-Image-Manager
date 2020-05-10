package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import screenshots.Screenshot;
import screenshots.ScreenshotUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainContentController implements Initializable {
    public FlowPane mainContentFlowPane;
    public ScrollPane mainContentScrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateContentArea();
    }

    private void populateContentArea() {
        ArrayList<Screenshot> screenshots = new ArrayList<>();
        try {
            screenshots = ScreenshotUtil.getScreenshots("/home/ricky/Pictures/Screenshots");
        } catch (NotDirectoryException notDirectoryException) {
            // show a pop up error here or something
        }
        for (Screenshot screenshot : screenshots) {
            File imageFile = new File(screenshot.getUrl());
            Image image = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setPreserveRatio(true);
            mainContentFlowPane.getChildren().add(imageView);
        }
    }
}
