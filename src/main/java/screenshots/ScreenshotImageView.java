package screenshots;

import javafx.scene.image.ImageView;

public class ScreenshotImageView extends ImageView {
    private Screenshot screenshot;

    public ScreenshotImageView() {
        screenshot = null;
    }

    public ScreenshotImageView(String url) {
        super(url);
        screenshot = new Screenshot(url);
    }

    public ScreenshotImageView(Screenshot screenshot) {
        super(screenshot);
    }

    public Screenshot getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(Screenshot screenshot) {
        this.screenshot = screenshot;
    }
}
