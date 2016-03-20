package sakethkaparthi.moviesapp.fragments;

import android.app.ProgressDialog;
import android.database.Cursor;
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
import android.widget.Toast;

import java.util.ArrayList;

import chipset.potato.Potato;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.activities.ContainerActivity;
import sakethkaparthi.moviesapp.adapters.FavouritesCursorAdapter;
import sakethkaparthi.moviesapp.adapters.MovieAdapter;
import sakethkaparthi.moviesapp.database.MoviesProvider;
import sakethkaparthi.moviesapp.models.Movie;
import sakethkaparthi.moviesapp.models.MoviesModel;
import sakethkaparthi.moviesapp.network.APIClient;

public class MovieListFragment extends Fragment {
    MovieAdapter movieAdapter;
    ProgressDialog progressDialog;
    GridView gridview;
    FavouritesCursorAdapter cursorAdapter;

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
        } else if (item.getItemId() == R.id.favourites) {
            showFavourites();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridview = (GridView) view.findViewById(R.id.grid_view);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        if (Potato.potate(getContext()).Utils().isInternetConnected()) {
            progressDialog.show();
            sortByPopular();
        } else {
            showFavourites();
            Toast.makeText(getContext(), "Please connect to the internet! ", Toast.LENGTH_SHORT).show();
        }
    }

    void sortByPopular() {
        if (Potato.potate(getContext()).Utils().isInternetConnected()) {
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
                            if (getActivity().findViewById(R.id.movie_list_fragment) == null) {
                                fragmentTransaction.replace(R.id.frame_container, MovieDetailsFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                            } else {
                                fragmentTransaction.replace(R.id.details_container, MovieDetailsFragment.newInstance());
                            }
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
        } else
            Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
    }

    void sortByRatings() {
        if (Potato.potate(getContext()).Utils().isInternetConnected()) {
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
                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            MovieDetailsFragment.movie = movieAdapter.getItem(position);
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                            if (getActivity().findViewById(R.id.movie_list_fragment) == null) {
                                fragmentTransaction.replace(R.id.frame_container, MovieDetailsFragment.newInstance());
                                fragmentTransaction.addToBackStack(null);
                            } else {
                                fragmentTransaction.replace(R.id.details_container, MovieDetailsFragment.newInstance());
                            }
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
        } else
            Toast.makeText(getContext(), "Please connect to the internet!", Toast.LENGTH_SHORT).show();
    }

    void showFavourites() {
        Cursor query = getActivity().getContentResolver().query(MoviesProvider.CONTENT_URI, null, null, null, null);
        cursorAdapter = new FavouritesCursorAdapter(getActivity(), query, 0);
        gridview.setAdapter(cursorAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetailsFragment.movie = getFavouriteMovies().get(position);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                if (getActivity().findViewById(R.id.movie_list_fragment) == null) {
                    fragmentTransaction.replace(R.id.frame_container, MovieDetailsFragment.newInstance());
                    fragmentTransaction.addToBackStack(null);
                } else {
                    fragmentTransaction.replace(R.id.details_container, MovieDetailsFragment.newInstance());
                }
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Movies");
        if (cursorAdapter != null)
            cursorAdapter.notifyDataSetChanged();
    }

    public ArrayList<Movie> getFavouriteMovies() {
        Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.CONTENT_URI, null, null, null, MoviesProvider.NAME);
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MoviesProvider.ID)));
            movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MoviesProvider.NAME)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(MoviesProvider.POSTER)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(MoviesProvider.RELEASE_DATE)));
            movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(MoviesProvider.RATING))));
            movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(MoviesProvider.SYNOPSIS)));
            movies.add(movie);
        }
        return movies;
    }
}
