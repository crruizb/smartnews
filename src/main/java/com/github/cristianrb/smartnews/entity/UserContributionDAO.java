package com.github.cristianrb.smartnews.entity;

import org.apache.catalina.User;

import javax.annotation.Generated;
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

    public UserContributionDAO() {}

    public UserContributionDAO(UserDAO user, ContributionDAO contributionDAO) {
        this.user = user;
        this.contribution = contributionDAO;
        this.accesedOn = new Date().toInstant().toEpochMilli();
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
}
