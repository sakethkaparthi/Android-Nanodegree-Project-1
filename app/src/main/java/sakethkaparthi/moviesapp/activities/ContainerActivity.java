package sakethkaparthi.moviesapp.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.fragments.MovieDetailsFragment;
import sakethkaparthi.moviesapp.fragments.MovieListFragment;
import sakethkaparthi.moviesapp.fragments.ReviewsFragment;

public class ContainerActivity extends AppCompatActivity {
    public static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, MovieListFragment.newInstance())
                .commit();
        if (findViewById(R.id.details_container) != null) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.details_container);
            if (currentFragment == null)
                getSupportFragmentManager().beginTransaction().replace(R.id.details_container, MovieDetailsFragment.newInstance())
                        .commit();
            else {
                if (currentFragment instanceof MovieDetailsFragment)
                    getSupportFragmentManager().beginTransaction().replace(R.id.details_container, MovieDetailsFragment.newInstance())
                            .commit();
                else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.details_container, ReviewsFragment.newInstance(id))
                            .commit();
                }
            }
        }
    }

}
