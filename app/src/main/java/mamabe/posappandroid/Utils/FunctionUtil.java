package mamabe.posappandroid.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;


public class FunctionUtil {
    private static NetworkInfo networkInfo;

    // for time
    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;
    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;
    public final static long ONE_DAY = ONE_HOUR * 24;

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for(;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        networkInfo = connectivity.getActiveNetworkInfo();
        // NetworkInfo info

        if (networkInfo != null && networkInfo.isConnected()
                && networkInfo.isAvailable()) {
            return true;
        }
        return false;

    }

    public static String readFileFromAssets(String filename,Context context)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine = reader.readLine();
            while(mLine != null)
            {
                // process line
                sb.append(mLine);
                mLine = reader.readLine();
            }

            reader.close();
        } catch(IOException e)
        {
            // log the exception
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static int getListViewHeightBasedOnChildrenWithMaxSize(ListView listView,
                                                                  int maxHeightSize) {
        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return 0;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i,
                    (View) listView.getChildAt(i), listView);
            listItem.measure(desiredWidth, 0);
            totalHeight += listItem.getMeasuredHeight();
        }


        int finalHeight = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        if(finalHeight > maxHeightSize){
            finalHeight = maxHeightSize;
        }

        return finalHeight;
    }

    public static Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();

        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        view.draw(canvas);
        return returnedBitmap;
    }

    public static Uri getUriFromBitmap(Context ctx, Bitmap bmp) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(ctx.getContentResolver(), bmp, "Title", null);

        return Uri.parse(path);
    }

    public static boolean isContentUri(Uri uri) {
        return uri.toString().contains("content://");
    }

    public static void deleteFile(Context ctx, Uri uri) {
        try {
            ctx.getContentResolver().delete(uri, null, null);
        }
        catch(Exception ignored) {
            deleteFile(ctx, uri.getPath());
        }
    }

    public static void deleteFile(Context ctx, String path) {
        try {
            File file = new File(path);

            if (file.exists()) {
                file.delete();
                refreshGallery(ctx);
            }
        }
        catch(Exception ignored) {}
    }

    public static void refreshGallery(Context ctx) {
        // for < KITKAT API 14
//        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
//        Uri uri = Uri.parse("file://" +  Environment.getExternalStorageDirectory());
//        sendBroadcast(intent, uri);

        String dir = Environment.getExternalStorageDirectory().toString();
        MediaScannerConnection.scanFile(ctx, new String[]{dir}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {

            }
        });
    }

    // convert timestamp to human readable format
    public static String formatTimeForChat(long timestamp) {
        String format;
        String formatted;

        long timestampMillis = timestamp * 1000;
        long differentMillis = System.currentTimeMillis() - timestampMillis;
        Calendar differentCal = Calendar.getInstance();
        differentCal.setTimeInMillis(differentMillis);

        // if more than 1 day
        int calendar_date = differentCal.get(Calendar.DATE);
        if(calendar_date > 1)
            formatted = DateFormat.format("MMM dd", timestampMillis).toString();
        else
            formatted = DateFormat.format("HH:mm", timestampMillis).toString();

        return formatted;
    }

    public static int convertToPixel(Context context, int dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dps * scale + 0.5f);
    }

    public static void hideSoftKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if(currentFocus != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
        }
    }

    // method for setting the height of the ListView dynamically
    // Hack to fix the issue of not showing all the items of the ListView when placed inside a ScrollView
    public static void maintainDynamicListHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;

        for(int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if(i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    // method to get screen width size
    public static Point getDeviceSize(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        Point size = new Point();

        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        size.set(metrics.widthPixels, metrics.heightPixels);

        return size;
    }

    // prompt user to rate app in playstore
    public static void openPlayStore(Activity activity) {
        final Uri uri = Uri.parse("market://details?id=" + activity.getApplicationContext().getPackageName());
        final Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);

        if(activity.getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0)
            activity.startActivity(rateAppIntent);
        else
            Toast.makeText(activity, "Cannot open Google PlayStore", Toast.LENGTH_LONG).show();
    }

    // prompt user with share dialog
    public static void openShareToApps(Context context, String subject, String title, String textToShare) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        context.startActivity(Intent.createChooser(sharingIntent, title));
    }
}
