package com.gustavovenegas.moviebrowser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by gustavovenegas on 08.12.16.
 * Adapter to inflate results into ListView
 */

public class MoviesAdapter extends ArrayAdapter<MovieDb> {
    private final Context context;
    private final List<MovieDb> movieList;
    private final String postersUrl;

    public MoviesAdapter(Context context, List<MovieDb> movieList, String postersUrl) {
        super(context, R.layout.movie_row, movieList);
        this.context = context;
        this.movieList = movieList;
        this.postersUrl = postersUrl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.movie_row, parent, false);

        TextView movieTitle = (TextView) rowView.findViewById(R.id.movie_title);
        movieTitle.setText(movieList.get(position).getOriginalTitle());

        TextView year = (TextView) rowView.findViewById(R.id.year);
        String releaseDate = movieList.get(position).getReleaseDate();
        String releaseYear;
        if(releaseDate.length()>=4){
            releaseYear = releaseDate.substring(0,4);
        } else {
            releaseYear = context.getResources().getString(R.string.not_available);
        }
        year.setText(releaseYear);

        TextView description = (TextView) rowView.findViewById(R.id.description);
        description.setText(movieList.get(position).getOverview());

        ImageView poster = (ImageView) rowView.findViewById(R.id.poster);
        String imageURL = postersUrl+movieList.get(position).getPosterPath();
        Picasso.with(context).load(imageURL).placeholder(R.drawable.ic_movie).into(poster);

        return rowView;
    }
}
