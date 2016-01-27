package sakethkaparthi.moviesapp;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;

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
    }
}
