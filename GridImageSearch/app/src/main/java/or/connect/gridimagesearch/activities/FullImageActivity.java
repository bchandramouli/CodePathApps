package or.connect.gridimagesearch.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import abstract_classes.EndlessScrollListener;
import or.connect.gridimagesearch.R;
import or.connect.gridimagesearch.adapters.ImageResultsAdapter;
import or.connect.gridimagesearch.fragments.ImageDialog;
import or.connect.gridimagesearch.models.ImageResult;

/**
 * Created by moulib on 2/20/15.
 */
public class FullImageActivity extends ActionBarActivity {

    private android.support.v7.widget.ShareActionProvider miShareAction;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialog, menu);


        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch reference to the share action provider
        miShareAction = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_layout);

        //Set a toolbar to replace the ActionBar;
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        ImageView ivFullImage = (ImageView) findViewById(R.id.ivFullImage);
        TextView tvFullTitle = (TextView) findViewById(R.id.tvFullTitle);

        ivFullImage.setImageResource(0);

        String url = getIntent().getStringExtra("full_image");
        Picasso.with(getContext().load(url).into(ivFullImage, new Callback() {
            @Override
            public void onSuccess() {
                //Setup the share intent after the image is loaded
                setupShareIntent(ivFullImage);
            }

            @Override
            public void onError() {
                // No work. :)
            }
        });

        tvFullTitle.setText(getIntent().getStringExtra("caption"));
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable)drawable).getBitmap();
        } else {
            return null;
        }

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bmp, "Description", null);

        if (path == null) {
            return null;
        } else {
            return(Uri.parse(path));
        }
    }

    // Gets the image URI and setup the associated share intent to hook into the provider
    public void setupShareIntent(ImageView ivImage) {
        // Fetch Bitmap Uri locally

        //ImageView ivImage = (ImageView)findViewById(R.id.ivFullImage);
        Uri bmpUri = getLocalBitmapUri(ivImage); // see previous remote images section

        if (bmpUri != null) {
            // Create share intent as described above
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Attach share event to the menu item provider
            miShareAction.setShareIntent(shareIntent);
        }
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

        return super.onOptionsItemSelected(mi);
    }
}
