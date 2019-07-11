package com.example.webpageloader.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.webpageloader.R;
import com.example.webpageloader.data.TestResult;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.TestResultViewHolder> {
    private List<TestResult> results;

    public TestResultAdapter() {
        results = new ArrayList<>();
    }




    @NonNull
    @Override
    public TestResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_result_list, parent, false);
        return new TestResultViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull TestResultViewHolder holder, int position) {
        holder.webPageNameView.setText(results.get(position).getWebPageName());
        holder.startTimeStampView.setText(String.valueOf(results.get(position).getStart()));
        holder.finishTimeStampView.setText(String.valueOf(results.get(position).getEnd()));
        if (results.get(position).getStatusCode() == HttpURLConnection.HTTP_OK) {
            holder.webPageNameView.setBackgroundColor(holder.webPageNameView.getResources().getColor(R.color.successGreen));
            holder.customTestParamView1.setText(results.get(position).getDuration() + " ms");
            holder.customTestParamLabelView1.setText("Load time");
            holder.customTestParamView2.setText(results.get(position).getBodySize() + "KB");
            holder.customTestParamLabelView2.setText("Body size");
        } else {
            holder.webPageNameView.setBackgroundColor(holder.webPageNameView.getResources().getColor(R.color.failureRed));
            holder.customTestParamLabelView1.setText("HTTP status code");
            holder.customTestParamView1.setText(String.valueOf(results.get(position).getStatusCode()));
            holder.customTestParamLabelView2.setText("Message");
            holder.customTestParamView2.setText(results.get(position).getStatusMessage());
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public void update(List<TestResult> testResultList) {
        results.clear();
        results.addAll(testResultList);
        notifyDataSetChanged();
    }

    public String getFastestResultURL(){
        int fastestPos = 0;
        for(int i = 0; i < results.size(); ++i) {
            if(results.get(i).getDuration() != 0 && results.get(i).getDuration() < results.get(fastestPos).getDuration()) {
                fastestPos = i;
            }
        }
        return results.get(fastestPos).getWebPageName();
    }

    public String getSlowestResultURL() {
        int slowestPos = 0;
        for(int i = 0; i < results.size(); ++i) {
            if(results.get(i).getDuration() > results.get(slowestPos).getDuration()) {
                slowestPos = i;
            }
        }
        return results.get(slowestPos).getWebPageName();
    }

    public int getSuccessfulTestCount() {
        int sum = 0;
        for(int i = 0; i < results.size(); ++i) {
            if(results.get(i).getStatusCode() == HttpURLConnection.HTTP_OK) {
                sum++;
            }
        }
        return sum;
    }

    public int getUnsuccessfulTestCount() {
        int sum = 0;
        for(int i = 0; i < results.size(); ++i) {
            if(results.get(i).getStatusCode() != HttpURLConnection.HTTP_OK) {
                sum++;
            }
        }
        return sum;
    }



    class TestResultViewHolder extends RecyclerView.ViewHolder {
        TextView webPageNameView;
        TextView startTimeStampView;
        TextView finishTimeStampView;
        TextView customTestParamView1;
        TextView customTestParamLabelView1;
        TextView customTestParamView2;
        TextView customTestParamLabelView2;

        public TestResultViewHolder(@NonNull View itemView) {

            super(itemView);
            webPageNameView = itemView.findViewById(R.id.webPageNameView);
            startTimeStampView = itemView.findViewById(R.id.startTimeStampView);
            finishTimeStampView = itemView.findViewById(R.id.finishTimeStampView);
            customTestParamView1 = itemView.findViewById(R.id.customTestParamView1);
            customTestParamView2 = itemView.findViewById(R.id.customTestParamView2);
            customTestParamLabelView1 = itemView.findViewById(R.id.customTestParamLabelView1);
            customTestParamLabelView2 = itemView.findViewById(R.id.customTestParamLabelView2);
        }
    }

}
