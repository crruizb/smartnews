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

    private static DataModel instance;
    private Map<User, Map<Contribution, Double>> data;
    protected static Set<Contribution> contributions;
    @Autowired
    private final UsersService usersService;

    private DataModel() {
        usersService = SpringContextConfig.getBean(UsersService.class);
    }

    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
        }
        return instance;
    }

    public Map<User, Map<Contribution, Double>> createDataModel() {
        data = new HashMap<>();
        contributions = new HashSet<>();
        // TODO: Get all user that rated at least N contributions
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
}