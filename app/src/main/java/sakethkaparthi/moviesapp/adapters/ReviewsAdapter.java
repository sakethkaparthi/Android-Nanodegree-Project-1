package sakethkaparthi.moviesapp.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import sakethkaparthi.moviesapp.models.Review;

/**
 * Created by saketh on 2/2/16.
 */
public class ReviewsAdapter extends BaseAdapter {
    Activity mActivity;
    ArrayList<Review> reviewArrayList;
    public ReviewsAdapter(Activity activity, ArrayList<Review> reviews){
        mActivity = activity;
        reviewArrayList = reviews;
    }
    @Override
    public int getCount() {
        return reviewArrayList.size();
    }

    @Override
    public Review getItem(int position) {
        return reviewArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){

        }
        return null;
    }
}
