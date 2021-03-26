package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.entity.*;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DataModel {

    private Map<User, Map<Contribution, Double>> data;
    protected static Set<Contribution> contributions;
    @Autowired
    private UsersService usersService;

    public DataModel(UsersService usersService) {
        this.usersService = usersService;
    }

    public Map<User, Map<Contribution, Double>> createDataModel() {
        data = new HashMap<>();
        contributions = new HashSet<>();
        List<UserDAO> users = usersService.getAllUsers();
        for (UserDAO userDAO : users) {
            Set<UserContributionDAO> newsByUser = userDAO.getContributionsVisited();
            Map<Contribution, Double> newsVisited = new HashMap<>();
            for (UserContributionDAO userAndNew : newsByUser) {
                Contribution cont = ContributionsMapper.mapContributionDAOToContribution(userAndNew.getContribution());
                newsVisited.put(cont, 1.0);
                contributions.add(cont);
            }

            User user = new User(userDAO.getId());
            data.put(user, newsVisited);
        }

        for (Map.Entry<User, Map<Contribution, Double>> entry : data.entrySet()) {
            Map<Contribution, Double> value = entry.getValue();
            for (Contribution cont : contributions) {
                if (!value.containsKey(cont)) {
                    value.put(cont, 0.0);
                }
            }
        }

        System.out.println(data);
        return data;
    }

    public Map<User, Map<Contribution, Double>> getData() {
        return data;
    }
}
