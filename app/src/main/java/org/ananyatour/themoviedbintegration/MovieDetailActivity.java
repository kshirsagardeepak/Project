package org.ananyatour.themoviedbintegration;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by USer on 18-04-2017.
 */

public class MovieDetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    SliderLayout sliderLayout;
    HashMap<String, String> Hash_file_maps;
    ArrayList<HashMap<String, String>> movieImagesArrayList;
    String originalMovieTitleStr = null, movieOverviewStr = null,popularityStr=null;
    TextView titleTxt, overviewTxt;
    RatingBar popularityRatingBar;
    ActionBar actionBar;
    LinearLayout movie_detail_layout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_layout);

        actionBar = getSupportActionBar();

        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        movie_detail_layout= (LinearLayout) findViewById(R.id.movie_detail_layout);
        titleTxt = (TextView) findViewById(R.id.titleTxt);
        overviewTxt = (TextView) findViewById(R.id.overviewTxt);

        popularityRatingBar = (RatingBar) findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra(CommonUtil.TAG_MOVIE_ID);
        if (movieId != null)
            new GetMovieImages(movieId).execute();

        new GetMovieDetail(movieId).execute();

    }

    @Override
    protected void onStop() {

        sliderLayout.stopAutoCycle();

        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private class GetMovieImages extends RemoteDataImporter {
        String movieId;

        public GetMovieImages(String movieId) {
            super(getApplicationContext(), getApplicationContext());
            this.movieId = movieId;
        }

        @Override
        protected FrameLayout getFrameLayout() {
            CommonUtil.showProgressDialog(MovieDetailActivity.this);
            return null;
        }

        @Override
        public String getURL() {
            String getMoviesImagesAPI = "https://api.themoviedb.org/3/movie/" + movieId + "/images?api_key=85b9e282273de69f7ab12e1f829f6172";
            return getMoviesImagesAPI;
        }

        @Override
        public void processJSON(JSONObject jsonObj) throws JSONException {
            JSONArray imageJsonArray = jsonObj
                    .getJSONArray("backdrops");
            if (imageJsonArray != null) {
                movieImagesArrayList = new ArrayList<HashMap<String, String>>();
                for (int j = 0; j < 5; j++) {
                    JSONObject imageJsonObject = imageJsonArray.getJSONObject(j);

                    String imagePath = imageJsonObject.getString("file_path");
                    HashMap<String, String> imageHashMap = new HashMap<>();
                    imageHashMap.put(CommonUtil.TAG_IMAGE_PATH, CommonUtil.MOVIE_IMAGE_BASE_URL + imagePath);

                    movieImagesArrayList.add(imageHashMap);
                }
            }
        }

        @Override
        public void processJSONArray(JSONArray jsonObj) throws JSONException {

        }

        @Override
        public void processPostExecute() {
            CommonUtil.hideProgressDialog();
            sliderLayout.setVisibility(View.VISIBLE);
            if (movieImagesArrayList != null) {
                if (movieImagesArrayList.size() > 0) {
                    for (int i = 0; i < movieImagesArrayList.size(); i++) {
                        HashMap<String, String> imageHashMap = movieImagesArrayList.get(i);
                        TextSliderView textSliderView = new TextSliderView(MovieDetailActivity.this);
                        textSliderView
                                .image(imageHashMap.get(CommonUtil.TAG_IMAGE_PATH))
                                .setScaleType(BaseSliderView.ScaleType.Fit);

                        sliderLayout.addSlider(textSliderView);
                    }
                    sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
                    sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    // sliderLayout.setCustomAnimation(new DescriptionAnimation());
                    sliderLayout.setDuration(3000);
                }
            }
        }
    }

    private class GetMovieDetail extends RemoteDataImporter {
        String movieId;

        public GetMovieDetail(String movieId) {
            super(getApplicationContext(), getApplicationContext());
            this.movieId = movieId;
        }

        @Override
        protected FrameLayout getFrameLayout() {
            CommonUtil.showProgressDialog(MovieDetailActivity.this);
            return null;
        }

        @Override
        public String getURL() {
            String getMoviesDetailAPI = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=85b9e282273de69f7ab12e1f829f6172";
            return getMoviesDetailAPI;
        }

        @Override
        public void processJSON(JSONObject jsonObj) throws JSONException {
            originalMovieTitleStr = jsonObj
                    .getString("title");
            movieOverviewStr = jsonObj
                    .getString("overview");
            popularityStr = jsonObj
                    .getString("popularity");
        }

        @Override
        public void processJSONArray(JSONArray jsonObj) throws JSONException {

        }

        @Override
        public void processPostExecute() {
            CommonUtil.hideProgressDialog();
            movie_detail_layout.setVisibility(View.VISIBLE);
            if (originalMovieTitleStr != null && movieOverviewStr != null) {
                titleTxt.setText(originalMovieTitleStr);
                overviewTxt.setText(movieOverviewStr);
                popularityRatingBar.setRating(Float.parseFloat(popularityStr) / 2);
                //Customize the ActionBar

            //    actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_background));//line under the action bar
                View viewActionBar = getLayoutInflater().inflate(R.layout.custom_action_bar, null);
                ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER);
                TextView textviewTitle = (TextView) viewActionBar.findViewById(R.id.title);
                textviewTitle.setText(originalMovieTitleStr);
                actionBar.setCustomView(viewActionBar, params);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }
    }
}
