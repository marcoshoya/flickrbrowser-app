package com.marcoshoya.flickrbrowser;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dc-user on 3/7/2017.
 */

class GetFlickrJsonData implements GetRawData.OnDownloadComplete {

    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> photoList = null;
    private String baseUrl;
    private String language;
    private boolean matchAll;
    private final OnDataAvailable callback;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(OnDataAvailable callback, String baseUrl, String language, boolean matchAll) {
        this.baseUrl = baseUrl;
        this.language = language;
        this.matchAll = matchAll;
        this.callback = callback;
    }

    private void executeThread(String criteria) {
        String url = createUrl(criteria, language, matchAll);

        GetRawData rawData = new GetRawData(this);
        rawData.execute(url);
    }

    private String createUrl(String criteria, String language, boolean matchAll) {

        return Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter("tags", criteria)
                .appendQueryParameter("tagmode", matchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format",  "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if (status == DownloadStatus.OK) {
            photoList = new ArrayList<>();
            try {

                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray("items");

                for (int i = 0; i < jsonArray.length(); i++) {

                    // start reading json object
                    JSONObject photo = jsonArray.getJSONObject(i);
                    JSONObject media = photo.getJSONObject("media");
                    String photoUrl = media.getString("m");
                    String link = media.getString("m").replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(
                            photo.getString("title"),
                            photo.getString("author"),
                            photo.getString("author_id"),
                            photo.getString("tags"),
                            link,
                            photoUrl
                    );

                    photoList.add(photoObject);
                    Log.d(TAG, "onDownloadComplete: " + photoObject.toString());
                }

            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: error: " + e.getMessage());
                status = DownloadStatus.FAILED;
            }
        }

        if (callback != null) {
            callback.onDataAvailable(photoList, status);
        }

    }
}
