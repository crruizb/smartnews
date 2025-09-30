package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.entity.UserContributionDAO;
import org.springframework.security.core.parameters.P;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ContributionsMapper {

    public static ContributionDAO mapContributionToContributionDAO(Contribution cont) {
        ContributionDAO contDAO = new ContributionDAO();
        contDAO.setTitle(cont.getTitle());
        contDAO.setLink(cont.getLink());
        contDAO.setCreator(cont.getCreator());
        contDAO.setDescription(cont.getDescription());
        contDAO.setPubDate(cont.getPubDate());
        contDAO.setUrlImage(cont.getUrlImage());
        contDAO.setSource(cont.getSource());
        contDAO.setSourceUrl(cont.getSourceUrl());
        contDAO.setCountry(cont.getCountry());
        contDAO.setCategories(cont.getCategories()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        return contDAO;
    }

    public static Contribution mapContributionDAOToContribution(ContributionDAO contDAO, String username) {
        Contribution cont = new Contribution();
        cont.setId(contDAO.getId());
        cont.setTitle(contDAO.getTitle());
        cont.setLink(contDAO.getLink());
        cont.setCreator(contDAO.getCreator());
        cont.setDescription(contDAO.getDescription());
        cont.setPubDate(contDAO.getPubDate());
        cont.setUrlImage(contDAO.getUrlImage());
        cont.setSource(contDAO.getSource());
        cont.setSourceUrl(contDAO.getSourceUrl());
        cont.setCountry(contDAO.getCountry());
        cont.setCategories(Arrays.asList(contDAO.getCategories().split(",")));
        if (username != null) {
            Set<UserContributionDAO> users = contDAO.getUsers();
            for (UserContributionDAO ucd : users) {
                if (ucd.getContribution().getId() == contDAO.getId()) {
                    cont.setVote(ucd.getVote());
                }
            }
        }

        return cont;
    }
}
