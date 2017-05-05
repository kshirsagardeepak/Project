package org.ananyatour.themoviedbintegration;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by user on 2/8/2016.
 */
public class CommonUtil {

    public static final String MOVIE_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w300";
    public static final String TAG_MOVIE_ID= "movieId";
    public static final String TAG_IMAGE_PATH= "imagePath";

    public static final String TAG_POSTER_PATH= "poster_path";
    public static final String TAG_ADULT= "adult";
    public static final String TAG_OVERVIEW = "overview";
    public static final String TAG_RELEASE_DATE = "release_date";
    public static final String TAG_ID = "id";
    public static final String TAG_ORIGINAL_TITLE = "original_title";
    public static final String TAG_TITLE = "title";
    public static final String TAG_BACKDROP_PATH = "backdrop_path";

    public static final String TAG_POPULARITY = "popularity";
    public static final String TAG_VOTE_COUNT = "vote_count";
    public static final String TAG_VIDEO = "video";
    public static final String TAG_VOTE_AVERAGE= "vote_average";

    static ProgressDialog mProgressDialog;


    public static final String[] TOPICS = {"global"};
    public static final boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public static void errorPopup(String title, String message, int drawable, final Intent intent, final Context context) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        alertDialogBuilder.setMessage(message)
                .setTitle(title).setIcon(drawable)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (intent != null)
                            context.startActivity(intent);
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static String convertStringDateToString(String from, String to, String date) throws ParseException {
        SimpleDateFormat fromFormat = new SimpleDateFormat(from);
        fromFormat.setLenient(false);
        SimpleDateFormat toFormat = new SimpleDateFormat(to);
        toFormat.setLenient(false);
        Date fromDate = fromFormat.parse(date);
        return toFormat.format(fromDate);
    }
    /**
     * Function to convert string date format to date field
     *
     * @param from - string from format
     * @param date - date in string format
     * @return
     * @throws ParseException
     */
    public static Date convertStringDateToDate(String from, String date) throws ParseException {
        SimpleDateFormat fromFormat = new SimpleDateFormat(from);
        fromFormat.setLenient(false);

        Date fromDate = fromFormat.parse(date);
        return fromDate;
    }
    public static boolean isNull(String string) {
        return string == null || string == "" || string.equalsIgnoreCase("") || string.equalsIgnoreCase("null");
    }
    public static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

        return sdf.format(date);
    }

}
