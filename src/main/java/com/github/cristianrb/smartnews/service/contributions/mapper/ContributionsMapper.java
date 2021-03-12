package com.github.cristianrb.smartnews.service.contributions.mapper;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;

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
}
