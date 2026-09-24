package com.github.cristianrb.smartnews.service.contributions;

import com.github.cristianrb.smartnews.entity.Contribution;
import com.github.cristianrb.smartnews.entity.ContributionDAO;
import com.github.cristianrb.smartnews.repository.ContributionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContributionsServiceImpl implements ContributionsService {

    /** Must match the "yyyy-MM-dd HH:mm" format used by the RSS handlers. */
    private static final DateTimeFormatter PUB_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private ContributionsRepository contributionsRepository;

    /**
     * Articles dated beyond this many minutes in the future are ignored, since
     * they are almost always feed errors. A small tolerance absorbs timezone
     * skew between the source and the server.
     */
    @Value("${app.news.futureToleranceMinutes:0}")
    private long futureToleranceMinutes;

    @Autowired
    public ContributionsServiceImpl(ContributionsRepository contributionsRepository) {
        this.contributionsRepository = contributionsRepository;
    }

    private String feedUpperBound() {
        return LocalDateTime.now(ZoneOffset.UTC)
                .plusMinutes(futureToleranceMinutes)
                .format(PUB_DATE_FORMAT);
    }

    @Override
    public ContributionDAO saveContribution(Contribution cont) {
        ContributionDAO cDAO = ContributionsMapper.mapContributionToContributionDAO(cont);
        if (!contributionExists(cont.getTitle())) {
            Optional<ContributionDAO> contDAO = findContributionByImage(cDAO.getUrlImage());
            if (contDAO.isPresent()) {
                ContributionDAO cDAOToUpdate = contDAO.get();
                cDAOToUpdate.setTitle(cDAO.getTitle());
                cDAOToUpdate.setDescription(cDAO.getDescription());
                cDAOToUpdate.setPubDate(cDAO.getPubDate());
                cDAOToUpdate.setCreator(cDAO.getCreator());
                cDAOToUpdate.setLink(cDAO.getLink());
                cDAO = cDAOToUpdate;
            }
            return this.contributionsRepository.save(cDAO);
        }
        return null;
    }


    @Override
    public Page<ContributionDAO> getAll(Pageable paging, String source, String date) {
        String now = feedUpperBound();
        if (source.equals("es")) {
            return this.contributionsRepository.findAllByCountryAndPubDateAfterAndPubDateBeforeOrderByPubDateDescIdDesc(paging, "ES", date, now);
        } else if (source.equals("en")) {
            return this.contributionsRepository.findAllByCountryAndPubDateAfterAndPubDateBeforeOrderByPubDateDescIdDesc(paging, "EN", date, now);
        }
        return this.contributionsRepository.findAllBySourceAndPubDateAfterAndPubDateBeforeOrderByPubDateDescIdDesc(paging, source, date, now);
    }

    @Override
    public ContributionDAO getContributionById(Integer id) {
        return contributionsRepository.findById(id).get();
    }

    @Override
    public boolean contributionExists(String title) {
        return this.contributionsRepository.findByTitle(title).isPresent();
    }

    @Override
    public Page<ContributionDAO> search(String query, Pageable paging) {
        String trimmed = query != null ? query.trim() : "";
        if (trimmed.isEmpty()) {
            return Page.empty(paging);
        }
        String tsquery = buildTsQuery(trimmed);
        if (tsquery.isEmpty()) {
            return Page.empty(paging);
        }
        return this.contributionsRepository.searchByQuery(tsquery, feedUpperBound(), paging);
    }

    private String buildTsQuery(String query) {
        String normalized = query.toLowerCase()
                .replaceAll("[^\\p{L}\\p{N}\\s]", "");
        return Arrays.stream(normalized.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .map(word -> word + ":*")
                .collect(Collectors.joining(" & "));
    }

    private Optional<ContributionDAO> findContributionByImage(String image) {
        if (image == null || !image.contains("/")) return Optional.empty();
        try {
            String[] imageSplit = image.split("/");
            String imageName = imageSplit[imageSplit.length-1];
            return this.contributionsRepository.findByUrlImageContaining(imageName);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
