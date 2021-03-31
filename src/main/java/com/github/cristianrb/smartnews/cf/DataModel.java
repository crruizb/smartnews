package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.config.SpringContextConfig;
import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.User;
import com.github.cristianrb.smartnews.entity.UserContributionDAO;
import com.github.cristianrb.smartnews.entity.UserDAO;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DataModel {

    private Map<User, Map<Contribution, Double>> data;
    private Set<Contribution> contributions;
    private UsersService usersService;

    public Map<User, Map<Contribution, Double>> createDataModel() {
        if (usersService == null) {
            usersService = SpringContextConfig.getBean(UsersService.class);
        }
        data = new HashMap<>();
        contributions = new HashSet<>();
        List<UserDAO> users = usersService.getAllUsers();
        for (UserDAO userDAO : users) {
            Set<UserContributionDAO> newsByUser = userDAO.getContributionsVisited();
            Map<Contribution, Double> newsVisited = new HashMap<>();
            for (UserContributionDAO userAndNew : newsByUser) {
                Contribution cont = ContributionsMapper.mapContributionDAOToContribution(userAndNew.getContribution());
                newsVisited.put(cont, (double) userAndNew.getVote());
                    contributions.add(cont);
            }

            User user = new User(userDAO.getId());
            data.put(user, newsVisited);
        }

        return data;
    }

    public Map<User, Map<Contribution, Double>> getData() {
        return data;
    }

    public Set<Contribution> getContributions() {
        return contributions;
    }
}