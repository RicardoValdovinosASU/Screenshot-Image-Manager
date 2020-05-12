package controllers;

import groups.ScreenshotGroup;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import modals.PreviewModal;
import modals.ScreenshotGroupModal;
import screenshots.Screenshot;
import screenshots.ScreenshotImageView;
import screenshots.ScreenshotUtil;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainContentController implements Initializable {
    public FlowPane mainContentFlowPane;
    public ScrollPane mainContentScrollPane;
    private ContextMenu contextMenu;
    private ArrayList<ScreenshotImageView> allScreenshots = new ArrayList<>();
    private ArrayList<ScreenshotImageView> selectedScreenshots = new ArrayList<>();
    private int initialShiftClick, secondShiftClick, shiftClickCount = 0;
    private static final int FLOW_PANE_COLUMN_SIZE = 10;
    private static final int FLOW_PANE_ROW_SIZE = 5;
    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 100;
    private static final double SCROLL_SPEED = 0.0005;
    private static final int TRANSITION_SPEED = 500;
    private static final int BUTTON_OFFSET = 20;
    private boolean isLeftSideBarHidden = false;
    private boolean isRightSideBarHidden = false;


    /* Main Content */
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

    // TODO: only showing 180/353 images
    private void populateContentArea(ArrayList<Screenshot> content) {
        if (content != null) {
            for (Screenshot screenshot : content) {
                BorderPane borderPane = createScreenshotIcon(screenshot);

                clickHandler(borderPane, (ScreenshotImageView) borderPane.getCenter());

                mainContentFlowPane.getChildren().add(borderPane);
            }
        } else {
            // TODO: show a pop up error here or something
        }
    }

    private void clickHandler(BorderPane borderPane, ScreenshotImageView screenshotImageView) {
        borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
            rightClickHandler(mouseEvent, borderPane, screenshotImageView);

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.isControlDown()) {
                controlClickHandler(mouseEvent, borderPane, screenshotImageView);
            } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.isShiftDown()) {
                shiftClickHandler(mouseEvent, borderPane, screenshotImageView);
            } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) { // normal left click
                singleClickHandler(mouseEvent, borderPane, screenshotImageView);

                if (mouseEvent.getClickCount() == 2) {
                    doubleClickHandler(mouseEvent, borderPane, screenshotImageView);
                }
            }
        });
    }

    private void rightClickHandler(MouseEvent mouseEvent, BorderPane borderPane, ScreenshotImageView screenshotImageView) {
        if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
            contextMenu.show(borderPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
        }
    }

    private void controlClickHandler(MouseEvent mouseEvent, BorderPane borderPane, ScreenshotImageView screenshotImageView) {
        screenshotImageView.setOpacity(0.5);
        selectedScreenshots.add(screenshotImageView);
    }

    private void shiftClickHandler(MouseEvent mouseEvent, BorderPane borderPane, ScreenshotImageView screenshotImageView) {
        clearSelectedImageViews();
        secondShiftClick = allScreenshots.indexOf(screenshotImageView);
        if (initialShiftClick < secondShiftClick) {
            for (int i = initialShiftClick; i <= secondShiftClick; i++) {
                allScreenshots.get(i).setOpacity(0.5);
                selectedScreenshots.add(allScreenshots.get(i));
            }
        } else {
            for (int i = secondShiftClick; i <= initialShiftClick; i++) {
                allScreenshots.get(i).setOpacity(0.5);
                selectedScreenshots.add(allScreenshots.get(i));
            }
        }
        System.out.println(selectedScreenshots.size());
    }

    private void singleClickHandler(MouseEvent mouseEvent, BorderPane borderPane, ScreenshotImageView screenshotImageView) {
        initialShiftClick = allScreenshots.indexOf(screenshotImageView);
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
    }

    private void doubleClickHandler(MouseEvent mouseEvent, BorderPane borderPane, ScreenshotImageView screenshotImageView) {
        selectedScreenshots.add(screenshotImageView);
        PreviewModal.showPreview(screenshotImageView.getScreenshot());
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

    public BorderPane createScreenshotIcon(Screenshot screenshot) {
        ScreenshotImageView screenshotImageView = setImageView(screenshot);
        allScreenshots.add(screenshotImageView);
        return setImageViewBackground(screenshotImageView);
    }

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem createEmptyGroup = new MenuItem("Create empty group");
        createEmptyGroup.setOnAction((actionEvent) -> {
            String resource = "views/ScreenshotGroupModalView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
            showScreenshotGroupModal(loader);
            ScreenshotGroupModal modal = loader.getController();
            String groupName = modal.getGroupName();
            if (groupName != null) {
                ScreenshotGroup screenshotGroup = new ScreenshotGroup(groupName);
                File file = new File("/home/ricky/Documents/programming/java/Intellij-Projects/Screenshot-Image-Manager/src/main/resources/assets/groups.png");
                ImageView imageView = new ImageView(new Image(file.toURI().toString()));
                mainContentFlowPane.getChildren().add(new BorderPane(imageView));
            } else {
                // TODO: show a pop up error here or something
            }
        });

        MenuItem createGroupFromSelected = new MenuItem("Create group from selected");
        createGroupFromSelected.setOnAction((actionEvent) -> {
            String resource = "views/ScreenshotGroupModalView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
            showScreenshotGroupModal(loader);
            ScreenshotGroupModal modal = loader.getController();
            String groupName = modal.getGroupName();
            if (groupName != null) {
                ScreenshotGroup screenshotGroup = new ScreenshotGroup(groupName, selectedScreenshots);
                for (ScreenshotImageView s: screenshotGroup.getGroupScreenshotImageViews()) {
                    System.out.println(s.getScreenshot().getName());
                }
            } else {
                // TODO: show a pop up error here or something
            }
        });

        // TODO: create sub menu listing the available groups
        MenuItem addSelectionsToGroup = new MenuItem("Add selections to group");
        addSelectionsToGroup.setOnAction((actionEvent) -> {
            System.out.println("menu item 3 clicked");
            System.out.println("selected amount: " + selectedScreenshots.size());
        });

        MenuItem menuItem4 = new MenuItem("menu item 4");
        menuItem4.setOnAction((actionEvent) -> {
            System.out.println("menu item 4 clicked");
            System.out.println("selected amount: " + selectedScreenshots.size());
        });

        contextMenu.getItems().addAll(createEmptyGroup, createGroupFromSelected, addSelectionsToGroup, menuItem4);
        return contextMenu;
    }

    private void showScreenshotGroupModal(FXMLLoader loader) {
        Stage stage = new Stage();
        AnchorPane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
    }

    /* Left Sidebar */
    public Pane leftSidePane;
    public Rectangle leftSideButton;

    public void onLeftSideButtonClicked(MouseEvent mouseEvent) {
        double leftSidePaneHiddenPosition = (leftSidePane.getLayoutX() - leftSidePane.getWidth()) + (leftSidePane.getWidth() * 0.25);
        double leftSideButtonHiddenPosition = leftSidePaneHiddenPosition - BUTTON_OFFSET;
        if (!isLeftSideBarHidden) {
            Utils.translateTransition(leftSidePane, TRANSITION_SPEED, 0, leftSidePaneHiddenPosition);
            Utils.translateTransition(leftSideButton, TRANSITION_SPEED, 0, leftSideButtonHiddenPosition);
            Utils.fadeTransition(leftSidePane, TRANSITION_SPEED, 1.0, 0.0);
            isLeftSideBarHidden = true;
        } else {
            Utils.translateTransition(leftSidePane, TRANSITION_SPEED, leftSidePaneHiddenPosition, 0);
            Utils.translateTransition(leftSideButton, TRANSITION_SPEED, leftSideButtonHiddenPosition, 0);
            Utils.fadeTransition(leftSidePane, TRANSITION_SPEED, 0.0, 1.0);
            isLeftSideBarHidden = false;
        }
    }

    public void onLeftSideButtonEntered(MouseEvent mouseEvent) {
        leftSideButton.fillProperty().setValue(Paint.valueOf("e0e0e0"));
    }

    public void onLeftSideButtonExited(MouseEvent mouseEvent) {
        leftSideButton.fillProperty().setValue(Paint.valueOf("c6c6c6"));
    }

    /* Right Sidebar */
    public Pane rightSidePane;
    public Rectangle rightSideButton;

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
