package sakethkaparthi.moviesapp.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import chipset.potato.Potato;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.activities.ContainerActivity;
import sakethkaparthi.moviesapp.adapters.TrailersAdapter;
import sakethkaparthi.moviesapp.database.MoviesProvider;
import sakethkaparthi.moviesapp.models.Movie;
import sakethkaparthi.moviesapp.models.Trailer;
import sakethkaparthi.moviesapp.models.TrailersModel;
import sakethkaparthi.moviesapp.network.APIClient;
import sakethkaparthi.moviesapp.resources.Constants;

public class MovieDetailsFragment extends Fragment {
    public static Movie movie;
    ListView trailersList;
    TrailersAdapter adapter;
    boolean favourite = false;

    public static MovieDetailsFragment newInstance() {

        MovieDetailsFragment fragment = new MovieDetailsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (movie != null)
            return inflater.inflate(R.layout.fragment_movie_details, container, false);
        else
            return inflater.inflate(R.layout.select_movie, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_share);
        inflater.inflate(R.menu.movie_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            if (movie != null) {
                if (Potato.potate(getContext()).Utils().isInternetConnected()) {
                    try {
                        String url = Constants.YOUTUBE_BASE + adapter.getItem(0).getKey();
                        Toast.makeText(getContext(), "Share the first Trailer!", Toast.LENGTH_SHORT).show();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "This movie has no trailers", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please connect to the internet to get trailers", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please select a movie first", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        if (((ContainerActivity) getActivity()).getSupportActionBar() != null)
            ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Movie Details");
        if (movie != null) {
            ImageView posterImageView = (ImageView) view.findViewById(R.id.movie_poster);
            final ImageView favouriteImageView = (ImageView) view.findViewById(R.id.favourite_image);
            TextView movieTitleTextView = (TextView) view.findViewById(R.id.movie_title);
            TextView movieRatingTextView = (TextView) view.findViewById(R.id.rating_text_view);
            TextView movieReleaseTextView = (TextView) view.findViewById(R.id.release_date_text_view);
            TextView movieSynopsisTextView = (TextView) view.findViewById(R.id.plot_synopsis_text_view);
            for (Movie movie1 : getFavouriteMovies()) {
                if (movie1.getId() == movie.getId()) {
                    favouriteImageView.setImageResource(R.drawable.fill_01);
                    favourite = true;
                }
            }
            favouriteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!favourite) {
                        ContentValues values = new ContentValues();
                        values.put(MoviesProvider.ID, movie.getId());
                        values.put(MoviesProvider.NAME, movie.getTitle());
                        values.put(MoviesProvider.POSTER, movie.getPosterPath());
                        values.put(MoviesProvider.SYNOPSIS, movie.getOverview());
                        values.put(MoviesProvider.RELEASE_DATE, movie.getReleaseDate());
                        values.put(MoviesProvider.RATING, movie.getVoteAverage() + "");
                        getActivity().getContentResolver().insert(MoviesProvider.CONTENT_URI, values);
                        favouriteImageView.setImageResource(R.drawable.fill_01);
                        favourite = true;
                        Toast.makeText(getContext(), "Added to favourites!", Toast.LENGTH_SHORT).show();
                    } else {
                        getActivity().getContentResolver().delete(MoviesProvider.CONTENT_URI, MoviesProvider.ID + " = " + movie.getId(), null);
                        favourite = false;
                        favouriteImageView.setImageResource(R.drawable.empty_01);
                        Toast.makeText(getContext(), "Removed from favourites!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            AppCompatButton appCompatButton = (AppCompatButton) view.findViewById(R.id.reviews_button);
            trailersList = (ListView) view.findViewById(R.id.trailers_list_view);
            Glide.with(getContext()).load(Constants.IMAGE_BASE_URL + movie.getPosterPath())
                    .into(posterImageView);
            movieTitleTextView.setText(movie.getTitle());
            movieRatingTextView.setText(movie.getVoteAverage() + "");
            movieReleaseTextView.setText(movie.getReleaseDate());
            movieSynopsisTextView.setText(movie.getOverview());
            appCompatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    if (getActivity().findViewById(R.id.details_container) == null) {
                        fragmentTransaction.replace(R.id.frame_container, ReviewsFragment.newInstance(movie.getId() + ""), "Reviews");
                    } else {
                        fragmentTransaction.replace(R.id.details_container, ReviewsFragment.newInstance(movie.getId() + ""), "Reviews");
                    }
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            if (Potato.potate(getContext()).Utils().isInternetConnected()) {
                progressDialog.show();
                APIClient.getAPI().getMovieTrailers(movie.getId() + "", new Callback<TrailersModel>() {
                    @Override
                    public void success(TrailersModel trailersModel, Response response) {
                        progressDialog.dismiss();
                        ArrayList<Trailer> trailers = new ArrayList<Trailer>();
                        trailers.addAll(trailersModel.getResults());
                        adapter = new TrailersAdapter(getActivity(), trailers);
                        trailersList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        trailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(Constants.YOUTUBE_BASE + adapter.getItem(position).getKey())));
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Please Connect to the internet to get the latest trailers", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Movie Details");
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
