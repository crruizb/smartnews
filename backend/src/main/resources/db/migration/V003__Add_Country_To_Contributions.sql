ALTER TABLE public.contributions ADD COLUMN country varchar(8) NULL;
UPDATE public.contributions SET country = 'ES';
UPDATE public.contributions SET country = 'EN' where source = 'NY Times';
ALTER TABLE public.contributions ALTER COLUMN country SET NOT NULL;