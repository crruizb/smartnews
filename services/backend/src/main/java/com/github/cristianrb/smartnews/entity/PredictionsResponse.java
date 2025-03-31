package com.github.cristianrb.smartnews.entity;

import java.util.ArrayList;
import java.util.List;

public class PredictionsResponse {

    private String user;
    private List<ContributionWithPrediction> predictions;

    public PredictionsResponse(String user, List<ContributionWithPrediction> predictions) {
        this.user = user;
        this.predictions = predictions;
    }

    public PredictionsResponse(String user) {
        this.user = user;
        this.predictions = new ArrayList<>();
    }

    public void addPrediction(ContributionWithPrediction contributionWithPrediction) {
        this.predictions.add(contributionWithPrediction);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<ContributionWithPrediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<ContributionWithPrediction> predictions) {
        this.predictions = predictions;
    }

    @Override
    public String toString() {
        return "PredictionsResponse{" +
                "user='" + user + '\'' +
                ", predictions=" + predictions +
                '}';
    }
}
