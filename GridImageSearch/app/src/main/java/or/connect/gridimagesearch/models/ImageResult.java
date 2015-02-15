package or.connect.gridimagesearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by moulib on 2/14/15.
 */
public class ImageResult {
    public String title;
    public String fullUrl;
    public String thumbUrl;
    public int tbWidth;
    public int tbHeight;

    //new ImageResult
    public ImageResult(JSONObject jsonObj) {
        try {
            /* TODO - fix HTML formats */
            this.title = jsonObj.getString("titleNoFormatting");
            if (this.title == null) {
                this.title = jsonObj.getString("title");
            }
            this.fullUrl = jsonObj.getString("url");
            this.thumbUrl = jsonObj.getString("tbUrl");
            this.tbWidth = jsonObj.getInt("tbWidth");
            this.tbHeight = jsonObj.getInt("tbHeight");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //ImageResult.fromJSONArray
    public static ArrayList<ImageResult> fromJSONArray(JSONArray imageArray) {

        ArrayList<ImageResult> imageResults = new ArrayList<>();

        for (int i = 0; i < imageArray.length(); i++) {
            try {
                imageResults.add(new ImageResult(imageArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return imageResults;
    }
}
