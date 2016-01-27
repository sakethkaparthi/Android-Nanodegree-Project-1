package sakethkaparthi.moviesapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieListActivity extends AppCompatActivity {
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        final GridView gridview = (GridView) findViewById(R.id.grid_view);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        APIClient.getAPI().getPopularMovies(new Callback<MoviesModel>() {
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
