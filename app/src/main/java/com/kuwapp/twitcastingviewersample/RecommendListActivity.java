package com.kuwapp.twitcastingviewersample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.kuwapp.twitcastingviewersample.SearchLiveMovieResponse.MovieInfo;

public class RecommendListActivity extends AppCompatActivity {

    private static final String KEY_ACCESS_TOKEN = "key_access_token";

    public static void startActivity(Context context, String accessToken) {
        Intent intent = new Intent(context, RecommendListActivity.class);
        intent.putExtra(KEY_ACCESS_TOKEN, accessToken);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_list);
        ListView listView = (ListView) findViewById(R.id.list_view);
        Adapter adapter = new Adapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String hlsUrl = adapter.getItem(i).getMovie().getHlsUrl();
            LiveActivity.startActivity(RecommendListActivity.this, hlsUrl);
        });

        fetchRecommendList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                SearchLiveMovieResponse searchLiveMovieResponse = new Gson().fromJson(response.body().charStream(), SearchLiveMovieResponse.class);
                runOnUiThread(() -> adapter.addAll(searchLiveMovieResponse.getMovieInfoList()));
            }
        });
    }

    private void fetchRecommendList(Callback callback) {
        String accessToken = getIntent().getStringExtra(KEY_ACCESS_TOKEN);
        Request request = new Request.Builder()
                .get()
                .url("https://apiv2.twitcasting.tv/search/lives?type=recommend&lang=ja")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("X-Api-Version", BuildConfig.API_VERSION)
                .build();
        OkHttpClient client = new OkHttpClient.Builder().build();
        client.newCall(request).enqueue(callback);
    }

    private static class Adapter extends ArrayAdapter<SearchLiveMovieResponse.MovieInfo> {

        private Adapter(Context context) {
            super(context, R.layout.list_item);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            }

            MovieInfo movieInfo = getItem(position);
            ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            Picasso.with(parent.getContext())
                    .load(movieInfo.getMovie().getSmallThumbnail())
                    .into(thumbnail);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText(movieInfo.getMovie().getTitle());
            TextView userName = (TextView) convertView.findViewById(R.id.user_name);
            userName.setText(movieInfo.getBroadcaster().getName());

            return convertView;
        }
    }

}
