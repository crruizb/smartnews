package com.github.cristianrb.smartnews.cf;

import com.github.cristianrb.smartnews.entity.*;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.UsersService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class DataModel {

    private Map<User, Set<Contribution>> data;
    @Autowired
    private UsersService usersService;

    public DataModel(UsersService usersService) {
        this.usersService = usersService;
    }

    public Map<User, Set<Contribution>> createDataModel() {
        data = new HashMap<>();

        List<UserDAO> users = usersService.getAllUsers();
        for (UserDAO userDAO : users) {
            Set<UserContributionDAO> newsByUser = userDAO.getContributionsVisited();
            Set<Contribution> newsVisited = new HashSet<>();
            for (UserContributionDAO userAndNew : newsByUser) {
                Contribution cont = ContributionsMapper.mapContributionDAOToContribution(userAndNew.getContribution());
                newsVisited.add(cont);
            }
            User user = new User(userDAO.getId());
            data.put(user, newsVisited);
        }
        System.out.println(data);
        return data;
    }

    public Map<User, Set<Contribution>> getData() {
        return data;
    }
}
