package model;

import groups.ScreenshotGroup;
import screenshots.ScreenshotImageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
    private static final ArrayList<ScreenshotImageView> allScreenshots = new ArrayList<>();
    private static final ArrayList<ScreenshotImageView> selectedScreenshots = new ArrayList<>();
    private static final ArrayList<ScreenshotGroup> allGroups = new ArrayList<>();

    public static ArrayList<ScreenshotImageView> getAllScreenshots() {
        return allScreenshots;
    }

    public static ArrayList<ScreenshotImageView> getSelectedScreenshots() {
        return selectedScreenshots;
    }

    public static ArrayList<ScreenshotGroup> getAllGroups() {
        return allGroups;
    }
}
