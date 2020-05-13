package screenshots;

import javafx.scene.image.Image;

import java.nio.file.attribute.BasicFileAttributes;

public class Screenshot extends Image {
    private String location;
    private String name;
    private BasicFileAttributes basicFileAttributes;

    public Screenshot(String location) {
        super(location);
        this.location = location;
        String[] splitLocation = location.split("/");
        this.name = splitLocation[splitLocation.length - 1];
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return basicFileAttributes.creationTime().toString();
    }

    public String getLastModified(){
        return basicFileAttributes.lastModifiedTime().toString();
    }

    public void setBasicFileAttributes(BasicFileAttributes basicFileAttributes) {
        this.basicFileAttributes = basicFileAttributes;
    }
}
