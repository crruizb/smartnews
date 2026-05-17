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
    Page<ContributionDAO> findAllByCountryAndPubDateAfterOrderByPubDateDescIdDesc(Pageable paging, String country, String date);
    Page<ContributionDAO> findAllBySourceAndPubDateAfterOrderByPubDateDescIdDesc(Pageable paging, String source, String date);

    Optional<ContributionDAO> findByUrlImageContaining(String image);

    @Query(value = "SELECT c.* FROM contributions c " +
            "WHERE c.search_vector @@ to_tsquery('simple', :tsquery) " +
            "ORDER BY ts_rank(c.search_vector, to_tsquery('simple', :tsquery)) DESC, c.id DESC",
            countQuery = "SELECT count(*) FROM contributions c " +
                    "WHERE c.search_vector @@ to_tsquery('simple', :tsquery)",
            nativeQuery = true)
    Page<ContributionDAO> searchByQuery(@Param("tsquery") String tsquery, Pageable paging);
}
