package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.entity.UserContributionDAO;
import com.github.cristianrb.smartnews.entity.UserDAO;
import com.github.cristianrb.smartnews.errors.ForbiddenAccesException;
import com.github.cristianrb.smartnews.errors.UserNotFoundException;
import com.github.cristianrb.smartnews.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDAO saveUser(UserDAO user) {
        return usersRepository.save(user);
    }

    @Override
    public Optional<UserDAO> getUser(String id) {
        return usersRepository.findById(id);
    }

    @Override
    public List<UserDAO> getAllUsers() {
        return usersRepository.findAll();
    }

    @Override
    public Integer getVoteOfContributionByUser(ContributionDAO contributionDAO, String name) {
        Integer vote = null;
        Optional<UserDAO> user = getUser(name);
        if (user.isPresent()) {
            for (UserContributionDAO userContributionDAO : user.get().getContributionsVisited()) {
                if (userContributionDAO.getContribution().equals(contributionDAO)) {
                    vote = userContributionDAO.getVote();
                }
            }
        }
        return vote;
    }

    @Override
    public List<Contribution> getContributionsVotedByUser(Principal principal) {
        Optional<UserDAO> user = getUser(principal.getName());
        if (user.isPresent()) {
            if (principal.getName().equals(principal.getName())) {
                List<Contribution> contsVoted = new ArrayList<>();
                for (UserContributionDAO userContributionDAO : user.get().getContributionsVisited()) {
                    ContributionDAO contributionDAO = userContributionDAO.getContribution();
                    Contribution contribution = ContributionsMapper.mapContributionDAOToContribution(contributionDAO);
                    contribution.setVote(userContributionDAO.getVote());
                    contsVoted.add(contribution);
                }
                contsVoted.sort(Contribution.contributionComparator);
                return contsVoted;
            }
            throw new ForbiddenAccesException("Forbidden access to this resource.");
        }
        throw new UserNotFoundException("User with userId: " + principal.getName() + " has no rated contributions or doesn't exists.");
    }


}
