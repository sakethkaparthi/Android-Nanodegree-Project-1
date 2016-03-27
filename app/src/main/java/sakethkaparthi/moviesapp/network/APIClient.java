package sakethkaparthi.moviesapp.network;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import sakethkaparthi.moviesapp.models.MoviesModel;
import sakethkaparthi.moviesapp.models.ReviewsModel;
import sakethkaparthi.moviesapp.models.TrailersModel;
import sakethkaparthi.moviesapp.resources.Constants;

/**
 * Created by saketh on 27/1/16.
 */
public class APIClient {
    private static final String BASE_URL = Constants.BASE_URL;
    private static APIInterface apiInterface = null;

    public static APIInterface getAPI() {
        if (apiInterface == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(BASE_URL)
                    .build();
            apiInterface = restAdapter.create(APIInterface.class);
        }
        return apiInterface;
    }

    public interface APIInterface {
        @GET("/discover/movie?sort_by=popularity.desc&api_key=" + Constants.API_KEY)
        void getPopularMovies(Callback<MoviesModel> moviesCallback);

        @GET("/discover/movie?sort_by=vote_average.desc&api_key=" + Constants.API_KEY)
        void getTopRatedMovies(Callback<MoviesModel> moviesCallback);

        @GET("/movie/{id}/videos?api_key=" + Constants.API_KEY)
        void getMovieTrailers(@Path("id") String id, Callback<TrailersModel> trailersModelCallback);

        @GET("/movie/{id}/reviews?api_key="+Constants.API_KEY)
        void getMovieReviews(@Path("id") String id, Callback<ReviewsModel> reviewsModelCallback);
    }
}
