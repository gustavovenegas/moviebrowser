package com.gustavovenegas.moviebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class PopularMoviesActivity extends AppCompatActivity {
    private TmdbDataSource tmdbDataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listView = (ListView) findViewById(R.id.movie_list);
        tmdbDataSource = new TmdbDataSource(this, TmdbDataSource.POPULAR_MODE, listView);

        listView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public void loadMore() {
                tmdbDataSource.getPopularMovies();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        tmdbDataSource.getTmdbConfig();
        tmdbDataSource.getPopularMovies();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            Intent intent = new Intent(PopularMoviesActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
