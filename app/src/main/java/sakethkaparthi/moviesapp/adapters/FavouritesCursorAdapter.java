package sakethkaparthi.moviesapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.database.MoviesProvider;
import sakethkaparthi.moviesapp.resources.Constants;

/**
 * Created by saketh on 16/3/16.
 */
public class FavouritesCursorAdapter extends CursorAdapter {
    public FavouritesCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.movie_grid_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.movie_poster);
        Picasso.with(context)
                .load(Constants.IMAGE_BASE_URL + cursor.getString(cursor.getColumnIndex(MoviesProvider.POSTER)))
                .into(imageView);
    }
}
