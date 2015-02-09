package or.connect.instaview;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by moulib on 2/8/15.
 */
public class CommentWrapper implements Serializable {

    private static final long serialVersionUID = 71234567890L;
    private ArrayList<PhotoComment> comments;

    public CommentWrapper(ArrayList<PhotoComment> data) {
        this.comments = data;
    }

    public ArrayList<PhotoComment> getComments() {
        return this.comments;
    }
}
