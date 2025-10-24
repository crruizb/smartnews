package com.github.cristianrb.smartnews.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "recommendations")
@IdClass(RecommendationPK.class)
public class Recommendation {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Id
    @Column(name = "contribution_id")
    private int contributionId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserDAO user;

    @ManyToOne
    @JoinColumn(name = "contribution_id", insertable = false, updatable = false)
    private ContributionDAO contribution;

    public Recommendation() {}

    public Recommendation(String userId, int contributionId) {
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

    public UserDAO getUser() {
        return user;
    }

    public void setUser(UserDAO user) {
        this.user = user;
    }

    public ContributionDAO getContribution() {
        return contribution;
    }

    public void setContribution(ContributionDAO contribution) {
        this.contribution = contribution;
    }
}
