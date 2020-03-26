package com.example.testtasklifehackstudio;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CardDescription extends AppCompatActivity {
    private TextView textViewDescription, textViewWWW, textViewPhoneNumber, textViewCompanyName;
    private ImageView imageView;
    private String baseUrl = "http://megakohz.bget.ru/test_task/test.php?id=";
    private String companyImageUrl = "http://megakohz.bget.ru/test_task/";
    private int companyId;
    private String companyUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        textViewDescription = findViewById(R.id.textView_description);
        textViewPhoneNumber = findViewById(R.id.textView_phone_number);
        textViewWWW = findViewById(R.id.textView_www);
        textViewCompanyName = findViewById(R.id.textView_company_name);
        imageView = findViewById(R.id.imageView_company_card);
        companyId = getIntent().getIntExtra("id", 0);
        companyUrl = baseUrl + companyId;
        downloadPageDescription();

        Button button = findViewById(R.id.button_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void downloadPageDescription() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageJSON().execute(companyUrl);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Нет интернета", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private class DownloadPageJSON extends AsyncTask<String, Void, String> {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonResult = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(companyUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                jsonResult = builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
            return jsonResult;

        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            try {
                if (companyId == 6) {
                    json = json.replace("\"Лилия\"", "\'Лилия\'");
                }
                if (companyId == 7) {
                    json = json.replace("\"ПОЕХАЛИ!\"", "\'ПОЕХАЛИ!\'");
                }
                JSONArray companyInformation = new JSONArray(json);
                JSONObject object = companyInformation.getJSONObject(0);
                String companyName = object.getString("name");
                String companyImage = object.getString("img");
                String companyDescription = object.getString("description");
                String companyWWW = object.getString("www");
                String companyPhone = object.getString("phone");
                System.out.println(companyWWW);
                System.out.println(companyPhone);
                Uri uriVisitCardImage = Uri.parse(companyImageUrl + companyImage);
                textViewCompanyName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/textstyle.ttf"));
                textViewCompanyName.setText(companyName);
                textViewDescription.setText(companyDescription);
                textViewPhoneNumber.setText(companyPhone);
                textViewWWW.setText(companyWWW);
                Picasso.get().load(uriVisitCardImage).into(imageView);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
