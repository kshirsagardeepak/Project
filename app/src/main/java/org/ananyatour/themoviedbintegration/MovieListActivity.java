package org.ananyatour.themoviedbintegration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MovieListActivity extends AppCompatActivity {
    ListView movieListView;
    String getUpcomingMovies = "https://api.themoviedb.org/3/movie/upcoming?api_key=85b9e282273de69f7ab12e1f829f6172";
    ArrayList<Movie> movieArrayList;
    public ListView res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.custom_action_bar, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.title);
        textviewTitle.setText("Upcoming Movies");
        actionBar.setCustomView(viewActionBar, params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(true);
        movieListView = (ListView) findViewById(R.id.movie_list_view);


        new GetUpcomingMovies().execute();
       /* }else {
           Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_SHORT).show();
        }*/

        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long rowId) {

                String movieId = ((TextView) view.findViewById(R.id.id_txt)).getText().toString();
                Intent intent = new Intent(MovieListActivity.this, MovieDetailActivity.class);
                intent.putExtra(CommonUtil.TAG_MOVIE_ID, movieId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_info) {
            Intent intent = new Intent(MovieListActivity.this, InformationActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetUpcomingMovies extends RemoteDataImporter {


        public GetUpcomingMovies() {
            super(getApplicationContext(), getApplicationContext());
        }

        @Override
        protected FrameLayout getFrameLayout() {
            CommonUtil.showProgressDialog(MovieListActivity.this);
            return null;
        }

        @Override
        public String getURL() {
            return getUpcomingMovies;
        }

        @Override
        public void processJSON(JSONObject jsonObj) throws JSONException {
            JSONArray arrayAttendee = jsonObj
                    .getJSONArray("results");
            if (arrayAttendee != null) {
                movieArrayList = new ArrayList<Movie>();
                for (int j = 0; j < arrayAttendee.length(); j++) {
                    JSONObject obj = arrayAttendee.getJSONObject(j);
                    Movie movie = new Movie();

                    movie.setPosterPath(obj.getString(CommonUtil.TAG_POSTER_PATH));
                    movie.setAdult(obj.getString(CommonUtil.TAG_ADULT));
                    movie.setId(obj.getString(CommonUtil.TAG_ID));
                    movie.setBackdropPath(obj.getString(CommonUtil.TAG_BACKDROP_PATH));
                    movie.setOriginalTitle(obj.getString(CommonUtil.TAG_ORIGINAL_TITLE));
                    movie.setTitle(obj.getString(CommonUtil.TAG_TITLE));
                    try {
                        movie.setReleaseDate(CommonUtil.convertStringDateToString("yyyy-MM-dd", "dd/MMM/yyyy", obj.getString(CommonUtil.TAG_RELEASE_DATE)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    movie.setOverview(obj.getString(CommonUtil.TAG_OVERVIEW));
                    movie.setPopularity(obj.getString(CommonUtil.TAG_POPULARITY));
                    movie.setVideo(obj.getString(CommonUtil.TAG_VIDEO));
                    movie.setVoteAverage(obj.getString(CommonUtil.TAG_VOTE_AVERAGE));
                    movie.setVoteCount(obj.getString(CommonUtil.TAG_VOTE_COUNT));

                    movieArrayList.add(movie);
                }
            }
        }

        @Override
        public void processJSONArray(JSONArray jsonObj) throws JSONException {

        }

        @Override
        public void processPostExecute() {
            CommonUtil.hideProgressDialog();
            if (movieArrayList != null) {
                if (movieArrayList.size() > 0) {
                    CustomMovieAdapter customMovieAdpater = new CustomMovieAdapter(MovieListActivity.this, movieArrayList, movieListView);
                    movieListView.setAdapter(customMovieAdpater);
                }
            }
        }
    }

    class CustomMovieAdapter extends BaseAdapter {
        private Context activity;
        ArrayList<Movie> data;
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        CustomMovieAdapter(Context activity, ArrayList<Movie> listItems,
                           ListView resLocal) {
            this.activity = activity;
            data = listItems;
            res = resLocal;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            //  return data.get(position);
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            public TextView movie_name_txt, id_txt, release_date_txt, is_adult_txt;
            public NetworkImageView movie_poster_img;
            public ImageButton play_button;

            ViewHolder(View view) {
                id_txt = (TextView) view.findViewById(R.id.id_txt);
                movie_name_txt = (TextView) view.findViewById(R.id.movie_name_txt);
                release_date_txt = (TextView) view.findViewById(R.id.release_date_txt);
                is_adult_txt = (TextView) view.findViewById(R.id.is_adult_txt);
                movie_poster_img = (NetworkImageView) view.findViewById(R.id.movie_poster_img);
                view.setTag(this);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            ViewHolder viewHolder = convertView == null
                    ? new ViewHolder(convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_item, parent, false))
                    : (ViewHolder) convertView.getTag();

            Movie movie = data.get(position);
            String posterPathStr = CommonUtil.MOVIE_IMAGE_BASE_URL + movie.getPosterPath();

            viewHolder.id_txt.setText(movie.getId());
            viewHolder.movie_name_txt.setText(movie.getTitle());
            viewHolder.release_date_txt.setText(movie.getReleaseDate());

            if (movie.getAdult().equalsIgnoreCase("true")) {
                viewHolder.is_adult_txt.setText("(A)");
            } else if (movie.getAdult().equalsIgnoreCase("false")) {
                viewHolder.is_adult_txt.setText("(U/A)");
            }

            viewHolder.movie_poster_img.setImageUrl(posterPathStr, imageLoader);
            return convertView;
        }

    }
}
