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
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import chipset.potato.Potato;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.activities.ContainerActivity;
import sakethkaparthi.moviesapp.adapters.ReviewsAdapter;
import sakethkaparthi.moviesapp.models.Review;
import sakethkaparthi.moviesapp.models.ReviewsModel;
import sakethkaparthi.moviesapp.network.APIClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends Fragment {
    ListView listView;
    public static String id;

    public ReviewsFragment() {
        // Required empty public constructor
    }

    public static ReviewsFragment newInstance(String id) {
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.id = id;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.reviews_list);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Reviews");
        if (Potato.potate(getContext()).Utils().isInternetConnected()) {
            progressDialog.show();
            APIClient.getAPI().getMovieReviews(id, new Callback<ReviewsModel>() {
                @Override
                public void success(final ReviewsModel reviewsModel, Response response) {
                    ArrayList<Review> reviews = new ArrayList<Review>();
                    reviews.addAll(reviewsModel.getResults());
                    final ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getActivity(), reviews);
                    listView.setAdapter(reviewsAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(reviewsAdapter.getItem(position).getUrl())));
                        }
                    });
                    progressDialog.dismiss();
                    if (reviews.isEmpty())
                        Toast.makeText(getContext(), "This movie has no reviews", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
                }

            });
        } else {
            Toast.makeText(getContext(), "Please connect to the internet to get Reviews", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ContainerActivity) getActivity()).getSupportActionBar().setTitle("Reviews");
    }
}
