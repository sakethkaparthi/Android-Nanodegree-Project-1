package sakethkaparthi.moviesapp.activities;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.stetho.Stetho;

import sakethkaparthi.moviesapp.R;
import sakethkaparthi.moviesapp.database.MoviesProvider;

public class ContainerActivity extends AppCompatActivity {

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
        ContentValues values = new ContentValues();
        values.put(MoviesProvider.NAME,
                "Death note");

        values.put(MoviesProvider.POSTER,
                "http://www.google.com");
        values.put(MoviesProvider.RATING,
                "5.0");
        values.put(MoviesProvider.SYNOPSIS,
                "Best thing to ever exist");
        values.put(MoviesProvider.RELEASE_DATE,
                "2005 something");
        Uri uri = getContentResolver().insert(
                MoviesProvider.CONTENT_URI, values);
    }
}
