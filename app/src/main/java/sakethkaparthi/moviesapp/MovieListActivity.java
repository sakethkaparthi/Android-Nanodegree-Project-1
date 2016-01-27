package sakethkaparthi.moviesapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        GridView gridview = (GridView) findViewById(R.id.grid_view);
        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> images = new ArrayList<>();
        names.add("The Revenant");
        names.add("Batman vs Superman");
        names.add("Deadpool");
        images.add("https://i.ytimg.com/vi/QRfj1VCg16Y/maxresdefault.jpg");
        images.add("http://fm.cnbc.com/applications/cnbc.com/resources/img/editorial/2015/04/17/102597285-Batman-vs-Superman.530x298.jpeg?v=1429289110");
        images.add("http://heroicuniverse.com/wp-content/uploads/2015/06/deadpool-will-die.png");
        MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(),names,images);
        gridview.setAdapter(movieAdapter);
    }
}
