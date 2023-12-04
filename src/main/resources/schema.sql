DROP TABLE IF EXISTS NOTICE;

create table NOTICE
(
    ID           BIGINT auto_increment primary key,
    TITLE        VARCHAR(255),
    CONTENT      VARCHAR(255),
    HITS         INTEGER,
    LIKES        INTEGER,
    REG_DATE     TIMESTAMP,
    UPDATE_DATE  TIMESTAMP,
    DELETED      BOOLEAN,
    DELETED_DATE TIMESTAMP
);

DROP TABLE IF EXISTS USER_ENTITY;

create table USER_ENTITY
(
    ID          BIGINT auto_increment primary key,
    EMAIL       VARCHAR(255),
    USER_NAME   VARCHAR(255),
    PASSWORD    VARCHAR(255),
    PHONE       VARCHAR(255),
    REG_DATE    TIMESTAMP,
    UPDATE_DATE TIMESTAMP
);