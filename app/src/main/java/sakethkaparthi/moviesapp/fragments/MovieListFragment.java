package sakethkaparthi.moviesapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.activities.ContainerActivity;
import sakethkaparthi.moviesapp.adapters.MovieAdapter;
import sakethkaparthi.moviesapp.models.Movie;
import sakethkaparthi.moviesapp.models.MoviesModel;
import sakethkaparthi.moviesapp.network.APIClient;

public class MovieListFragment extends Fragment {
    MovieAdapter movieAdapter;
    ProgressDialog progressDialog;
    GridView gridview;

    public static MovieListFragment newInstance() {


        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.sort_menu, menu);
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridview = (GridView) view.findViewById(R.id.grid_view);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Reviews");
        sortByPopular();
    }

    void sortByPopular() {
        APIClient.getAPI().getPopularMovies(new Callback<MoviesModel>() {
            @Override
            public void success(MoviesModel moviesModel, Response response) {
                progressDialog.dismiss();
                ArrayList<Movie> movies = new ArrayList<Movie>();
                movies.addAll(moviesModel.getResults());
                movieAdapter = new MovieAdapter(getContext(), movies);
                gridview.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        MovieDetailsFragment.movie = movieAdapter.getItem(position);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                        fragmentTransaction.replace(R.id.frame_container, MovieDetailsFragment.newInstance());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
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
                movieAdapter = new MovieAdapter(getContext(), movies);
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

    @Override
    public void onResume() {
        super.onResume();
        ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Movies");
    }
}
