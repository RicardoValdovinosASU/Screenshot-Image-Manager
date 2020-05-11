package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import screenshots.Screenshot;
import screenshots.ScreenshotUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainContentController implements Initializable {
    private static final int FLOW_PANE_COLUMN_SIZE = 10;
    private static final int FLOW_PANE_ROW_SIZE = 5;
    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 100;
    private static final double SCROLL_SPEED = 0.0005;
    public FlowPane mainContentFlowPane;
    public ScrollPane mainContentScrollPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // show images
        ArrayList<Screenshot> content = getContent();
        populateContentArea(content);

        // hide scroll bars
        mainContentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainContentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // set height to number of rows
        mainContentFlowPane.setPrefHeight(IMAGE_HEIGHT * (content.size() / (double) FLOW_PANE_COLUMN_SIZE));

        // scroll speed
        mainContentScrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SCROLL_SPEED;
            mainContentScrollPane.setVvalue(mainContentScrollPane.getVvalue() - deltaY);
        });
    }

    private ArrayList<Screenshot> getContent() {
        ArrayList<Screenshot> screenshots = null;
        try {
            screenshots = ScreenshotUtil.getScreenshots("/home/ricky/Pictures/Screenshots");
        } catch (NotDirectoryException notDirectoryException) {
            // show a pop up error here or something
        }
        return screenshots;
    }

    private void populateContentArea(ArrayList<Screenshot> content) {
        if (content != null) {
            for (Screenshot screenshot : content) {
                File imageFile = new File(screenshot.getUrl());
                Image image = new Image(imageFile.toURI().toString());
                ImageView imageView = setImageView(image);
                BorderPane borderPane = setImageViewBackground(imageView);
                borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseClicked) -> {
                    System.out.println("Clicked: " + screenshot.getName());
                });
                mainContentFlowPane.getChildren().add(borderPane);
            }
        } else {
            // show a pop up error here or something
        }
    }

    private ImageView setImageView(Image image) {
        ImageView imageView = new ImageView();
        imageView.setImage(ScreenshotUtil.scale(image, IMAGE_WIDTH, IMAGE_HEIGHT, true));
        return imageView;
    }

    private BorderPane setImageViewBackground(ImageView imageView) {
        BorderPane borderPane = new BorderPane();
        borderPane.backgroundProperty().setValue(new Background(new BackgroundFill(Paint.valueOf("000000"), CornerRadii.EMPTY, null)));
        borderPane.setPrefWidth(IMAGE_WIDTH);
        borderPane.setPrefHeight(IMAGE_HEIGHT);
        borderPane.setCenter(imageView);
        return borderPane;
    }

}
