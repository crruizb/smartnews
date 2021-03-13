package com.github.cristianrb.smartnews.service.mapper;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;

import java.util.Arrays;
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
        contDAO.setCategories(cont.getCategories()
                .stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        return contDAO;
    }

    public static Contribution mapContributionDAOToContribution(ContributionDAO contDAO) {
        Contribution cont = new Contribution();
        cont.setTitle(contDAO.getTitle());
        cont.setLink(contDAO.getLink());
        cont.setCreator(contDAO.getCreator());
        cont.setDescription(contDAO.getDescription());
        cont.setPubDate(contDAO.getPubDate());
        cont.setUrlImage(contDAO.getUrlImage());
        cont.setSource(contDAO.getSource());
        cont.setSourceUrl(contDAO.getSourceUrl());
        cont.setCategories(Arrays.asList(contDAO.getCategories().split(",")));
        return cont;
    }
}
