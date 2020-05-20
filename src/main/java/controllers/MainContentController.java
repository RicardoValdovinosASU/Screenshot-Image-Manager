package controllers;

import groups.GroupImageView;
import groups.ScreenshotGroup;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import modals.PreviewModal;
import modals.ScreenshotGroupModal;
import model.Data;
import screenshots.Screenshot;
import screenshots.ScreenshotImageView;
import screenshots.ScreenshotUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainContentController extends Controller implements Initializable {
    private static final int FLOW_PANE_COLUMN_SIZE = 10;
//    private static final int FLOW_PANE_ROW_SIZE = 5;
    private static final int IMAGE_WIDTH = 150;
    private static final int IMAGE_HEIGHT = 100;
    private static final String ASSETS_PATH = "/home/ricky/Documents/programming/java/Intellij-Projects/Screenshot-Image-Manager/src/main/resources/assets/";
    public FlowPane mainContentFlowPane;
    public ScrollPane mainContentScrollPane;
    private int initialShiftClick;
    private ContextMenu contextMenu;
    private RightSidePaneController rightSidePaneController;
    private LeftSidePaneController leftSidePaneController; // will be used later to display settings

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Screenshot> content = getContentFromDirectory("/home/ricky/Pictures/Screenshots");
        populateContentArea(content);

        hideScrollBars();

        // set height to number of rows
        mainContentFlowPane.setPrefHeight(IMAGE_HEIGHT * (content.size() / (double) FLOW_PANE_COLUMN_SIZE));

        setScrollSpeed();

        contextMenu = createContextMenu();

        initializeControllers(); // FIXME: returning null pointer exception
    }

    private ArrayList<Screenshot> getContentFromDirectory(String directoryPath) {
        ArrayList<Screenshot> screenshots = null;
        try {
            screenshots = ScreenshotUtil.getScreenshots(directoryPath);
        } catch (NotDirectoryException notDirectoryException) {
            // TODO: show a pop up error here or something
        }
        return screenshots;
    }

    // TODO: only showing 180/353 images
    private void populateContentArea(ArrayList<Screenshot> content) {
        if (content != null) {
            for (Screenshot screenshot : content) {
                BorderPane borderPane = createIconForScreenshot(screenshot);

                clickHandler(borderPane, (ScreenshotImageView) borderPane.getCenter());

                mainContentFlowPane.getChildren().add(borderPane);
            }
        } else {
            // TODO: show a pop up error here or something
        }
    }

    private void clickHandler(BorderPane borderPane, ScreenshotImageView clickedScreenshotImageView) {
        borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                rightClickHandler(mouseEvent, borderPane);
            }

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.isControlDown()) {
                controlClickHandler(clickedScreenshotImageView);
            } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.isShiftDown()) {
                shiftClickHandler(clickedScreenshotImageView);
            } else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) { // normal left click
                initialShiftClick = Data.getAllScreenshots().indexOf(clickedScreenshotImageView);
                if (mouseEvent.getClickCount() == 1) {
                    singleClickHandler(clickedScreenshotImageView);
                }

                if (mouseEvent.getClickCount() == 2) {
                    doubleClickHandler(clickedScreenshotImageView);
                }
            }
        });
    }

    private void rightClickHandler(MouseEvent mouseEvent, BorderPane borderPane) {
        contextMenu.show(borderPane, mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    private void controlClickHandler(ScreenshotImageView clickedScreenshotImageView) {
        clickedScreenshotImageView.setOpacity(0.5);
        Data.getSelectedScreenshots().add(clickedScreenshotImageView);
    }

    private void shiftClickHandler(ScreenshotImageView clickedScreenshotImageView) {
        clearSelectedImageViews();
        int secondShiftClick = Data.getAllScreenshots().indexOf(clickedScreenshotImageView);
        if (initialShiftClick < secondShiftClick) {
            for (int i = initialShiftClick; i <= secondShiftClick; i++) {
                Data.getAllScreenshots().get(i).setOpacity(0.5);
                Data.getSelectedScreenshots().add(Data.getAllScreenshots().get(i));
            }
        } else {
            for (int i = secondShiftClick; i <= initialShiftClick; i++) {
                Data.getAllScreenshots().get(i).setOpacity(0.5);
                Data.getSelectedScreenshots().add(Data.getAllScreenshots().get(i));
            }
        }
        System.out.println(Data.getSelectedScreenshots().size());
    }

    private void singleClickHandler(ScreenshotImageView clickedScreenshotImageView) {
        // if image is currently selected, deselect it otherwise select it.
        if (Data.getSelectedScreenshots().contains(clickedScreenshotImageView)) {
            clickedScreenshotImageView.setOpacity(1);
            clearSelectedImageViews();
        } else {
            clearSelectedImageViews();
            clickedScreenshotImageView.setOpacity(0.5);
            Data.getSelectedScreenshots().add(clickedScreenshotImageView);
            rightSidePaneController.displayInfo(clickedScreenshotImageView);
        }
    }

    private void doubleClickHandler(ScreenshotImageView clickedScreenshotImageView) {
        Data.getSelectedScreenshots().add(clickedScreenshotImageView);
        PreviewModal.showPreview(clickedScreenshotImageView.getScreenshot());
    }

    private void clearSelectedImageViews() {
        for (ImageView view : Data.getSelectedScreenshots()) {
            view.setOpacity(1);
        }
        Data.getSelectedScreenshots().clear();
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

    private ContextMenu createContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        MenuItem createEmptyGroup = new MenuItem("Create empty group");
        createEmptyGroup.setOnAction((this::createEmptyGroupActionEvent));

        MenuItem createGroupFromSelected = new MenuItem("Create group from selected");
        createGroupFromSelected.setOnAction(this::createGroupFromSelectedActionEvent);

        // TODO: create sub menu listing the available groups
        MenuItem addSelectionsToGroup = new MenuItem("Add selections to group");
        addSelectionsToGroup.setOnAction(this::addSelectionsToGroupActionEvent);

        contextMenu.getItems().addAll(createEmptyGroup, createGroupFromSelected, addSelectionsToGroup);
        return contextMenu;
    }

    private void createEmptyGroupActionEvent(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/ScreenshotGroupModalView.fxml"));
        showScreenshotGroupModal(loader);
        ScreenshotGroupModal modal = loader.getController();
        String groupName = modal.getGroupName();
        if (groupName != null) {
            ScreenshotGroup screenshotGroup = new ScreenshotGroup(groupName);
            GroupImageView groupImageView = createImageViewForGroup(ASSETS_PATH + "groups.png", screenshotGroup.getName());
            addGroupToMainContent(groupImageView);
            Data.getAllGroups().add(screenshotGroup);
        } else {
            // TODO: show a pop up error here or something
        }
    }

    private void createGroupFromSelectedActionEvent(ActionEvent actionEvent) {
        String resource = "views/ScreenshotGroupModalView.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
        showScreenshotGroupModal(loader);
        ScreenshotGroupModal modal = loader.getController();
        String groupName = modal.getGroupName();
        if (groupName != null) {
            ScreenshotGroup screenshotGroup = new ScreenshotGroup(groupName, Data.getSelectedScreenshots());
            GroupImageView groupImageView = createImageViewForGroup(ASSETS_PATH + "groups.png", screenshotGroup.getName());
            addGroupToMainContent(groupImageView);
        } else {
            // TODO: show a pop up error here or something
        }
    }

    public void addSelectionsToGroupActionEvent(ActionEvent actionEvent) {
        System.out.println("menu item 3 clicked");
        System.out.println("selected amount: " + Data.getSelectedScreenshots().size());
    }

    private void initializeControllers() {
        rightSidePaneController = getControllerFromView("views/RightSidePaneView.fxml");
        leftSidePaneController = getControllerFromView("views/LeftSidePaneView");
    }

    private <T> T getControllerFromView(String resourceLocation) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resourceLocation));
        return loader.getController();
    }

    private BorderPane createIconForScreenshot(Screenshot screenshot) {
        ScreenshotImageView screenshotImageView = createImageViewForScreenshot(screenshot);
        Data.getAllScreenshots().add(screenshotImageView);
        return createImageViewForBackground(screenshotImageView);
    }

    private ScreenshotImageView createImageViewForScreenshot(Screenshot screenshot) {
        ScreenshotImageView screenshotImageView = new ScreenshotImageView();
        screenshotImageView.setScreenshot(screenshot);
        screenshotImageView.setImage(ScreenshotUtil.scale(screenshot, IMAGE_WIDTH, IMAGE_HEIGHT, true));
        return screenshotImageView;
    }

    private BorderPane createImageViewForBackground(ScreenshotImageView screenshotImageView) {
        BorderPane borderPane = new BorderPane();
        borderPane.backgroundProperty().setValue(new Background(new BackgroundFill(Paint.valueOf("000000"), CornerRadii.EMPTY, null)));
        borderPane.setPrefWidth(IMAGE_WIDTH);
        borderPane.setPrefHeight(IMAGE_HEIGHT);
        borderPane.setCenter(screenshotImageView);
        return borderPane;
    }

    private GroupImageView createImageViewForGroup(String path, String name) {
        File file = new File(path);
        GroupImageView groupImageView = new GroupImageView(new Image(file.toURI().toString()));
        groupImageView.setName(name);
        groupImageView.setFitWidth(IMAGE_WIDTH);
        groupImageView.setFitHeight(IMAGE_HEIGHT);
        groupImageView.setPreserveRatio(true);
        return groupImageView;
    }

    private void addGroupToMainContent(GroupImageView groupImageView) {
        mainContentFlowPane.getChildren().clear();
        BorderPane borderPane = new BorderPane();
        StackPane stackPane = new StackPane();
        Label label = new Label(groupImageView.getName());
        stackPane.getChildren().add(groupImageView);
        stackPane.getChildren().add(label);
        borderPane.setCenter(stackPane);
        borderPaneClickHandler(borderPane);
        mainContentFlowPane.getChildren().add(borderPane);
        ArrayList<Screenshot> temp = new ArrayList<>();
        for (ScreenshotImageView screenshotImageView : Data.getAllScreenshots()) {
            temp.add(screenshotImageView.getScreenshot());
        }
        Data.getAllScreenshots().clear();
        populateContentArea(temp);
        temp.clear();
    }

    private void borderPaneClickHandler(BorderPane borderPane) {
        borderPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent) -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                handleSinglePrimaryClick(mouseEvent, borderPane);

                if (mouseEvent.getClickCount() == 2) {
                    handlerDoublePrimaryClick(mouseEvent, borderPane);
                }
            } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                handleSecondaryClick(mouseEvent, borderPane);
            }
        });
    }

    private void handleSinglePrimaryClick(MouseEvent mouseEvent, BorderPane borderPane) {
        System.out.println("group clicked!" + ((GroupImageView) borderPane.getCenter()).getName());
    }

    private void handlerDoublePrimaryClick(MouseEvent mouseEvent, BorderPane borderPane) {
        System.out.println("group clicked!" + ((GroupImageView) borderPane.getCenter()).getName());
    }

    private void handleSecondaryClick(MouseEvent mouseEvent, BorderPane borderPane) {
        System.out.println("group clicked!" + ((GroupImageView) borderPane.getCenter()).getName());
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
}