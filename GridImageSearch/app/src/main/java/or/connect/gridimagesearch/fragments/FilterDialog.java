package or.connect.gridimagesearch.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import or.connect.gridimagesearch.R;
import or.connect.gridimagesearch.activities.SearchActivity;

/**
 * Created by moulib on 2/14/15.
 */
public class FilterDialog extends DialogFragment {

    private EditText imageSize;
    private EditText colorFilter;
    private EditText imageType;
    private EditText siteFilter;

    private OnSaveListener listener;

    //Define the interface now, that the parent activity needs to implement
    public interface OnSaveListener {
        public void onFilterSelection(String filter_name, String filter_choice);
    }

    public FilterDialog() {
        // Empty constructor needed for dialog fragments
    }

    public static FilterDialog newInstance() {
        FilterDialog fltr = new FilterDialog();
        Bundle args = new Bundle();

        // TODO Need args? args.putString("full_image", url);
        fltr.setArguments(args);
        return fltr;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnSaveListener) {
            this.listener = (OnSaveListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + "must implement FilterDialog.onSaveListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.filter, container);

        imageSize = (EditText) view.findViewById(R.id.etImageSize);
        colorFilter = (EditText) view.findViewById(R.id.etColorFilter);
        imageType = (EditText) view.findViewById(R.id.etImageType);
        siteFilter = (EditText) view.findViewById(R.id.etSiteFilter);

        imageSize.setText(((SearchActivity)listener).getImage_size());
        colorFilter.setText(((SearchActivity)listener).getColor_filter());
        imageType.setText(((SearchActivity)listener).getImage_type());
        siteFilter.setText(((SearchActivity) getActivity()).getSite_filter());

        // Get rid of the title on the full screen images
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        Button btSave = (Button) view.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFilterSelection("color_filter", colorFilter.getText().toString());
                listener.onFilterSelection("image_size", imageSize.getText().toString());
                listener.onFilterSelection("image_type", imageType.getText().toString());
                listener.onFilterSelection("site_filter", siteFilter.getText().toString());
                getDialog().dismiss();
            }
        });

        Button btCancel = (Button) view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
