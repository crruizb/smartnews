-- Indexes backing the /latest feed queries (filter by country or source,
-- range on pub_date, ordered by pub_date DESC, id DESC). Without them every
-- feed request scans the whole contributions table.
CREATE INDEX idx_contributions_country_pubdate
    ON public.contributions (country, pub_date DESC, id DESC);

CREATE INDEX idx_contributions_source_pubdate
    ON public.contributions (source, pub_date DESC, id DESC);
