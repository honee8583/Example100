DROP TABLE IF EXISTS NOTICE;
DROP TABLE IF EXISTS USER_ENTITY;
DROP TABLE IF EXISTS NOTICE_LIKE;

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
    DELETED_DATE TIMESTAMP,

    USER_ID      BIGINT,
    constraint FK_NOTICE_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table NOTICE_LIKE
(
    ID        BIGINT auto_increment primary key,
    NOTICE_ID BIGINT,
    USER_ID   BIGINT,
    constraint FK_NOTICE_LIKE_NOTICE_ID foreign key (NOTICE_ID) references NOTICE (ID),
    constraint FK_NOTICE_LIKE_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);