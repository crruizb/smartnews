package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.entity.UserContributionDAO;
import com.github.cristianrb.smartnews.entity.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserContributionImpl implements UserContributionService {

    @Autowired
    private ContributionsService contributionsService;
    @Autowired
    private UsersService usersService;

    @Override
    public void voteContribution(int vote, int contributionId, Principal principal) {
        if (vote > 5) vote = 5;
        else if (vote < 0) vote = 0;
        ContributionDAO contributionDAO = contributionsService.getContributionById(contributionId);
        Optional<UserDAO> optionalUser = usersService.getUser(principal.getName());
        UserDAO user = optionalUser.orElseGet(() -> new UserDAO(principal.getName()));
        Set<UserContributionDAO> contributionsVisited = user.getContributionsVisited();
        if (contributionsVisited == null) {
            contributionsVisited = new HashSet<>();
            user.setContributionsVisited(contributionsVisited);
        }
        contributionsVisited.add(new UserContributionDAO(user, contributionDAO, vote));
        usersService.saveUser(user);
    }

}
