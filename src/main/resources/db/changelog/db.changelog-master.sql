--liquibase formatted sql

--changeset datascience:001-create-app-schema dbms:postgresql splitStatements:false logicalFilePath:db/changelog/changes/001-create-schema.yaml
--validCheckSum 1:any
CREATE SCHEMA IF NOT EXISTS datas;

--changeset datascience:002-create-competition-import dbms:postgresql logicalFilePath:db/changelog/changes/002-create-competition-import.yaml
--validCheckSum 1:any
CREATE TABLE datas.competition_import (
    competition_id BIGINT NOT NULL,
    imported BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT pk_competition_import PRIMARY KEY (competition_id)
);

--changeset datascience:003-insert-competition-import dbms:postgresql logicalFilePath:db/changelog/changes/003-insert-competition-import.yaml
--validCheckSum 1:any
INSERT INTO datas.competition_import (competition_id) VALUES
(55960),
(58330),
(61794),
(62825),
(62170),
(63522),
(63610),
(64482),
(66171),
(68806),
(68808),
(70655),
(72878),
(73627),
(71467),
(71464),
(75007),
(73663),
(70171),
(72907),
(69046),
(74925),
(67411),
(70221),
(70463),
(70641),
(72661),
(80805),
(73093),
(80794),
(81402),
(78498),
(76531),
(78439),
(69095),
(81384),
(90862),
(92668),
(91675),
(91676),
(85935),
(85859),
(82503),
(81712),
(82504),
(83996),
(85233),
(85234),
(83570),
(92672),
(82707),
(82704),
(83960),
(82577),
(89800),
(90822),
(81680),
(88336),
(88335),
(82573),
(86174),
(95877),
(92774),
(94786),
(96843),
(96579),
(96557),
(95106),
(96045),
(96540),
(96573),
(94393),
(96971),
(94391),
(93540),
(94375),
(94390),
(102069),
(93549),
(101677),
(95915),
(100832)
ON CONFLICT (competition_id) DO NOTHING;

--changeset datascience:004-create-competition-rounds dbms:postgresql logicalFilePath:db/changelog/changes/004-create-competition-rounds.yaml
--validCheckSum 1:any
CREATE TABLE datas.competition (
    id BIGSERIAL NOT NULL,
    pdga_id BIGINT,
    name VARCHAR(255),
    course VARCHAR(255),
    CONSTRAINT pk_competition PRIMARY KEY (id)
);

CREATE TABLE datas.round (
    id BIGSERIAL NOT NULL,
    competition_id BIGINT NOT NULL,
    date DATE,
    CONSTRAINT pk_round PRIMARY KEY (id),
    CONSTRAINT fk_round_competition FOREIGN KEY (competition_id) REFERENCES datas.competition (id)
);

CREATE TABLE datas.round_group (
    id BIGSERIAL NOT NULL,
    round_id BIGINT NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_round_group PRIMARY KEY (id),
    CONSTRAINT fk_round_group_round FOREIGN KEY (round_id) REFERENCES datas.round (id)
);

--changeset datascience:005-create-competition-rounds dbms:postgresql logicalFilePath:db/changelog/changes/005-create-competition-rounds.yaml
--validCheckSum 1:any
CREATE TABLE datas.course (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_course PRIMARY KEY (id)
);

alter table datas.competition add column course_id BIGINT NOT NULL REFERENCES datas.course (id);
alter table datas.competition drop column course;

CREATE TABLE datas.basket (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255),
    course_id BIGINT NOT NULL REFERENCES datas.course (id),
    CONSTRAINT pk_basket PRIMARY KEY (id)
);

CREATE TABLE datas.basket_variation (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255),
    basket_id BIGINT NOT NULL REFERENCES datas.basket (id),
    CONSTRAINT pk_basket_variation PRIMARY KEY (id)
);

--changeset datascience:006-pdga-import-processing dbms:postgresql logicalFilePath:db/changelog/changes/006-pdga-import-processing.yaml
--validCheckSum 1:any
ALTER TABLE datas.round_group RENAME TO round_division;
ALTER TABLE datas.round_division RENAME CONSTRAINT pk_round_group TO pk_round_division;
ALTER TABLE datas.round_division RENAME CONSTRAINT fk_round_group_round TO fk_round_division_round;

ALTER TABLE datas.competition ADD COLUMN simple_name VARCHAR(255);
ALTER TABLE datas.competition ADD COLUMN start_date DATE;
ALTER TABLE datas.competition ADD COLUMN end_date DATE;
ALTER TABLE datas.competition ADD COLUMN country VARCHAR(100);
ALTER TABLE datas.competition ADD COLUMN location VARCHAR(255);
ALTER TABLE datas.competition ADD COLUMN tier VARCHAR(50);
ALTER TABLE datas.competition ADD COLUMN total_players INTEGER;
ALTER TABLE datas.competition ADD CONSTRAINT uq_competition_pdga_id UNIQUE (pdga_id);

ALTER TABLE datas.round ADD COLUMN number INTEGER;
ALTER TABLE datas.round ADD COLUMN label VARCHAR(100);
ALTER TABLE datas.round ADD COLUMN label_abbreviated VARCHAR(50);
ALTER TABLE datas.round ADD CONSTRAINT uq_round_competition_number UNIQUE (competition_id, number);

ALTER TABLE datas.course ADD COLUMN pdga_id BIGINT;
ALTER TABLE datas.course ADD CONSTRAINT uq_course_pdga_id UNIQUE (pdga_id);

CREATE TABLE datas.competition_division (
    id BIGSERIAL NOT NULL,
    competition_id BIGINT NOT NULL,
    pdga_division_id BIGINT,
    code VARCHAR(20) NOT NULL,
    name VARCHAR(255),
    players INTEGER,
    pro BOOLEAN,
    short_name VARCHAR(255),
    latest_round INTEGER,
    CONSTRAINT pk_competition_division PRIMARY KEY (id),
    CONSTRAINT fk_competition_division_competition FOREIGN KEY (competition_id) REFERENCES datas.competition (id),
    CONSTRAINT uq_competition_division_code UNIQUE (competition_id, code)
);

CREATE TABLE datas.layout (
    id BIGSERIAL NOT NULL,
    competition_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    pdga_id BIGINT,
    name VARCHAR(255),
    holes INTEGER,
    par INTEGER,
    length INTEGER,
    units VARCHAR(50),
    accuracy VARCHAR(20),
    CONSTRAINT pk_layout PRIMARY KEY (id),
    CONSTRAINT fk_layout_competition FOREIGN KEY (competition_id) REFERENCES datas.competition (id),
    CONSTRAINT fk_layout_course FOREIGN KEY (course_id) REFERENCES datas.course (id),
    CONSTRAINT uq_layout_pdga_id UNIQUE (pdga_id)
);

CREATE TABLE datas.layout_hole (
    id BIGSERIAL NOT NULL,
    layout_id BIGINT NOT NULL,
    hole_ordinal INTEGER NOT NULL,
    hole_code VARCHAR(20),
    label VARCHAR(20),
    par INTEGER,
    length INTEGER,
    CONSTRAINT pk_layout_hole PRIMARY KEY (id),
    CONSTRAINT fk_layout_hole_layout FOREIGN KEY (layout_id) REFERENCES datas.layout (id),
    CONSTRAINT uq_layout_hole_ordinal UNIQUE (layout_id, hole_ordinal)
);

ALTER TABLE datas.round_division ADD COLUMN competition_division_id BIGINT;
ALTER TABLE datas.round_division ADD COLUMN layout_id BIGINT;
ALTER TABLE datas.round_division ADD COLUMN pdga_live_round_id BIGINT;
ALTER TABLE datas.round_division ADD COLUMN pool VARCHAR(100);
ALTER TABLE datas.round_division RENAME COLUMN name TO division_code;
ALTER TABLE datas.round_division ADD CONSTRAINT fk_round_division_competition_division FOREIGN KEY (competition_division_id) REFERENCES datas.competition_division (id);
ALTER TABLE datas.round_division ADD CONSTRAINT fk_round_division_layout FOREIGN KEY (layout_id) REFERENCES datas.layout (id);
ALTER TABLE datas.round_division ADD CONSTRAINT uq_round_division_round_division UNIQUE (round_id, competition_division_id);

CREATE TABLE datas.player (
    id BIGSERIAL NOT NULL,
    pdga_num BIGINT,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    name VARCHAR(255),
    city VARCHAR(255),
    country VARCHAR(100),
    nationality VARCHAR(100),
    profile_url VARCHAR(500),
    CONSTRAINT pk_player PRIMARY KEY (id),
    CONSTRAINT uq_player_pdga_num UNIQUE (pdga_num)
);

CREATE TABLE datas.round_result (
    id BIGSERIAL NOT NULL,
    round_division_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    pdga_result_id BIGINT,
    pdga_round_id BIGINT,
    pdga_score_id BIGINT,
    layout_id BIGINT,
    rating INTEGER,
    round_score INTEGER,
    round_to_par INTEGER,
    grand_total INTEGER,
    total_to_par INTEGER,
    round_rating INTEGER,
    previous_place INTEGER,
    running_place INTEGER,
    tied BOOLEAN,
    completed BOOLEAN,
    played INTEGER,
    CONSTRAINT pk_round_result PRIMARY KEY (id),
    CONSTRAINT fk_round_result_round_division FOREIGN KEY (round_division_id) REFERENCES datas.round_division (id),
    CONSTRAINT fk_round_result_player FOREIGN KEY (player_id) REFERENCES datas.player (id),
    CONSTRAINT fk_round_result_layout FOREIGN KEY (layout_id) REFERENCES datas.layout (id),
    CONSTRAINT uq_round_result_score_id UNIQUE (pdga_score_id),
    CONSTRAINT uq_round_result_player_division UNIQUE (round_division_id, player_id)
);

CREATE TABLE datas.hole_score (
    id BIGSERIAL NOT NULL,
    round_result_id BIGINT NOT NULL,
    hole_ordinal INTEGER NOT NULL,
    score INTEGER,
    par INTEGER,
    CONSTRAINT pk_hole_score PRIMARY KEY (id),
    CONSTRAINT fk_hole_score_round_result FOREIGN KEY (round_result_id) REFERENCES datas.round_result (id),
    CONSTRAINT uq_hole_score_result_ordinal UNIQUE (round_result_id, hole_ordinal)
);


--changeset datascience:007-basket-course-variations dbms:postgresql
--validCheckSum 1:any

CREATE TABLE datas.basket_course (
    id BIGSERIAL NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_basket_course PRIMARY KEY (id)
);

ALTER TABLE datas.basket DROP COLUMN course_id;
ALTER TABLE datas.basket ADD COLUMN basket_course_id BIGINT NOT NULL REFERENCES datas.basket_course(id);
ALTER TABLE datas.basket_variation ADD COLUMN distance integer;


--changeset datascience:008-basket-variations-results dbms:postgresql
--validCheckSum 1:any

ALTER TABLE datas.hole_score ADD COLUMN basket_variation_id BIGINT REFERENCES datas.basket_variation(id);


--changeset datascience:009-basket-variation-round-division dbms:postgresql
--validCheckSum 1:any

CREATE TABLE datas.basket_variation_round_division (
    id BIGSERIAL NOT NULL,
    round_division_id BIGINT NOT NULL,
    hole_ordinal INTEGER NOT NULL,
    basket_variation_id BIGINT NOT NULL,
    CONSTRAINT pk_basket_variation_round_division PRIMARY KEY (id),
    CONSTRAINT fk_bvrd_round_division FOREIGN KEY (round_division_id) REFERENCES datas.round_division (id),
    CONSTRAINT fk_bvrd_basket_variation FOREIGN KEY (basket_variation_id) REFERENCES datas.basket_variation (id),
    CONSTRAINT uq_bvrd_round_division_hole UNIQUE (round_division_id, hole_ordinal)
);

INSERT INTO datas.basket_variation_round_division (round_division_id, hole_ordinal, basket_variation_id)
SELECT grouped.round_division_id, grouped.hole_ordinal, MIN(grouped.basket_variation_id)
FROM (
    SELECT rr.round_division_id, hs.hole_ordinal, hs.basket_variation_id
    FROM datas.hole_score hs
             JOIN datas.round_result rr ON rr.id = hs.round_result_id
    WHERE hs.basket_variation_id IS NOT NULL
    GROUP BY rr.round_division_id, hs.hole_ordinal, hs.basket_variation_id
) grouped
GROUP BY grouped.round_division_id, grouped.hole_ordinal
HAVING COUNT(*) = 1;

ALTER TABLE datas.hole_score DROP COLUMN basket_variation_id;
