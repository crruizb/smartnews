CREATE TABLE public.contributions (
	id SERIAL NOT NULL,
	categories text NULL,
	creator varchar(255) NULL,
	description text NULL,
	link varchar(255) NULL,
	pub_date varchar(255) NULL,
	"source" varchar(255) NULL,
	source_url varchar(255) NULL,
	title varchar(255) NULL,
	url_image varchar(255) NULL,
	CONSTRAINT contributions_pkey PRIMARY KEY (id),
	CONSTRAINT contributions_title_key UNIQUE (title)
);

CREATE TABLE public.users (
	id varchar(255) NOT NULL,
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE public.user_contribution (
	contribution_id int4 NOT NULL,
	vote int4 NOT NULL,
	accesed_on int8 NOT NULL,
	user_id varchar(255) NOT NULL,
	CONSTRAINT user_contribution_pkey PRIMARY KEY (contribution_id, user_id)
);

ALTER TABLE public.user_contribution ADD CONSTRAINT fkilyqpadgnudhcn9c3tjyifnvl FOREIGN KEY (user_id) REFERENCES public.users(id);
ALTER TABLE public.user_contribution ADD CONSTRAINT fkq1j0rcp0co5kbd0wgnpw7wxpp FOREIGN KEY (contribution_id) REFERENCES public.contributions(id);