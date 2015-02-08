package or.connect.instaview;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class PopularPhotos extends ActionBarActivity {

    public static final String CLIENT_ID = "409a67a59576427789eee4d7edcc2058";

    private ArrayList<InstagramPhoto> photoList;
    private InstagramPhtotosAdapter aPhotos;

    // SwipeRefreshLayout
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_photos);

        // Init photo array
        photoList = new ArrayList<>();

        // Create the adapter and link it to the source
        aPhotos = new InstagramPhtotosAdapter(this, photoList);

        // Find the listview
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        // bind it to the custom adapter
        lvPhotos.setAdapter(aPhotos);

        swipeContainer = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data to load
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Send the API request to Instagram
                fetchPopularPhotos();
            }
        });

        // Configure refreshing colors
        swipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPopularPhotos();
    }

    public void fetchPopularPhotos() {
        // Popular: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        // Initiate the request
        client.get(url, null, new JsonHttpResponseHandler(){
            // onSuccess => 200
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /*
                 * Response: Expect a Json Object
                 *   - TYPE: {“data” => [x] => “type” => “image” or “video"
                 *       - URL: {“data” => “images” => “standard resolution” => “url”}
                 *   - Likes: {“data”=> [x] => “likes” => “count”}
                 *   - Author: {“data” => [x] => “user” => “username”}
                 *   - posted time: {“data” => [x] => “created_time”}
                 *   - Caption: {“data” => [x] => “caption” => “text”}
                 *   - Comments: {"data" => [x] =>
                 *        "comments" => "data" => [x] => "created_time", "text", "count", "from" => "username", "profile_picture"
                 */
                //Log.i("DEBUG", response.toString());

                /* Iterate over the objects and parse them */
                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // decode the json object into the object model
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        // create a new photo object and populate it
                        InstagramPhoto photo = new InstagramPhoto();

                        JSONObject user = photoJSON.getJSONObject("user");
                        if (user != null) {
                            photo.username = user.getString("username");
                            photo.userProfileUrl = user.getString("profile_picture");
                        } else {
                            photo.username = "foo";
                            photo.userProfileUrl = "";
                        }
                        try {
                            JSONObject caption = photoJSON.getJSONObject("caption");
                            if (caption != null) {
                                photo.caption = caption.getString("text");
                            } else {
                                photo.caption = "";
                            }
                        } catch (JSONException e) {
                            photo.caption = "";
                        }

                        JSONObject images = photoJSON.getJSONObject("images");
                        if (images != null) {
                            images = images.getJSONObject("standard_resolution");
                        }
                        if (images != null) {
                            photo.imageUrl = images.getString("url");
                            photo.imageHeight = images.getInt("height");
                        } else {
                            photo.imageUrl = "";
                            photo.imageHeight = 10;
                        }

                        photo.timeCreated = Long.parseLong(photoJSON.getString("created_time"));

                        JSONObject likes = photoJSON.getJSONObject("likes");
                        if (likes != null) {
                            photo.likesCount = likes.getInt("count");
                        } else {
                            photo.likesCount = 0;
                        }

                        JSONObject comments = photoJSON.getJSONObject("comments");
                        if (comments != null) {

                            // get the comments from the data array!
                            JSONArray commentArray = comments.getJSONArray("data");

                            photo.comments = new ArrayList<PhotoComment>();
                            photo.commentCount = commentArray.length();

                            for (int j = 0; j < commentArray.length(); j++) {
                                JSONObject cmt = commentArray.getJSONObject(j);
                                PhotoComment photoComment = new PhotoComment();

                                photoComment.createTime = Long.parseLong(cmt.getString("created_time"));
                                photoComment.text = cmt.getString("text");

                                JSONObject from = cmt.getJSONObject("from");
                                if (from != null) {
                                    photoComment.from = from.getString("username");
                                    photoComment.fromProfile = from.getString("profile_picture");
                                }
                                photo.comments.add(photoComment);
                            }

                        } else {
                           photo.commentCount = 0;
                        }

                        photoList.add(photo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // notify the change
                aPhotos.notifyDataSetChanged();

                // Now call setRefreshing(false) to signal that refresh finished
                swipeContainer.setRefreshing(false);
            }

            // onFailure - failed

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                // Failure handling - TODO
                //super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
