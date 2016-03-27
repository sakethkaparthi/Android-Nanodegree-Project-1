package sakethkaparthi.moviesapp.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.models.Trailer;

/**
 * Created by saketh on 2/2/16.
 */
public class TrailersAdapter extends BaseAdapter {
    ArrayList<Trailer> trailerArrayList;
    Activity mActivity;

    public TrailersAdapter(Activity activity, ArrayList<Trailer> trailers) {
        mActivity = activity;
        trailerArrayList = trailers;
    }

    @Override
    public int getCount() {
        return trailerArrayList.size();
    }

    @Override
    public Trailer getItem(int position) {
        return trailerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = mActivity.getLayoutInflater().inflate(R.layout.trailer_list_item,parent,false);
        }
        ((TextView)convertView.findViewById(R.id.trailer_title)).setText(getItem(position).getName());
        return convertView;
    }
}
