package com.github.cristianrb.smartnews.repository;

import com.github.cristianrb.smartnews.entity.ContributionDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContributionsRepository  extends JpaRepository<ContributionDAO, Integer> {

}
