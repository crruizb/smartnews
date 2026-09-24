package com.github.cristianrb.smartnews.repository;

import com.github.cristianrb.smartnews.entity.ContributionDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributionsRepository extends JpaRepository<ContributionDAO, Integer> {

    Optional<ContributionDAO> findByTitle(String title);

    Page<ContributionDAO> findAllByPubDateAfterOrderByPubDateDescIdDesc(Pageable paging, String date);
    Page<ContributionDAO> findAllByCountryAndPubDateAfterAndPubDateBeforeOrderByPubDateDescIdDesc(Pageable paging, String country, String after, String before);
    Page<ContributionDAO> findAllBySourceAndPubDateAfterAndPubDateBeforeOrderByPubDateDescIdDesc(Pageable paging, String source, String after, String before);

    Optional<ContributionDAO> findByUrlImageContaining(String image);

    @Query(value = "SELECT c.* FROM contributions c " +
            "WHERE c.search_vector @@ to_tsquery('simple', :tsquery) " +
            "AND c.pub_date <= :now " +
            "ORDER BY ts_rank(c.search_vector, to_tsquery('simple', :tsquery)) DESC, c.id DESC",
            countQuery = "SELECT count(*) FROM contributions c " +
                    "WHERE c.search_vector @@ to_tsquery('simple', :tsquery) " +
                    "AND c.pub_date <= :now",
            nativeQuery = true)
    Page<ContributionDAO> searchByQuery(@Param("tsquery") String tsquery, @Param("now") String now, Pageable paging);
}
