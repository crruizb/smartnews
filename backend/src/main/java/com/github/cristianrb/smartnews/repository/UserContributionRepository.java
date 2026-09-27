package com.github.cristianrb.smartnews.repository;

import com.github.cristianrb.smartnews.entity.UserContributionDAO;
import com.github.cristianrb.smartnews.entity.UserContributionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserContributionRepository extends JpaRepository<UserContributionDAO, UserContributionPK> {

    interface ContributionVote {
        Integer getContributionId();
        Integer getVote();
    }

    @Query("SELECT uc.contribution.id AS contributionId, uc.vote AS vote FROM UserContributionDAO uc " +
            "WHERE uc.user.id = :userId AND uc.contribution.id IN :contributionIds")
    List<ContributionVote> findVotesByUser(@Param("userId") String userId,
                                           @Param("contributionIds") Collection<Integer> contributionIds);
}
