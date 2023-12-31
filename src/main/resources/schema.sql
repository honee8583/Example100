DROP TABLE IF EXISTS NOTICE;
DROP TABLE IF EXISTS USER_ENTITY;
DROP TABLE IF EXISTS NOTICE_LIKE;
DROP TABLE IF EXISTS USER_LOGIN_HISTORY;
DROP TABLE IF EXISTS BOARD_TYPE;
DROP TABLE IF EXISTS BOARD_HITS;
DROP TABLE IF EXISTS BOARD_LIKE;
DROP TABLE IF EXISTS BOARD_BAD_REPORT;
DROP TABLE IF EXISTS BOARD_SCRAP;
DROP TABLE IF EXISTS BOARD_BOOKMARK;
DROP TABLE IF EXISTS USER_INTEREST;
DROP TABLE IF EXISTS BOARD_COMMENT;
DROP TABLE IF EXISTS USER_POINT;
DROP TABLE IF EXISTS MAIL_TEMPLATE;

create table USER_ENTITY
(
    ID                 BIGINT auto_increment primary key,
    EMAIL              VARCHAR(255),
    USER_NAME          VARCHAR(255),
    PASSWORD           VARCHAR(255),
    PHONE              VARCHAR(255),
    STATUS             INTEGER,
    LOCK_YN            BOOLEAN,
    REG_DATE           TIMESTAMP,
    UPDATE_DATE        TIMESTAMP,
    PASSWORD_RESET_YN  BOOLEAN,
    PASSWORD_RESET_KEY VARCHAR(255)
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

create table USER_LOGIN_HISTORY
(
    ID         BIGINT auto_increment primary key,
    USER_ID    BIGINT,
    EMAIL      VARCHAR(255),
    USER_NAME  VARCHAR(255),
    LOGIN_DATE TIMESTAMP,
    IP_ADDR    VARCHAR(255)
);

create table BOARD_TYPE
(
    ID          BIGINT auto_increment primary key,
    BOARD_NAME  VARCHAR(255),
    USING_YN    BOOLEAN,
    REG_DATE    TIMESTAMP,
    UPDATE_DATE TIMESTAMP
);

create table BOARD
(
    ID                 BIGINT auto_increment primary key,
    USER_ID            BIGINT,
    BOARD_TYPE_ID      BIGINT,
    TITLE              VARCHAR(255),
    CONTENT            CLOB,
    TOP_YN             BOOLEAN,
    REG_DATE           TIMESTAMP,
    UPDATE_DATE        TIMESTAMP,
    PUBLISH_START_DATE DATE,
    PUBLISH_END_DATE   DATE,
    REPLY_CONTENTS     CLOB,
    constraint FK_BOARD_BOARD_TYPE_ID foreign key (BOARD_TYPE_ID) references BOARD_TYPE (ID),
    constraint FK_BOARD_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table BOARD_HITS
(
    ID        BIGINT auto_increment primary key,
    BOARD_ID BIGINT,
    USER_ID   BIGINT,
    REG_DATE  TIMESTAMP,
    constraint FK_BOARD_HITS_BOARD_ID foreign key (BOARD_ID) references BOARD (ID),
    constraint FK_BOARD_HITS_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table BOARD_LIKE
(
    ID        BIGINT auto_increment primary key,
    BOARD_ID BIGINT,
    USER_ID   BIGINT,
    REG_DATE  TIMESTAMP,
    constraint FK_BOARD_LIKE_BOARD_ID foreign key (BOARD_ID) references BOARD (ID),
    constraint FK_BOARD_LIKE_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table BOARD_BAD_REPORT
(
    ID             BIGINT auto_increment primary key,
    USER_ID        BIGINT,
    USER_NAME      VARCHAR(255),
    USER_EMAIL     VARCHAR(255),
    BOARD_ID       BIGINT,
    BOARD_USER_ID  BIGINT,
    BOARD_TITLE    VARCHAR(255),
    BOARD_CONTENTS VARCHAR(255),
    BOARD_REG_DATE TIMESTAMP,
    COMMENTS       VARCHAR(255),
    REG_DATE       TIMESTAMP
);

create table BOARD_SCRAP
(
    ID             BIGINT auto_increment primary key,
    USER_ID        BIGINT,
    BOARD_ID       BIGINT,
    BOARD_TYPE_ID  BIGINT,
    BOARD_USER_ID  BIGINT,
    BOARD_TITLE    VARCHAR(255),
    BOARD_CONTENTS VARCHAR(255),
    BOARD_REG_DATE TIMESTAMP,
    REG_DATE       TIMESTAMP,
    constraint FK_BOARD_SCRAP_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table BOARD_BOOKMARK
(
    ID             BIGINT auto_increment primary key,
    USER_ID        BIGINT,
    BOARD_ID       BIGINT,
    BOARD_TYPE_ID  BIGINT,
    BOARD_TITLE    VARCHAR(255),
    BOARD_URL      VARCHAR(255),
    REG_DATE       TIMESTAMP,
    constraint FK_BOARD_BOOKMARK_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table USER_INTEREST
(
    ID               BIGINT auto_increment primary key,
    USER_ID          BIGINT,
    INTEREST_USER_ID BIGINT,
    REG_DATE         TIMESTAMP,
    constraint FK_USER_INTEREST_USER_ID foreign key (USER_ID) references USER_ENTITY (ID),
    constraint FK_USER_INTEREST_INTEREST_USER_ID foreign key (INTEREST_USER_ID) references USER_ENTITY (ID)
);

create table BOARD_COMMENT
(
    ID       BIGINT auto_increment primary key,
    USER_ID  BIGINT,
    BOARD_ID BIGINT,
    COMMENTS VARCHAR(255),
    REG_DATE TIMESTAMP,
    constraint FK_BOARD_COMMENT_USER_ID foreign key (USER_ID) references USER_ENTITY (ID),
    constraint FK_BOARD_COMMENT_BOARD_ID foreign key (BOARD_ID) references BOARD (ID)
);

create table USER_POINT
(
    ID       BIGINT auto_increment primary key,
    USER_ID  BIGINT,
    USER_POINT_TYPE VARCHAR(255),
    POINT INTEGER,
    constraint FK_USER_POINT_USER_ID foreign key (USER_ID) references USER_ENTITY (ID)
);

create table LOGS
(
    ID       BIGINT auto_increment primary key,
    TEXT     CLOB,
    REG_DATE TIMESTAMP
);

create table MAIL_TEMPLATE
(
    ID             BIGINT auto_increment primary key,
    TEMPLATE_ID    VARCHAR(255),
    TITLE          VARCHAR(255),
    CONTENTS       VARCHAR(255),
    SEND_EMAIL     VARCHAR(255),
    SEND_USER_NAME VARCHAR(255),
    REG_DATE       TIMESTAMP
)