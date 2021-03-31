package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.entity.UserDAO;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UsersService {

    public UserDAO saveUser(UserDAO user);

    public Optional<UserDAO> getUser(String id);

    public List<UserDAO> getAllUsers();

    public Integer getVoteOfContributionByUser(ContributionDAO contributionDAO, String name);

    public List<Contribution> getContributionsVotedByUser(String userId, Principal principal);
}
