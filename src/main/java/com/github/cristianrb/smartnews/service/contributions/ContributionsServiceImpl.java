package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.repository.ContributionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return this.contributionsRepository.save(cDAO);
    }

    @Override
    public Page<ContributionDAO> getAll(Pageable paging) {
        return this.contributionsRepository.findAll(paging);
    }

    @Override
    public ContributionDAO getContributionById(Integer id) {
        return contributionsRepository.findById(id).get();
    }
}
