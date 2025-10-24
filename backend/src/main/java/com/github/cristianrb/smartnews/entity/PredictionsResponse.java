package com.github.cristianrb.smartnews.entity;

import java.util.ArrayList;
import java.util.List;

public record PredictionsResponse(String user, List<ContributionWithPrediction> predictions) {

    public PredictionsResponse(String user) { this(user, new ArrayList<>()); }

    public void addPrediction(ContributionWithPrediction contributionWithPrediction) {
        this.predictions.add(contributionWithPrediction);
    }

    public String getUser() { return user; }
    public List<ContributionWithPrediction> getPredictions() { return predictions; }

    @Override
    public String toString() {
        return "PredictionsResponse{" +
                "user='" + user + '\'' +
                ", predictions=" + predictions +
                '}';
    }
}
