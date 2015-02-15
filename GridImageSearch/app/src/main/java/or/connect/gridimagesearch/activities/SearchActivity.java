package or.connect.gridimagesearch.activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import abstract_classes.EndlessScrollListener;
import or.connect.gridimagesearch.R;
import or.connect.gridimagesearch.adapters.ImageResultsAdapter;
import or.connect.gridimagesearch.fragments.FilterDialog;
import or.connect.gridimagesearch.fragments.ImageDialog;
import or.connect.gridimagesearch.models.ImageResult;


public class SearchActivity extends ActionBarActivity implements FilterDialog.OnSaveListener {

    private EditText etQuery;
    private GridView gvResults;

    private ArrayList<ImageResult> imageResults;
    private ImageResultsAdapter imageResultsAdapter;

    private String color_filter = "any";
    private String image_size = "any";
    private String image_type = "any";
    private String site_filter = "all";
    private int image_count = 0;

    public boolean firstReq = true;

    private String sQuery = "Android";

    public String getColor_filter() {
        return color_filter;
    }

    public void setColor_filter(String color_filter) {
        this.color_filter = color_filter;
    }

    public String getImage_size() {
        return image_size;
    }

    public void setImage_size(String image_size) {
        this.image_size = image_size;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public String getSite_filter() {
        return site_filter;
    }

    public void setSite_filter(String site_filter) {
        this.site_filter = site_filter;
    }

    private RequestParams setReqParams(int page) {

        RequestParams params = new RequestParams();

        //Set the query
        params.put("q", sQuery);

        if (page > 0) {
            // Start page for endless pagination
            params.put("start", page);
            firstReq = false;
        } else {
            firstReq = true;
        }

        if (image_count > 0) {
            // number of images
            params.put("rsz", image_count);
        }

        if (image_size != "any") {
            // image size filter
            params.put("imgsz", image_size);
        }

        if (color_filter != "any") {
            // colors - blue, black, red, ...
            params.put("imgcolor", color_filter);
        }

        if (image_type != "any") {
            // type - icon, small, medium, large, huge, ...
            params.put("imgtype", image_type);
        }

        if (site_filter != "all") {
            // site - espn.com, ...
            params.put("as_sitesearch", site_filter);
        }

        return params;
    }

    private Boolean networkAvailable() {
        try {
            Process p1 = java.lang.Runtime.getRuntime().exec("ping -n 5 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    private void searchQuery(int page) {

        //"https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=dog";
        String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0";
        RequestParams params;
        AsyncHttpClient client = new AsyncHttpClient();

        if (!networkAvailable()) {
            Toast.makeText(getApplicationContext(), "No network access", Toast.LENGTH_LONG).show();
        }

        params = setReqParams(page);

        /*
         * Response Json Data
         *
         * responseData =>
         *     results => [x] =>
         *         "title"
         *         "url"
         *         "width"
         *         "height"
         *         "tbUrl"
         *         "tbWidth"
         *         "tbHeight"
         */
        client.get(searchUrl, params, new JsonHttpResponseHandler() {
            //OnSuccess => 200
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (response == null) return;

                try {
                    //Log.d("DEBUG", response.toString());

                    JSONObject responseData = response.getJSONObject("responseData");
                    if (responseData == null) return;

                    JSONArray resultArr = responseData.getJSONArray("results");

                    // TODO - why does this not work for the first time to display infinite images?
                    if (firstReq) { // clear the existing images (only for new search!)
                        imageResults.clear();
                    }

                    // Adding to the adapter, updates the underlying data source!
                    imageResultsAdapter.addAll(ImageResult.fromJSONArray(resultArr));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //onFailure
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupViews();
        searchQuery(0);
    }

    private void setupViews() {
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);

        gvResults.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered when new data needs to be loaded
                searchQuery(page);
                // or customLoadMoreDataFromApi(totalItemsCount);
            }
        });

        //Set a toolbar to replace the ActionBar;
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        // Create data source - to hold the image search results
        imageResults = new ArrayList<>();

        //Attach the data source to an adapter
        imageResultsAdapter = new ImageResultsAdapter(this, imageResults);

        // Link the adapter to the adapterView
        gvResults.setAdapter(imageResultsAdapter);
    }

    public void onImageSearch(View v) {
        sQuery = etQuery.getText().toString();
        searchQuery(0);
    }

    /*
     * If the image/text is clicked
     *     - Start a dialog fragment
     *     - pass the full url to be displayed!
     */
    public void onImageClick(View v) {
        int position = gvResults.getPositionForView((View) v.getParent());
        ImageResult image = imageResults.get(position);

        FragmentManager fm = getSupportFragmentManager();
        ImageDialog imageDialog = ImageDialog.newInstance(image.fullUrl);
        imageDialog.show(fm, "full_image");
    }

    /*
     * If the setting is clicked
     *  - display filter options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle presses on the action bar items
        switch (mi.getItemId()) {
            case R.id.miSearch:
                applyFilters();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    public void applyFilters() {
        FragmentManager fm = getSupportFragmentManager();
        FilterDialog filterDialog = FilterDialog.newInstance();
        filterDialog.show(fm, "filter_options");
    }

    // Now we can define the action to take in the activity when the fragment event fires
    @Override
    public void onFilterSelection(String name, String value) {
        switch (name) {
            case "color_filter":
                setColor_filter(value);
                break;
            case "site_filter":
                setSite_filter(value);
                break;
            case "image_size":
                setImage_size(value);
                break;
            case "image_type":
                setImage_type(value);
                break;
            default:
                break;
        }
    }
}