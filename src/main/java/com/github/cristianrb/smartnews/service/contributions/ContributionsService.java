package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ContributionsService {

    ContributionDAO saveContribution(Contribution cont);

    Page<ContributionDAO> getAll(Pageable paging);
}
