package com.github.cristianrb.smartnews.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_contribution")
@IdClass(UserContributionPK.class)
public class UserContributionDAO {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserDAO user;

    @Id
    @ManyToOne
    @JoinColumn(name = "contribution_id", referencedColumnName = "id")
    private ContributionDAO contribution;

    @JoinColumn(name = "accesed_on")
    private long accesedOn;

    @JoinColumn(name = "vote")
    private int vote;

    public UserContributionDAO() {}

    public UserContributionDAO(UserDAO user, ContributionDAO contributionDAO, int vote) {
        this.user = user;
        this.contribution = contributionDAO;
        this.accesedOn = new Date().toInstant().toEpochMilli();
        this.vote = vote;
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

    public long getAccesedOn() {
        return accesedOn;
    }

    public void setAccesedOn(long accesedOn) {
        this.accesedOn = accesedOn;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
