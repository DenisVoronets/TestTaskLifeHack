package com.example.testtasklifehackstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<CompanyVisitCard> companyVisitCardList;
    private ProgressBar progressBar;
    private String companyNameUrl = "http://megakohz.bget.ru/test_task/test.php";
    private String companyImageUrl = "http://megakohz.bget.ru/test_task/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);

        companyVisitCardList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(companyVisitCardList, this);
        linearLayoutManager = new LinearLayoutManager(this);
        downloadPage();
    }

    public void downloadPage() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();

        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageTask().execute(companyNameUrl);
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Нет интернета", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private class DownloadPageTask extends AsyncTask<String, Void, String> {
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
                URL url = new URL(companyNameUrl);
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
                JSONArray companyNames = new JSONArray(json);
                for (int i = 0; i < 15; i++) {
                    JSONObject secondObject = companyNames.getJSONObject(i);
                    String companyName = secondObject.getString("name");
                    String companyImage = secondObject.getString("img");
                    int companyId = secondObject.getInt("id");
                    Uri uriImage = Uri.parse(companyImageUrl + companyImage);
                    companyVisitCardList.add(new CompanyVisitCard(companyName, uriImage,companyId));
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(linearLayoutManager);
                progressBar.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
