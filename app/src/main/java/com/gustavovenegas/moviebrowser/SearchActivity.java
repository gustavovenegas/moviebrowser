package com.gustavovenegas.moviebrowser;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;


public class SearchActivity extends AppCompatActivity {
    private TmdbDataSource tmdbDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);

        final EditText movieName = (EditText) findViewById(R.id.movie_name);
        tmdbDataSource = new TmdbDataSource(this, TmdbDataSource.SEARCH_MODE, (ListView) findViewById(R.id.movie_list));

        movieName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tmdbDataSource.resetMovieList();
                if(editable.length()>0){
                    tmdbDataSource.searchMovies(editable.toString());
                }
            }
        });

        final ListView listView = (ListView) findViewById(R.id.movie_list);
        listView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public void loadMore() {
                tmdbDataSource.searchMovies(movieName.getText().toString());
            }
        });

        movieName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            Intent intent = new Intent(SearchActivity.this, PopularMoviesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart(){
        super.onStart();
        tmdbDataSource.getTmdbConfig();
    }
}
