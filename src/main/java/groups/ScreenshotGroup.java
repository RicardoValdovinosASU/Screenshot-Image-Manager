package groups;

import screenshots.ScreenshotImageView;

import java.util.ArrayList;

public class ScreenshotGroup {
    private ArrayList<ScreenshotImageView> groupScreenshotImageViews;
    private String name;

    public ScreenshotGroup(String name) {
        this.name = name;
    }

    public ScreenshotGroup(String name, ArrayList<ScreenshotImageView> groupScreenshotImageViews) {
        this.groupScreenshotImageViews = groupScreenshotImageViews;
        this.name = name;
    }

    public ArrayList<ScreenshotImageView> getGroupScreenshotImageViews() {
        return groupScreenshotImageViews;
    }

    public void setGroupScreenshotImageViews(ArrayList<ScreenshotImageView> groupScreenshotImageViews) {
        this.groupScreenshotImageViews = groupScreenshotImageViews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
