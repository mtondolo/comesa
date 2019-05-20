package com.android.example.comesapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.android.example.comesapp.data.NewsContract;
import com.android.example.comesapp.utils.JsonUtils;
import com.android.example.comesapp.utils.NetworkUtils;

import java.net.URL;

public class EventSyncTask {

    /**
     * Performs the network request for updated event, parses the JSON from that request, and
     * inserts the event information into our ContentProvider.
     */
    synchronized public static void syncEvent(Context context) {

        try {

            /*
             * The getUrl method will return the URL that we need to get the policy JSON for the
             * policy.
             *
             */
            URL eventRequestUrl = NetworkUtils.buildEventUrl(context);

            /* Use the URL to retrieve the JSON */
            String jsonEventResponse = NetworkUtils.getResponseFromHttpUrl(eventRequestUrl);

            /* Parse the JSON into a list of event values */
            ContentValues[] eventValues = JsonUtils.getEventFromJsonStr(context, jsonEventResponse);

            /*
             * In cases where our JSON contained an error code, getEventFromJsonStr
             * would have returned null.
             */

            if (eventValues != null && eventValues.length != 0) {

                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver eventContentResolver = context.getContentResolver();

                /* Delete event data because we don't need to keep multiple events' data */
                eventContentResolver.delete(
                        NewsContract.NewsEntry.EVENT_URI,
                        null,
                        null);

                /* Insert our new event into event's ContentProvider */
                eventContentResolver.bulkInsert(
                        NewsContract.NewsEntry.EVENT_URI,
                        eventValues);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
