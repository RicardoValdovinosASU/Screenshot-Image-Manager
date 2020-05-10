package screenshots;

import java.io.File;
import java.nio.file.NotDirectoryException;
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
                    screenshots.add(new Screenshot(child.getAbsolutePath()));
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
}
