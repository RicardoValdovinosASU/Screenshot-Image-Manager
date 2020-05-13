package screenshots;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class ScreenshotUtil {
    private ScreenshotUtil() {
    }

    // get all screenshots within a directory
    public static ArrayList<Screenshot> getScreenshots(String directoryPath) throws NotDirectoryException {
        ArrayList<Screenshot> screenshots = new ArrayList<>();

        File directory = new File("/home/ricky/Pictures/Screenshots");
        if (!directory.isDirectory()) throw new NotDirectoryException(directoryPath);

        File[] directoryListing = directory.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (getFileExtension(child).equals("png")) {
                    Screenshot screenshot = new Screenshot(child.toURI().toString());
                    try {
                        screenshot.setBasicFileAttributes(Files.readAttributes(Path.of(child.toURI()), BasicFileAttributes.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    screenshots.add(screenshot);
                }
            }
        }
        return screenshots;
    }

    public static String getFileExtension(File file) {
        String fileExtension = "";
        String fileName;
        if (file != null) {
            fileName = file.getName();
            if (fileName.contains(".")) {
                String[] temp = fileName.split("\\.");
                fileExtension = temp[temp.length - 1];
            }
        }
        return fileExtension;
    }

    public static Image scale(Image source, int targetWidth, int targetHeight, boolean preserveRatio) {
        ImageView imageView = new ImageView(source);
        imageView.setPreserveRatio(preserveRatio);
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        return imageView.snapshot(null, null);
    }
}
