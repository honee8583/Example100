DROP TABLE IF EXISTS NOTICE;

create table NOTICE
(
    ID          BIGINT auto_increment primary key,
    TITLE       VARCHAR(255),
    CONTENT     VARCHAR(255),
    HITS        INTEGER,
    LIKES       INTEGER,
    REG_DATE    TIMESTAMP,
    UPDATE_DATE TIMESTAMP
);