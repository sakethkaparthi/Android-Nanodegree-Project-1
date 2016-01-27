package sakethkaparthi.moviesapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieListActivity extends AppCompatActivity {
    MovieAdapter movieAdapter;
    ProgressDialog progressDialog;
    GridView gridview;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sort_by_popular) {
            sortByPopular();
            return true;
        } else if (item.getItemId() == R.id.sort_by_ratings) {
            sortByRatings();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        gridview = (GridView) findViewById(R.id.grid_view);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        sortByPopular();
    }

    void sortByPopular() {
        APIClient.getAPI().getPopularMovies(new Callback<MoviesModel>() {
            @Override
            public void success(MoviesModel moviesModel, Response response) {
                progressDialog.dismiss();
                ArrayList<Movie> movies = new ArrayList<Movie>();
                movies.addAll(moviesModel.getResults());
                movieAdapter = new MovieAdapter(getApplicationContext(), movies);
                gridview.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
    }

    void sortByRatings() {
        APIClient.getAPI().getTopRatedMovies(new Callback<MoviesModel>() {
            @Override
            public void success(MoviesModel moviesModel, Response response) {
                progressDialog.dismiss();
                Log.d("apicall", "success");
                ArrayList<Movie> movies = new ArrayList<Movie>();
                movies.addAll(moviesModel.getResults());
                Log.d("apicall", movies.size() + "");
                movieAdapter = new MovieAdapter(getApplicationContext(), movies);
                gridview.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
    }
}
