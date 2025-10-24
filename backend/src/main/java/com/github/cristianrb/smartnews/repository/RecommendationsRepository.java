package com.github.cristianrb.smartnews.repository;

import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.entity.Recommendation;
import com.github.cristianrb.smartnews.entity.RecommendationPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationsRepository extends JpaRepository<Recommendation, RecommendationPK> {

    /**
     * Find all recommendations for a specific user
     */
    List<Recommendation> findByUserId(String userId);

    /**
     * Find all recommendations for a specific user with pagination
     */
    Page<Recommendation> findByUserId(String userId, Pageable pageable);

    /**
     * Find recommended contributions for a specific user
     */
    @Query("SELECT r.contribution FROM Recommendation r WHERE r.userId = :userId")
    List<ContributionDAO> findRecommendedContributionsByUserId(@Param("userId") String userId);

    /**
     * Find recommended contributions for a specific user with pagination
     */
    @Query("SELECT r.contribution FROM Recommendation r WHERE r.userId = :userId")
    Page<ContributionDAO> findRecommendedContributionsByUserId(@Param("userId") String userId, Pageable pageable);
}
