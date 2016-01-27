package sakethkaparthi.moviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by saketh on 27/1/16.
 */
public class MovieAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String > movieNames, posterLinks;
    public MovieAdapter(Context mContext, ArrayList<String> names, ArrayList<String> images){
        this.mContext = mContext;
        movieNames = names;
        posterLinks = images;
    }
    @Override
    public int getCount() {
        return movieNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_grid_item,parent,false);
        }
        ImageView imageView  = (ImageView)convertView.findViewById(R.id.movie_poster);
        TextView textView = (TextView)convertView.findViewById(R.id.movie_title);
        Picasso.with(mContext).load(posterLinks.get(position)).into(imageView);
        textView.setText(movieNames.get(position));
        return convertView;
    }
}
