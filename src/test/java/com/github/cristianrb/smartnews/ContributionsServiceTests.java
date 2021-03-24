package com.github.cristianrb.smartnews;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.repository.ContributionsRepository;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.ContributionsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContributionsServiceTests {

    @Mock
    ContributionsRepository contributionsRepository;

    @InjectMocks
    ContributionsServiceImpl contributionsService;

    private static final int page = 0;

    @Test
    public void testSaveContribution() {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("C1");
        Contribution c1 = new Contribution(null, "C1", "D1", "L1", categories, "P1", "Cr1",
                "url1", "source1", "sourceUrl1");

        ContributionDAO c1DAO = ContributionsMapper.mapContributionToContributionDAO(c1);

        when(contributionsRepository.save(c1DAO)).thenReturn(c1DAO);
        ContributionDAO cdao = contributionsService.saveContribution(c1);
        assertThat(cdao.equals(c1DAO));
    }

    @Test
    public void testGetContributions() {
        List<ContributionDAO> contributions = new ArrayList<ContributionDAO>();
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("C1");
        Contribution c1 = new Contribution(null, "C1", "D1", "L1", categories, "P1", "Cr1",
                "url1", "source1", "sourceUrl1");
        Contribution c2 = new Contribution(null, "C2", "D2", "L2", categories, "P2", "Cr2",
                "url2", "source2", "sourceUrl2");
        contributions.addAll(Arrays.asList(ContributionsMapper.mapContributionToContributionDAO(c1),
                ContributionsMapper.mapContributionToContributionDAO(c2)));

        Pageable paging = PageRequest.of(page, 10);
        final int start = (int)paging.getOffset();
        final int end = Math.min((start + paging.getPageSize()), contributions.size());
        Page<ContributionDAO> contributionsPaged = new PageImpl<>(contributions.subList(start, end), paging, contributions.size());

        when(contributionsRepository.findAll(paging)).thenReturn(contributionsPaged);
        Page<ContributionDAO> resultContributionsDAO = contributionsService.getAll(paging);
        assertThat(resultContributionsDAO.equals(contributionsPaged));
    }
}
