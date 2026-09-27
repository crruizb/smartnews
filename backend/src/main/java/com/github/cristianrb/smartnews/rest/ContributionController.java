package com.github.cristianrb.smartnews.rest;

import com.github.cristianrb.smartnews.service.ai.RecommendationsService;
import com.github.cristianrb.smartnews.entity.*;
import com.github.cristianrb.smartnews.errors.RecommendationsException;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import com.github.cristianrb.smartnews.service.contributions.UserContributionService;
import com.github.cristianrb.smartnews.service.contributions.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContributionController {

    private final ContributionsService contributionsService;
    private final UsersService usersService;
    private final UserContributionService usersContributionService;
    private final RecommendationsService aiRecommendationsService;
    private static final int PAGE_SIZE = 12;
    private final RecommendationsService recommendationsService;

    @Autowired
    public ContributionController(ContributionsService contributionsService,
                                  UsersService usersService,
                                  UserContributionService usersContributionService,
                                  RecommendationsService aiRecommendationsService,
                                  RecommendationsService recommendationsService) {
        this.contributionsService = contributionsService;
        this.usersService = usersService;
        this.usersContributionService = usersContributionService;
        this.aiRecommendationsService = aiRecommendationsService;
        this.recommendationsService = recommendationsService;
    }

    @GetMapping("/latest")
    public Map<String,Slice<Contribution>> getAllContributions(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "source", defaultValue = "all") String source,
            @RequestParam(name = "date", defaultValue = "2010-01-01T00:00:00Z") String date,
            Principal principal
    ) {
        Slice<ContributionDAO> contributions = contributionsService.getAll(PageRequest.of(page, PAGE_SIZE), source, date);
        Map<Integer, Integer> votes = votesOf(contributions, principal);
        Slice<Contribution> data = contributions.map(c -> toContribution(c, votes));
        HashMap<String, Slice<Contribution>> json = new HashMap<>();
        json.put("data", data);
        return json;
    }

    @GetMapping("/search")
    public Page<Contribution> searchContributions(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            Principal principal
    ) {
        Page<ContributionDAO> contributions = contributionsService.search(query, PageRequest.of(page, PAGE_SIZE));
        Map<Integer, Integer> votes = votesOf(contributions, principal);
        return contributions.map(c -> toContribution(c, votes));
    }

    @GetMapping("/contributions")
    public Contribution getContributionById(@RequestParam(name = "id") Integer id,
                                            Principal principal) {
        ContributionDAO contributionDAO = contributionsService.getContributionById(id);
        String username = usernameOrNull(principal);
        Contribution contribution = ContributionsMapper.mapContributionDAOToContribution(contributionDAO);

        if (username != null) {
            Integer vote = usersService.getVoteOfContributionByUser(contributionDAO, username);
            contribution.setVote(vote);
        }
        return contribution;
    }

    @GetMapping("/recommendations")
    public Page<Contribution> getFeed(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                      Principal principal) {
        if (usersService.getUser(principal.getName()).isPresent()) {
                User user = new User(principal.getName());
                List<Contribution> contributionList = recommendationsService.recommendations(user);

                Pageable pageable = PageRequest.of(page, PAGE_SIZE);
                return toPage(contributionList, pageable);
        }
        throw new RecommendationsException("There are no possible recommendations. Try to rate some contribution before.");

    }

    @GetMapping("/rated")
    public Page<Contribution> getContributionsRated(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    Principal principal) {
        List<Contribution> contsVoted = usersService.getContributionsVotedByUser(principal);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return toPage(contsVoted, pageable);
    }

    @PostMapping("/contributions/{id}")
    public void postVoteContribution(@RequestBody Integer vote, @PathVariable(name = "id") Integer id, Principal principal) {
        usersContributionService.voteContribution(vote, id, principal);
    }

    @PutMapping("/contributions")
    public void putVoteContribution(@RequestBody Integer vote, @RequestParam(name = "id") Integer id, Principal principal) {
        usersContributionService.voteContribution(vote, id, principal);
    }

    /** Loads the caller's votes for a whole page in one query instead of one lazy load per item. */
    private Map<Integer, Integer> votesOf(Slice<ContributionDAO> contributions, Principal principal) {
        String username = usernameOrNull(principal);
        if (username == null) return Map.of();
        List<Integer> ids = contributions.getContent().stream().map(ContributionDAO::getId).toList();
        return usersService.getVotesByUser(username, ids);
    }

    private static Contribution toContribution(ContributionDAO contributionDAO, Map<Integer, Integer> votes) {
        Contribution contribution = ContributionsMapper.mapContributionDAOToContribution(contributionDAO);
        contribution.setVote(votes.get(contributionDAO.getId()));
        return contribution;
    }

    private static String usernameOrNull(Principal principal) {
        return principal != null ? principal.getName() : null;
    }

    private Page<Contribution> toPage(List<Contribution> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
