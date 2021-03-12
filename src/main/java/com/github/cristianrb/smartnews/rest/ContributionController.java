package com.github.cristianrb.smartnews.rest;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import com.github.cristianrb.smartnews.service.contributions.mapper.ContributionsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ContributionController {

    private final ContributionsService contributionsService;

    @Autowired
    public ContributionController(ContributionsService contributionsService) {
        this.contributionsService = contributionsService;
    }

    @GetMapping("/contributions")
    public Page<Contribution> getAllContributions(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        int size = 10;
        Pageable paging = PageRequest.of(page, size);
        Page<ContributionDAO> contributionsDAO = this.contributionsService.getAll(paging);
        return contributionsDAO.map(ContributionsMapper::mapContributionDAOToContribution);
    }
}
