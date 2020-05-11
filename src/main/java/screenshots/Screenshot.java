package screenshots;

public class Screenshot {
    private String url;
    private String name;

    public Screenshot(String url) {
        this.url = url;
        String[] splitUrl = url.split("/");
        this.name = splitUrl[splitUrl.length - 1];
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
