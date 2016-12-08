package com.gustavovenegas.moviebrowser;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by gustavovenegas on 08.12.16.
 * Data source for importing objetcs from TMDB
 */

public class TmdbDataSource {
    private static final String API_KEY = "93aea0c77bc168d8bbce3918cefefa45";
    private static final String LANGUAGE = "en-US";
    private static final boolean EXCLUDE_ADULT = false;
    private static final String SORT_BY = "popularity.desc";
    private int page = 1;
    private Context context;
    private String postersUrl;
    private List<MovieDb> movieList;
    private ListView listView;
    private NetworkManager networkManager;
    private MoviesAdapter adapter;
    private int mode;
    public static final int POPULAR_MODE = 0;
    public static final int SEARCH_MODE = 1;


    public TmdbDataSource(Context context, int mode, ListView listView){
        this.context = context;
        this.mode = mode;
        this.listView = listView;
        this.networkManager = new NetworkManager(context);
    }

    public void resetMovieList(){
        if(movieList != null) {
            movieList.clear();
        }
        page = 1;
        listView.setSelection(0);
    }


    public void getTmdbConfig (){
        if(networkManager.networkAvailable()){
            new downloadConfigTask().execute();
        } else {
            Toast toast = Toast.makeText(context, R.string.error_config_not_downloaded, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class downloadConfigTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... unused) {
            TmdbConfiguration configAPI = new TmdbApi(API_KEY).getConfiguration();
            return configAPI.getBaseUrl() + configAPI.getPosterSizes().get(2);
        }

        @Override
        protected void onPostExecute(String url) {
            postersUrl = url;
        }
    }

    private class downloadMoviesPageTask extends AsyncTask<String, Void, MovieResultsPage> {

        @Override
        protected MovieResultsPage doInBackground(String... name) {
            //TODO show loading
            switch (mode){
                case POPULAR_MODE:
                    TmdbDiscover discover = new TmdbApi(API_KEY).getDiscover();

                    Discover discoverFilter = new Discover();
                    discoverFilter.page(page);
                    discoverFilter.sortBy(SORT_BY);
                    discoverFilter.language(LANGUAGE);
                    discoverFilter.includeAdult(EXCLUDE_ADULT);

                    return discover.getDiscover(discoverFilter);
                case SEARCH_MODE:
                    TmdbSearch search = new TmdbApi(API_KEY).getSearch();
                    return search.searchMovie(name[0], null, LANGUAGE, EXCLUDE_ADULT, page);
            }
            return null;
        }

        @Override
        protected void onPostExecute(MovieResultsPage result) {
            List<MovieDb> newMovies = result.getResults();
            if (newMovies == null || newMovies.size() == 0) {
                //display error
            } else{
                if (movieList == null){
                    movieList = newMovies;
                } else {
                    movieList.addAll(newMovies);
                }

                if(result.getTotalPages()>page) {
                    page++;
                } else {
                    page = 0;
                }

                View topView = listView.getChildAt(0);
                int currentOffset = (topView == null) ? 0 : (topView.getTop() - listView.getPaddingTop());
                int currentItem = listView.getFirstVisiblePosition();

                adapter = new MoviesAdapter(context, movieList, postersUrl);
                listView.setAdapter(adapter);
                listView.setSelectionFromTop(currentItem, currentOffset);
            }
        }
    }

    public void searchMovies(String movieName) {
        if(networkManager.networkAvailable()){
            if(page>0) {
                new downloadMoviesPageTask().execute(movieName);
            } else {
                Toast toast = Toast.makeText(context, R.string.error_no_more_movies, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(context, R.string.error_no_internet_connection, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void getPopularMovies(){
        if(networkManager.networkAvailable()){
            if(page>0) {
                new downloadMoviesPageTask().execute("empty");
            } else {
                Toast toast = Toast.makeText(context, R.string.error_no_more_movies, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(context, R.string.error_no_internet_connection, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
