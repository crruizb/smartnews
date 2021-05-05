package com.github.cristianrb.smartnews.rest;

import com.github.cristianrb.smartnews.auth.CurrentUser;
import com.github.cristianrb.smartnews.cf.Recommender;
import com.github.cristianrb.smartnews.cf.SlopeOneImpl;
import com.github.cristianrb.smartnews.entity.*;
import com.github.cristianrb.smartnews.errors.RecommendationsException;
import com.github.cristianrb.smartnews.errors.UnauthorizedAccessException;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import com.github.cristianrb.smartnews.service.contributions.ContributionsService;
import com.github.cristianrb.smartnews.service.contributions.UserContributionService;
import com.github.cristianrb.smartnews.service.contributions.UsersService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ContributionController {

    private final ContributionsService contributionsService;
    private final UsersService usersService;
    private final UserContributionService usersContributionService;
    private static final int PAGE_SIZE = 10;

    @Autowired
    public ContributionController(ContributionsService contributionsService, UsersService usersService,
                                  UserContributionService usersContributionService) {
        this.contributionsService = contributionsService;
        this.usersService = usersService;
        this.usersContributionService = usersContributionService;
    }

    @ApiOperation(value = "Retrieves at most 10 contributions of a given page")
    @GetMapping("/latest")
    public Page<Contribution> getAllContributions(@RequestParam(name = "page", defaultValue = "0") Integer page) {
        return contributionsService.getAll(PageRequest.of(page, PAGE_SIZE))
                .map(ContributionsMapper::mapContributionDAOToContribution);
    }

    @ApiOperation(value = "Retrieve a contribution of a given id")
    @GetMapping("/contributions")
    public Contribution getContributionById(@RequestParam(name = "id") Integer id,
                                            @CurrentUser Principal principal) {
        ContributionDAO contributionDAO = contributionsService.getContributionById(id);
        Contribution contribution = ContributionsMapper.mapContributionDAOToContribution(contributionDAO);

        if (principal != null) {
            Integer vote = usersService.getVoteOfContributionByUser(contributionDAO, principal.getName());
            contribution.setVote(vote);
        }
        return contribution;
    }

    @ApiOperation(value = "Retrieves the contributions recommended for a given user")
    @GetMapping("/recommendations")
    public Page<Contribution> getFeed(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                      Principal principal) {
        if (principal == null) {
            throw new UnauthorizedAccessException("Log in to access to this resource");
        }
        if (usersService.getUser(principal.getName()).isPresent()) {
                User user = new User(principal.getName());
                Recommender recomm = new SlopeOneImpl();
                List<Contribution> contributionList = recomm.findRecommendations(user);

                Pageable pageable = PageRequest.of(page, PAGE_SIZE);
                return toPage(contributionList, pageable);
        }
        throw new RecommendationsException("There are no possible recommendations. Try to rate some contribution before.");

    }

    @ApiOperation(value = "Retrieves the contributions rated by a user")
    @GetMapping("/rated")
    public Page<Contribution> getContributionsRated(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    Principal principal) {
        if (principal == null) {
            throw new UnauthorizedAccessException("Log in to access to this resource");
        }
        List<Contribution> contsVoted = usersService.getContributionsVotedByUser(principal);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return toPage(contsVoted, pageable);
    }

    @ApiOperation(value = "Adds a new rating from a given user to a given contribution")
    @PostMapping("/contributions")
    public void postVoteContribution(@RequestBody Integer vote, @RequestParam(name = "id") Integer id, @CurrentUser Principal principal) {
        if (principal == null) {
            throw new UnauthorizedAccessException("Log in to access to this resource");
        }
        usersContributionService.voteContribution(vote, id, principal);
    }

    @ApiOperation(value = "Updates a rating from a given user to a given contribution")
    @PutMapping("/contributions")
    public void putVoteContribution(@RequestBody Integer vote, @RequestParam(name = "id") Integer id, Principal principal) {
        if (principal == null) {
            throw new UnauthorizedAccessException("Log in to access to this resource");
        }
        usersContributionService.voteContribution(vote, id, principal);
    }

    private Page<Contribution> toPage(List<Contribution> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        if(start > list.size())
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    @ApiOperation(value = "Retrieves the predictions for all the users")
    @GetMapping("/predictions")
    public List<PredictionsResponse> getAllPredictions() {
        SlopeOneImpl recomm = new SlopeOneImpl();
        Map<User, Map<Contribution, Double>> predictions = recomm.predictRecommendations();
        List<PredictionsResponse> cleanPredictions = new ArrayList<>();

        for (Map.Entry<User, Map<Contribution, Double>> entry : predictions.entrySet()) {
            PredictionsResponse predictionByUser = new PredictionsResponse(entry.getKey().getId());

            entry.getValue().forEach( (k, v) -> predictionByUser.addPrediction(new ContributionWithPrediction(k.getId(), k.getTitle(), k.getVote(), v)));
            cleanPredictions.add(predictionByUser);
        }

        return cleanPredictions;
    }
}
