package com.github.cristianrb.smartnews.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserDAO {

    @Id
    private String id;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserContributionDAO> contributionsVisited;

    public UserDAO() {}

    public UserDAO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<UserContributionDAO> getContributionsVisited() {
        return contributionsVisited;
    }

    public void setContributionsVisited(Set<UserContributionDAO> contributionsVisited) {
        this.contributionsVisited = contributionsVisited;
    }
}
