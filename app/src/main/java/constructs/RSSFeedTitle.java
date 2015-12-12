package constructs;

import java.io.Serializable;

public class RSSFeedTitle implements Serializable {
private String url="";
    private String name="";

    public RSSFeedTitle(String MyUrl, String MyName){ //инициализация
     this.url =    MyUrl;
      this.name = MyName;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }
}
