package com.github.cristianrb.smartnews.repository;

import com.github.cristianrb.smartnews.entity.ContributionDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributionsRepository extends JpaRepository<ContributionDAO, Integer> {

    Optional<ContributionDAO> findByTitle(String title);
}
