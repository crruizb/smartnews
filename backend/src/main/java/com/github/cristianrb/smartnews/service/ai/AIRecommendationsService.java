package com.github.cristianrb.smartnews.service.ai;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.Recommendation;
import com.github.cristianrb.smartnews.entity.RecommendationPK;
import com.github.cristianrb.smartnews.entity.User;
import com.github.cristianrb.smartnews.repository.RecommendationsRepository;
import com.github.cristianrb.smartnews.service.contributions.ContributionsMapper;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
public class AIRecommendationsService implements RecommendationsService {

    private final ChatClient chatClient;
    private final GenericSqlTools sqlTools;
    private final SchemaDescriptionProvider schemaDescriptionProvider;
    private final RecommendationsRepository recommendationsRepository;

    public AIRecommendationsService(ChatClient.Builder builder, GenericSqlTools sqlTools, SchemaDescriptionProvider schemaDescriptionProvider, RecommendationsRepository recommendationsRepository) {
        this.chatClient = builder.build();
        this.sqlTools = sqlTools;
        this.schemaDescriptionProvider = schemaDescriptionProvider;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public List<Contribution> recommendations(User user) {
        var contributions = this.recommendationsRepository.findRecommendedContributionsByUserId(user.getId());
        if (contributions.isEmpty()) {
            List<Integer> recommendedIds = getPredictionsForUser(user.getId());
            var recommendations = recommendedIds.stream().map(id -> new Recommendation(user.getId(), id)).toList();
            this.recommendationsRepository.saveAll(recommendations);
            contributions = this.recommendationsRepository.findRecommendedContributionsByUserId(user.getId());
        }

        return contributions
                .stream().map(c -> {
                    return ContributionsMapper.mapContributionDAOToContribution(c, user.getId());
                }).toList();
    }

    private List<Integer> getPredictionsForUser(String userId) {
        String system = """
            ROLE: News recommendation engine
            
            OBJECTIVE:
            Produce a single raw JSON array of up to 50 distinct article ids (integers). Example: [12,5,7]
            
            OUTPUT RULES:
            - Output ONLY the JSON array. No prose, no code fences, no explanation.
            - Length <= 50; if fewer exist, return what is available.
            - All ids unique and must exist in contribution.id.
            - Exclude any article already voted on by the user (user_contribution).
            - If none available, output [].
            
            DATA & SCHEMA (concise):
            """ + schemaDescriptionProvider.getSchemaSummary() + """
            RANKING PRIORITY:
            1. Articles highly rated (vote 4 or 5) by users sharing high-rated categories with the target user (their votes >=4).
            2. Recent popular articles (many high votes) within the user’s high-rated categories.
            3. If still insufficient, globally recent popular articles (still excluding already voted).
            
            CONSTRAINTS:
            - READ-ONLY. Never modify data.
            - Use SQL tool ONLY when needed for fresh data.
            - Avoid using SQL tool with a repeated query.
            - Only issue a query if its result will be directly used to build or filter candidate ids; avoid exploratory duplicates.
            - Maximum 7 SQL queries total; prefer aggregated queries.
            - Only SELECT statements; add LIMIT clauses as needed.
            - Never fabricate ids; always verify via contribution.
            - Time budget: aim to finish all reasoning and SQL tool use within 20 seconds; if close to limit, immediately return the best current valid list.
            
            SQL GUIDELINES:
            - Fetch only needed columns (id, category, vote aggregates, timestamps).
            - Use aggregation to minimize queries.
            - Avoid redundant queries for the same slice of data.
            
            VALIDATION BEFORE RETURN:
            - All ids exist in contribution.
            - No ids the user has voted.
            - No duplicates.
            - Count <= 50.
            
            Return ONLY the JSON array (no key, no wrapper).
        """;


        List<Integer> content = CompletableFuture
                .supplyAsync(() -> chatClient
                        .prompt(system + "\nGenerate recommendations for user id: " + userId)
                        .tools(sqlTools, schemaDescriptionProvider)
                        .call()
                        .entity(new ParameterizedTypeReference<List<Integer>>() {}))
                .completeOnTimeout(List.of(), 30, TimeUnit.SECONDS)
                .exceptionally(ex -> List.of())
                .join();
        System.out.println(content);

        return content;
    }
}