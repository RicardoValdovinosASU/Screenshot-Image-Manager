package groups;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GroupImageView extends ImageView {
    private String name;

    public GroupImageView() {
    }

    public GroupImageView(String name) {
        this.name = name;
    }

    public GroupImageView(Image image) {
        super.setImage(image);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
