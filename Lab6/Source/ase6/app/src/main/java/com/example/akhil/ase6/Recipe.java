package com.example.akhil.ase6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.lang.Object;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpEntity;



import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Recipe extends AppCompatActivity {

    String API_URL = "https://api.fullcontact.com/v2/person.json?";
    String API_KEY = "b29103a702edd6a";
    String sourceText;
    TextView outputTextView;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        outputTextView = (TextView) findViewById(R.id.txt_Result);
    }
    public void logout(View v) {
        Intent redirect = new Intent(Recipe.this, LoginActivity.class);
        startActivity(redirect);
    }
    public void translateText(View v) {
        TextView sourceTextView = (TextView) findViewById(R.id.txt_Email);
        ImageView sourceImageView=(ImageView) findViewById(R.id.imageView);

        sourceText = sourceTextView.getText().toString();
        //sourceImage=sourceImageView.getText().toBitImage();


        String getURL = "https://api.edamam.com/search?q="+sourceText+"&app_id=1079fc51&app_key=d2db7528aafb672d292b1ad804e83193";
        // /The API service URL
        // Image getURLl=" ";


        Log.v("tag","url is :"+getURL);
        final String response1 = "";
        OkHttpClient client = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .url(getURL)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }                @Override

                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;
                    final String result = response.body().string();
                    try {
                        jsonResult = new JSONObject(result);

                        JSONArray hitsArray = jsonResult.getJSONArray("hits");



                        JSONObject recipeObject = hitsArray.getJSONObject(0).getJSONObject("recipe");

                        for (int i = 0; i < recipeObject.length(); i++) {
                            //  String id = c.getString("id");
                            // String name = c.getString("name");


                            final String convertedText = recipeObject.getString("url");
                            final String uri= recipeObject.getString("uri");
                            final String label= recipeObject.getString("label");
                            final String image=recipeObject.getString("image");

                            Log.d("okHttp", jsonResult.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    outputTextView.setText("URL :" + convertedText);
                                    //outputTextView2.setText("URI :" + uri);
                                    //outputTextView.setText("Label :" +label);

                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ImageView img = (ImageView) findViewById(R.id.imageView);
                    try {
                        URL url = new URL("https://www.edamam.com/web-img/18d/18dcf05995cb40e8ce4c077972341d7a.jpg");
                        //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                        HttpGet httpRequest = null;

                        httpRequest = new HttpGet(url.toURI());

                        HttpClient httpclient = new DefaultHttpClient();
                        HttpResponse resp = (HttpResponse) httpclient
                                .execute(httpRequest);

                        HttpEntity entity = resp.getEntity();
                        BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                        InputStream input = b_entity.getContent();

                        Bitmap bitmap = BitmapFactory.decodeStream(input);

                        img.setImageBitmap(bitmap);

                    } catch (Exception ex) {

                    }
                }
            });


        } catch (Exception ex) {
            outputTextView.setText("Image :" + ex.getMessage());

        }

    }
}