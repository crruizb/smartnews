package com.github.cristianrb.smartnews.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;


@Embeddable
public class UserContributionPK implements Serializable {

    private String user;
    private Integer contribution;

    public UserContributionPK() {}

    public UserContributionPK(String userId, int contributionId) {
        this.user = userId;
        this.contribution = contributionId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getContribution() {
        return contribution;
    }

    public void setContribution(Integer contribution) {
        this.contribution = contribution;
    }
}
