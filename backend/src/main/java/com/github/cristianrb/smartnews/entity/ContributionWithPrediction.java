package com.github.cristianrb.smartnews.entity;

public record ContributionWithPrediction(Integer id, String title, Double value) {
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public Double getValue() { return value; }
}
