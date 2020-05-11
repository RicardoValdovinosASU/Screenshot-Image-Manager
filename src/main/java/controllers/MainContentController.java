package controllers;

import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import modals.PreviewModal;
import screenshots.Screenshot;
import screenshots.ScreenshotImageView;
import screenshots.ScreenshotUtil;

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
    private ContextMenu contextMenu;
    private ArrayList<ScreenshotImageView> allScreenshots = new ArrayList<>();
    private ArrayList<ScreenshotImageView> selectedScreenshots = new ArrayList<>();
    private int initialShiftClick, secondShiftClick, shiftClickCount = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // show images
        ArrayList<Screenshot> content = getContent();
        populateContentArea(content);

        hideScrollBars();

        // set height to number of rows
        mainContentFlowPane.setPrefHeight(IMAGE_HEIGHT * (content.size() / (double) FLOW_PANE_COLUMN_SIZE));

        setScrollSpeed();

        // initialize context menu
        contextMenu = createContextMenu();
    }

    private ArrayList<Screenshot> getContent() {
        ArrayList<Screenshot> screenshots = null;
        try {
            screenshots = ScreenshotUtil.getScreenshots("/home/ricky/Pictures/Screenshots");
        } catch (NotDirectoryException notDirectoryException) {
            // TODO: show a pop up error here or something
        }
        return screenshots;
    }

    private void populateContentArea(ArrayList<Screenshot> content) {
        if (content != null) {
            for (Screenshot screenshot : content) {
                ScreenshotImageView screenshotImageView = setImageView(screenshot);
                allScreenshots.add(screenshotImageView);
                BorderPane borderPane = setImageViewBackground(screenshotImageView);

                // handle clicks
                borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
                    // right click
                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        contextMenu.show(borderPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                    }

                    // left click
                    // control + click
                    if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.isControlDown()) {
                        screenshotImageView.setOpacity(0.5);
                        selectedScreenshots.add(screenshotImageView);
                    } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.isShiftDown()) {
                        // TODO: come up with an algorithm to get shift clicking to work.
                        System.out.println("shift clicking");
                    } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) { // normal left click

                        if (mouseEvent.getClickCount() == 1) {
                            // if image is currently selected, deselect it otherwise select it.
                            if (selectedScreenshots.contains(screenshotImageView)) {
                                screenshotImageView.setOpacity(1);
                                clearSelectedImageViews();
                            } else {
                                clearSelectedImageViews();
                                screenshotImageView.setOpacity(0.5);
                                selectedScreenshots.add(screenshotImageView);
                            }
                            // TODO: update info in side panels
                        }

                        if (mouseEvent.getClickCount() == 2) {
                            selectedScreenshots.add(screenshotImageView);
                            PreviewModal.showPreview(screenshot);
                            // TODO: open window to show enlarged view of image
                        }
                    }
                });

                mainContentFlowPane.getChildren().add(borderPane);
            }
        } else {
            // TODO: show a pop up error here or something
        }
    }

    private void clearSelectedImageViews() {
        for (ImageView view : selectedScreenshots) {
            view.setOpacity(1);
        }
        selectedScreenshots.clear();
    }

    private void hideScrollBars() {
        mainContentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        mainContentScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    }

    private void setScrollSpeed() {
        mainContentScrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * SCROLL_SPEED;
            mainContentScrollPane.setVvalue(mainContentScrollPane.getVvalue() - deltaY);
        });
    }

    private ScreenshotImageView setImageView(Screenshot screenshot) {
        ScreenshotImageView screenshotImageView = new ScreenshotImageView();
        screenshotImageView.setScreenshot(screenshot);
        screenshotImageView.setImage(ScreenshotUtil.scale(screenshot, IMAGE_WIDTH, IMAGE_HEIGHT, true));
        return screenshotImageView;
    }

    private BorderPane setImageViewBackground(ScreenshotImageView screenshotImageView) {
        BorderPane borderPane = new BorderPane();
        borderPane.backgroundProperty().setValue(new Background(new BackgroundFill(Paint.valueOf("000000"), CornerRadii.EMPTY, null)));
        borderPane.setPrefWidth(IMAGE_WIDTH);
        borderPane.setPrefHeight(IMAGE_HEIGHT);
        borderPane.setCenter(screenshotImageView);
        return borderPane;
    }

    // TODO: add actual functionality for the menu options
    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem menuItem1 = new MenuItem("menu item 1");
        menuItem1.setOnAction((actionEvent) -> {
            System.out.println("menu item 1 clicked");
            System.out.println("selected amount: " + selectedScreenshots.size());
        });

        MenuItem menuItem2 = new MenuItem("menu item 2");
        menuItem2.setOnAction((actionEvent) -> {
            System.out.println("menu item 2 clicked");
            System.out.println("selected amount: " + selectedScreenshots.size());
        });

        MenuItem menuItem3 = new MenuItem("menu item 3");
        menuItem3.setOnAction((actionEvent) -> {
            System.out.println("menu item 3 clicked");
            System.out.println("selected amount: " + selectedScreenshots.size());
        });

        MenuItem menuItem4 = new MenuItem("menu item 4");
        menuItem4.setOnAction((actionEvent) -> {
            System.out.println("menu item 4 clicked");
            System.out.println("selected amount: " + selectedScreenshots.size());
        });

        contextMenu.getItems().addAll(menuItem1, menuItem2, menuItem3, menuItem4);
        return contextMenu;
    }
}
