package com.github.cristianrb.smartnews.entity;

import java.io.Serializable;
import java.util.Objects;

public class RecommendationPK implements Serializable {

    private String userId;
    private int contributionId;

    public RecommendationPK() {}

    public RecommendationPK(String userId, int contributionId) {
        this.userId = userId;
        this.contributionId = contributionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getContributionId() {
        return contributionId;
    }

    public void setContributionId(int contributionId) {
        this.contributionId = contributionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationPK that = (RecommendationPK) o;
        return contributionId == that.contributionId && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, contributionId);
    }
}
