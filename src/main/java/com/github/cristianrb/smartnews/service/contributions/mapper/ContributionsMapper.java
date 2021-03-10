package com.github.cristianrb.smartnews.service.contributions.mapper;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;

public class ContributionsMapper {

    public static ContributionDAO mapContributionToContributionDAO(Contribution cont) {
        ContributionDAO contDAO = new ContributionDAO();
        contDAO.setTitle(cont.getTitle());
        contDAO.setLink(cont.getLink());
        contDAO.setCreator(cont.getCreator());
        contDAO.setDescription(cont.getDescription());
        //contDAO.setDescription("Description test");
        contDAO.setPubDate(cont.getPubDate());
        contDAO.setUrlImage(cont.getUrlImage());
        contDAO.setSource(cont.getSource());
        contDAO.setSourceUrl(cont.getSourceUrl());
        return contDAO;
    }
}
