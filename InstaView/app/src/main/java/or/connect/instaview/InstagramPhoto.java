package or.connect.instaview;

import java.util.ArrayList;

/**
 * Created by moulib on 2/4/15.
 */

public class InstagramPhoto {
    public String username;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public int imageWidth;
    public String filter;
    public int likesCount;
    public long timeCreated;
    public String userProfileUrl;
    public int commentCount;
    public int commentTotal;
    public ArrayList<PhotoComment> comments;
}
