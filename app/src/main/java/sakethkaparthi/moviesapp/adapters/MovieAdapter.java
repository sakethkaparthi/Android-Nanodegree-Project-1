package sakethkaparthi.moviesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sakethkaparthi.moviesapp.models.Movie;
import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.resources.Constants;

/**
 * Created by saketh on 27/1/16.
 */
public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Movie> movieArrayList;

    public MovieAdapter(Context mContext, ArrayList<Movie> movieArrayList) {
        this.mContext = mContext;
        this.movieArrayList = movieArrayList;
    }

    @Override
    public int getCount() {
        return movieArrayList.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movieArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL+movieArrayList.get(position).getPosterPath()).into(imageView);
        return convertView;
    }
}
