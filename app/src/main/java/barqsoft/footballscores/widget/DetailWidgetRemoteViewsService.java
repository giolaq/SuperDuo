package barqsoft.footballscores.widget;

/**
 * Created by joaobiriba on 01/10/2015.
 */

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;

/**
 * RemoteViewsService controlling the data being shown in the scrollable weather detail widget
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();
    private static final String[] FORECAST_COLUMNS = {
            DatabaseContract.SCORES_TABLE + "." + DatabaseContract.scores_table._ID,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL
    };
    // these indices must match the projection
    static final int INDEX_SCORE_ID = 0;
    static final int INDEX_LEAGUE = 1;
    static final int INDEX_HOME = 2;
    static final int INDEX_AWAY = 3;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
//                String location = Utility.getPreferredLocation(DetailWidgetRemoteViewsService.this);
//                Uri weatherForLocationUri = WeatherContract.WeatherEntry
//                        .buildWeatherLocationWithStartDate(location, System.currentTimeMillis());
//                data = getContentResolver().query(weatherForLocationUri,
//                        FORECAST_COLUMNS,
//                        null,
//                        null,
//                        WeatherContract.WeatherEntry.COLUMN_DATE + " ASC");
                Date fragmentdate = new Date(System.currentTimeMillis() + ((-1) * 86400000));
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                String[] date = new String[1];
                date[0] = mformat.format(fragmentdate);

                Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
                data = getContentResolver().query(uri,
                        FORECAST_COLUMNS,
                        null,
                        date,
                        null);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_today_list_item);
//                int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
//                int weatherArtResourceId = Utility.getIconResourceForWeatherCondition(weatherId);
//                Bitmap weatherArtImage = null;
//                if ( !Utility.usingLocalGraphics(DetailWidgetRemoteViewsService.this) ) {
//                    String weatherArtResourceUrl = Utility.getArtUrlForWeatherCondition(
//                            DetailWidgetRemoteViewsService.this, weatherId);
//                    try {
//                        weatherArtImage = Glide.with(DetailWidgetRemoteViewsService.this)
//                                .load(weatherArtResourceUrl)
//                                .asBitmap()
//                                .error(weatherArtResourceId)
//                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
//                    } catch (InterruptedException | ExecutionException e) {
//                        Log.e(LOG_TAG, "Error retrieving large icon from " + weatherArtResourceUrl, e);
//                    }
//                }
//                String description = data.getString(INDEX_WEATHER_DESC);
//                long dateInMillis = data.getLong(INDEX_WEATHER_DATE);
//                String formattedDate = Utility.getFriendlyDayString(
//                        DetailWidgetRemoteViewsService.this, dateInMillis, false);
//                double maxTemp = data.getDouble(INDEX_WEATHER_MAX_TEMP);
//                double minTemp = data.getDouble(INDEX_WEATHER_MIN_TEMP);
//                String formattedMaxTemperature =
//                        Utility.formatTemperature(DetailWidgetRemoteViewsService.this, maxTemp);
//                String formattedMinTemperature =
//                        Utility.formatTemperature(DetailWidgetRemoteViewsService.this, minTemp);
//                if (weatherArtImage != null) {
//                    views.setImageViewBitmap(R.id.widget_icon, weatherArtImage);
//                } else {
//                    views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                    setRemoteContentDescription(views, description);
//                }
//                views.setTextViewText(R.id.widget_date, formattedDate);
//                views.setTextViewText(R.id.widget_description, description);
//                views.setTextViewText(R.id.widget_high_temperature, formattedMaxTemperature);
//                views.setTextViewText(R.id.widget_low_temperature, formattedMinTemperature);
//
//                final Intent fillInIntent = new Intent();
//                String locationSetting =
//                        Utility.getPreferredLocation(DetailWidgetRemoteViewsService.this);
//                Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//                        locationSetting,
//                        dateInMillis);
//                fillInIntent.setData(weatherUri);
//                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                String league = data.getString(INDEX_LEAGUE);
                String home = data.getString(INDEX_HOME);
                String away = data.getString(INDEX_AWAY);

                views.setTextViewText(R.id.widget_date, league);
                views.setTextViewText(R.id.widget_description, home);
                views.setTextViewText(R.id.widget_high_temperature, away);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget_icon, description);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_today_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(INDEX_SCORE_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}