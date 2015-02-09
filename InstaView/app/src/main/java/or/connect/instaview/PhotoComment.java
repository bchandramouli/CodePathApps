package or.connect.instaview;

import java.io.Serializable;

/**
 * Created by moulib on 2/7/15.
 */
public class PhotoComment implements Serializable {
    private static final long serialVersionUID = 1234567890L;
    public String from;
    public long createTime;
    public String text;
    public String fromProfile;
}
