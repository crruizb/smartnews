CREATE TABLE recommendations (
    user_id varchar(255) NOT NULL,
    contribution_id int4 NOT NULL,
    CONSTRAINT recommendations_pk PRIMARY KEY (user_id, contribution_id),
    CONSTRAINT fk_recommendations_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_recommendations_contribution FOREIGN KEY (contribution_id) REFERENCES contributions(id)
);