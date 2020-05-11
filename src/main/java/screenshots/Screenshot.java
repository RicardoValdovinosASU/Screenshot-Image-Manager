package screenshots;

import javafx.scene.image.Image;

public class Screenshot extends Image {
    private String location;
    private String name;

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
}
