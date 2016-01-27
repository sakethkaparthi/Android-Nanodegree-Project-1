package sakethkaparthi.moviesapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.models.Movie;
import sakethkaparthi.moviesapp.resources.Constants;

public class MovieDetailsActivity extends AppCompatActivity {
    public static Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ImageView posterImageView =  (ImageView)findViewById(R.id.movie_poster);
        TextView movieTitleTextView = (TextView)findViewById(R.id.movie_title);
        TextView movieRatingTextView = (TextView)findViewById(R.id.rating_text_view);
        TextView movieReleaseTextView = (TextView) findViewById(R.id.release_date_text_view);
        TextView movieSynopsisTextView = (TextView) findViewById(R.id.plot_synopsis_text_view);
        Picasso.with(this).load(Constants.IMAGE_BASE_URL+movie.getPosterPath())
                .into(posterImageView);
        movieTitleTextView.setText(movie.getTitle());
        movieRatingTextView.setText(movie.getVoteAverage()+"");
        movieReleaseTextView.setText(movie.getReleaseDate());
        movieSynopsisTextView.setText(movie.getOverview());
    }
}
