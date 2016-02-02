package sakethkaparthi.moviesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.adapters.TrailersAdapter;
import sakethkaparthi.moviesapp.models.Movie;
import sakethkaparthi.moviesapp.models.Trailer;
import sakethkaparthi.moviesapp.models.TrailersModel;
import sakethkaparthi.moviesapp.network.APIClient;
import sakethkaparthi.moviesapp.resources.Constants;

public class MovieDetailsFragment extends Fragment {
    public static Movie movie;
    ListView trailersList;
    TrailersAdapter adapter;

    public static MovieDetailsFragment newInstance() {

        MovieDetailsFragment fragment = new MovieDetailsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        ImageView posterImageView = (ImageView) view.findViewById(R.id.movie_poster);
        TextView movieTitleTextView = (TextView) view.findViewById(R.id.movie_title);
        TextView movieRatingTextView = (TextView) view.findViewById(R.id.rating_text_view);
        TextView movieReleaseTextView = (TextView) view.findViewById(R.id.release_date_text_view);
        TextView movieSynopsisTextView = (TextView) view.findViewById(R.id.plot_synopsis_text_view);
        trailersList = (ListView) view.findViewById(R.id.trailers_list_view);
        Picasso.with(getContext()).load(Constants.IMAGE_BASE_URL + movie.getPosterPath())
                .into(posterImageView);
        movieTitleTextView.setText(movie.getTitle());
        movieRatingTextView.setText(movie.getVoteAverage() + "");
        movieReleaseTextView.setText(movie.getReleaseDate());
        movieSynopsisTextView.setText(movie.getOverview());
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
    }

}
