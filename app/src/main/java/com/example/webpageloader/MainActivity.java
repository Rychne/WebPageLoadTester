package com.example.webpageloader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.webpageloader.adapter.TestResultAdapter;
import com.example.webpageloader.data.TestResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TestResultAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button startButton;
    private String[] webPages = {"https://index.hu", "http://www.hunifylabs.com/", "http://hunify.ujlap.hu/", "http://ilyenbiztosnincs.eu/", "http://www.goole.com/asd.html", "https://www.danceformers.hu/"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.TestResultsRecyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TestResultAdapter();
        recyclerView.setAdapter(adapter);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadWebPagesInBackground();
            }
        });
    }

    private void loadWebPagesInBackground() {

        new AsyncTask<Void, Void, List<TestResult>>() {

            @Override
            protected List<TestResult> doInBackground(Void... voids) {
                List<TestResult> testResultList = new ArrayList<>();
                long startTime = 0, endTime = 0;
                for (String webpage : webPages
                ) {
                    TestResult res = new TestResult();
                    res.setWebPageName(webpage);
                    HttpURLConnection conn = null;
                    try {
                        URL url = new URL(webpage);
                        startTime = System.currentTimeMillis();
                        conn = (HttpURLConnection) url.openConnection();
                        //Set request header to get Content-length header field in response if possible
                        conn.setRequestProperty("Accept-Encoding", "identity");
                        endTime = System.currentTimeMillis();

                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            if (conn.getHeaderField("Content-length") == null) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = br.readLine()) != null) {
                                    response.append(line);
                                }
                                br.close();
                                endTime = System.currentTimeMillis();
                                // Division by 1024  to convert B --> KB
                                res.setBodySize(response.length() / 1024);
                            } else {
                                endTime = System.currentTimeMillis();
                                // Division by 1024  to convert B --> KB
                                res.setBodySize(conn.getContentLength() / 1024);
                            }
                            res.setStart(startTime);
                            res.setEnd(endTime);
                            res.setDuration(endTime - startTime);
                            res.setStatusCode(conn.getResponseCode());
                            res.setStatusMessage(conn.getResponseMessage());
                        } else {
                            res.setStart(startTime);
                            res.setEnd(endTime);
                            res.setStatusCode(conn.getResponseCode());
                            res.setStatusMessage(conn.getResponseMessage());
                        }

                        testResultList.add(res);

                        conn.disconnect();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (UnknownHostException e) {
                        endTime = System.currentTimeMillis();

                        res.setStart(startTime);
                        res.setEnd(endTime);
                        res.setStatusMessage("No address associated with hostname");

                        testResultList.add(res);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(conn != null) {
                            conn.disconnect();
                        }
                    }
                }
                return testResultList;
            }

            @Override
            protected void onPostExecute(List<TestResult> testResultList) {
                adapter.update(testResultList);
                refreshTestsSummary();
            }
        }.execute();
    }

    private void refreshTestsSummary() {
        TextView fastest = findViewById(R.id.fastestView);
        TextView slowest = findViewById(R.id.slowestView);
        TextView successfulTestCount = findViewById(R.id.successfulView);
        TextView unsuccessfulTestCount = findViewById(R.id.unsuccesfulView);

        fastest.setText(adapter.getFastestResultURL());
        slowest.setText(adapter.getSlowestResultURL());
        successfulTestCount.setText(String.valueOf(adapter.getSuccessfulTestCount()));
        unsuccessfulTestCount.setText(String.valueOf(adapter.getUnsuccessfulTestCount()));
    }
}
