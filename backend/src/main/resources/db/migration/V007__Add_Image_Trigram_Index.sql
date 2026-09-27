-- The RSS downloader looks up existing articles by image file name with
-- url_image LIKE '%name%' for every new item. A trigram index lets that
-- substring match use an index instead of scanning the whole table.
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_contributions_url_image_trgm
    ON public.contributions USING GIN (url_image gin_trgm_ops);
