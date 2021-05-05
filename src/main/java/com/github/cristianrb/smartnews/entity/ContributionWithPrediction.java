package com.github.cristianrb.smartnews.entity;

import java.util.Objects;

public class ContributionWithPrediction {

    private Integer id;
    private String title;
    private Integer vote;
    private Double value;

    public ContributionWithPrediction(Integer id, String title, Integer vote, Double value) {
        this.id = id;
        this.title = title;
        this.vote = vote;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getVote() {
        return vote;
    }

    public void setVote(Integer vote) {
        this.vote = vote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContributionWithPrediction that = (ContributionWithPrediction) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
