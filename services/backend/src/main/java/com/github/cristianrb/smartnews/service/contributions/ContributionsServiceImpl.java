package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.repository.ContributionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContributionsServiceImpl implements ContributionsService {

    private ContributionsRepository contributionsRepository;

    @Autowired
    public ContributionsServiceImpl(ContributionsRepository contributionsRepository) {
        this.contributionsRepository = contributionsRepository;
    }

    @Override
    public ContributionDAO saveContribution(Contribution cont) {
        ContributionDAO cDAO = ContributionsMapper.mapContributionToContributionDAO(cont);
        if (!contributionExists(cont.getTitle())) {
            Optional<ContributionDAO> contDAO = findContributionByImage(cDAO.getUrlImage());
            if (contDAO.isPresent()) {
                ContributionDAO cDAOToUpdate = contDAO.get();
                cDAOToUpdate.setTitle(cDAO.getTitle());
                cDAOToUpdate.setDescription(cDAO.getDescription());
                cDAOToUpdate.setPubDate(cDAO.getPubDate());
                cDAOToUpdate.setCreator(cDAO.getCreator());
                cDAOToUpdate.setLink(cDAO.getLink());
                cDAO = cDAOToUpdate;
            }
            return this.contributionsRepository.save(cDAO);
        }
        return null;
    }


    @Override
    public Page<ContributionDAO> getAll(Pageable paging) {
        return this.contributionsRepository.findAllByOrderByPubDateDescIdDesc(paging);
    }

    @Override
    public ContributionDAO getContributionById(Integer id) {
        return contributionsRepository.findById(id).get();
    }

    @Override
    public boolean contributionExists(String title) {
        return this.contributionsRepository.findByTitle(title).isPresent();
    }

    private Optional<ContributionDAO> findContributionByImage(String image) {
        if (image == null || !image.contains("/")) return Optional.empty();
        try {
            String[] imageSplit = image.split("/");
            String imageName = imageSplit[imageSplit.length-1];
            return this.contributionsRepository.findByUrlImageContaining(imageName);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
