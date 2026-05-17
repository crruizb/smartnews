-- Add tsvector column for full-text search
ALTER TABLE public.contributions
    ADD COLUMN search_vector tsvector;

-- Backfill existing data
UPDATE public.contributions
SET search_vector = setweight(to_tsvector('simple', coalesce(title, '')), 'A')
                  || setweight(to_tsvector('simple', coalesce(description, '')), 'B');

-- Create GIN index for fast search
CREATE INDEX idx_contributions_search_vector
    ON public.contributions USING GIN (search_vector);

-- Create function to auto-update search_vector
CREATE OR REPLACE FUNCTION update_contributions_search_vector()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.search_vector := setweight(to_tsvector('simple', coalesce(NEW.title, '')), 'A')
                      || setweight(to_tsvector('simple', coalesce(NEW.description, '')), 'B');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to call the function on insert/update
CREATE TRIGGER trigger_update_contributions_search_vector
    BEFORE INSERT OR UPDATE ON public.contributions
    FOR EACH ROW
    EXECUTE FUNCTION update_contributions_search_vector();
