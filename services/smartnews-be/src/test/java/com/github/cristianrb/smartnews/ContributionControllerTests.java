package com.github.cristianrb.smartnews;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.rest.ContributionController;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContributionControllerTests {

    @Mock
    ContributionsService contributionsService;

    @InjectMocks
    ContributionController contributionController;

    private Pageable paging;
    private Page<ContributionDAO> contributionsPaged;
    private Contribution c1;
    private static final int page = 0;

    @BeforeEach
    public void setUp() {
        List<ContributionDAO> contributions = new ArrayList<ContributionDAO>();
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("C1");
        c1 = new Contribution(1, "C1", "D1", "L1", categories, "P1", "Cr1",
                "url1", "source1", "sourceUrl1", null);
        Contribution c2 = new Contribution(2, "C2", "D2", "L2", categories, "P2", "Cr2",
                "url2", "source2", "sourceUrl2", null);
        contributions.addAll(Arrays.asList(ContributionsMapper.mapContributionToContributionDAO(c1),
                ContributionsMapper.mapContributionToContributionDAO(c2)));

        paging = PageRequest.of(page, 10);
        final int start = (int)paging.getOffset();
        final int end = Math.min((start + paging.getPageSize()), contributions.size());
        contributionsPaged = new PageImpl<>(contributions.subList(start, end), paging, contributions.size());
    }

    @Test
    public void testRetrieveContributions() {
        when(contributionsService.getAll(paging, "all", "")).thenReturn(contributionsPaged);
        Map<String,Page<Contribution> >contributionsResult = contributionController.getAllContributions(page, "all", "");
        assertThat(contributionsResult.get("data").getTotalElements()).isEqualTo(2);
    }

    @Test
    public void testRetrieveAContribution() {
        ContributionDAO cdao = ContributionsMapper.mapContributionToContributionDAO(c1);
        when(contributionsService.getContributionById(1)).thenReturn(cdao);
        assertThat(contributionsService.getContributionById(1)).isEqualTo(cdao);
    }
}
