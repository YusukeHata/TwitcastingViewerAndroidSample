package com.kuwapp.twitcastingviewersample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OAuthActivity extends AppCompatActivity {

    private static final String OAUTH_URL = "https://apiv2.twitcasting.tv/oauth2/authorize?client_id=%s&response_type=code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        openBrowser();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (!intent.getAction().equals(Intent.ACTION_VIEW)) {
            return;
        }
        String code = intent.getData().getQueryParameter("code");
        fetchAccessToken(code, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                AccessToken accessToken = new Gson().fromJson(response.body().charStream(), AccessToken.class);
                launch(accessToken.getAccessToken());
                finish();
            }
        });
    }

    private void openBrowser() {
        Uri uri = Uri.parse(String.format(OAUTH_URL, BuildConfig.CLIENT_ID));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void fetchAccessToken(String code, Callback callback) {
        Request request = new Request.Builder()
                .post(createRequestBody(code))
                .url("https://apiv2.twitcasting.tv/oauth2/access_token")
                .build();
        OkHttpClient client = new OkHttpClient.Builder().build();
        client.newCall(request).enqueue(callback);
    }

    private RequestBody createRequestBody(String code) {
        return new FormBody.Builder()
                .add("code", code)
                .add("grant_type", "authorization_code")
                .add("client_id", BuildConfig.CLIENT_ID)
                .add("client_secret", BuildConfig.CLIENT_SECRET)
                .add("redirect_uri", BuildConfig.CALLBACK_URL)
                .build();
    }

    private void launch(String accessToken) {
        RecommendListActivity.startActivity(this, accessToken);
    }

}
