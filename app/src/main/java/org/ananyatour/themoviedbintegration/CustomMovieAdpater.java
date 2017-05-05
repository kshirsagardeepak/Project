package org.ananyatour.themoviedbintegration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by user on 12/16/2015.
 */
public class CustomMovieAdpater extends BaseAdapter {
    private Context activity;
    private ArrayList<Movie> data;
    private static LayoutInflater inflater = null;
    public ListView res;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ViewHolder holder;

    public CustomMovieAdpater(Context activity, ArrayList<Movie> listItems,
                              ListView resLocal) {
        this.activity = activity;
        data = listItems;
        res = resLocal;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*********
     * Create a holder Class to contain inflated xml file elements
     *********/
    public static class ViewHolder {
        public TextView movie_name_txt, id_txt, release_date_txt, is_adult_txt;
        public NetworkImageView movie_poster_img;
        public ImageButton play_button;
    }


    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        // final ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.movie_list_view_item, null);

            holder = new ViewHolder();
            holder.id_txt = (TextView) vi.findViewById(R.id.id_txt);
            holder.movie_name_txt = (TextView) vi.findViewById(R.id.movie_name_txt);
            holder.release_date_txt = (TextView) vi.findViewById(R.id.release_date_txt);
            holder.is_adult_txt = (TextView) vi.findViewById(R.id.is_adult_txt);
            holder.movie_poster_img = (NetworkImageView) vi.findViewById(R.id.movie_poster_img);
          /*  holder.feedImageView = (FeedImageView) vi.findViewById(R.id.feedImage1);*/

            /************ Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }

        if (data.size() <= 0) {

        } else {
            Movie movie = data.get(position);
            String posterPathStr = CommonUtil.MOVIE_IMAGE_BASE_URL + movie.getPosterPath();

            holder.id_txt.setText(movie.getId());
            holder.movie_name_txt.setText(movie.getTitle());
            holder.release_date_txt.setText(movie.getReleaseDate());

            if (movie.getAdult().equalsIgnoreCase("true")) {
                holder.is_adult_txt.setText("(A)");
            } else if (movie.getAdult().equalsIgnoreCase("false")) {
                holder.is_adult_txt.setText("(U/A)");
            }

            holder.movie_poster_img.setImageUrl(posterPathStr, imageLoader);
            // Feed image
           /* if (posterPathStr != null || posterPathStr != "") {
                holder.movie_poster_img.setImageUrl(posterPathStr, imageLoader);
                holder.movie_poster_img.setVisibility(View.VISIBLE);
                holder.movie_poster_img
                        .setResponseObserver(new FeedImageView.ResponseObserver() {
                            @Override
                            public void onError() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
            }*/
        }
        return vi;
    }
}
