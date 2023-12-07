INSERT INTO USER_ENTITY(EMAIL, USER_NAME, PASSWORD, PHONE, STATUS, REG_DATE) VALUES ('user1@gmail.com', 'user1', '$2a$10$pRPUavDR5wZ2Pj4sMEhi2.BUmKH8e9kr6nyxPwn8SRerBZJ/923fa', '010-1234-5678', 1, '2023-11-30 01:12:20.000000');
INSERT INTO USER_ENTITY(EMAIL, USER_NAME, PASSWORD, PHONE, STATUS, REG_DATE) VALUES ('user2@gmail.com', 'user2', '$2a$10$pRPUavDR5wZ2Pj4sMEhi2.BUmKH8e9kr6nyxPwn8SRerBZJ/923fa', '010-1234-5678', 1, '2023-11-30 01:12:20.000000');
INSERT INTO USER_ENTITY(EMAIL, USER_NAME, PASSWORD, PHONE, STATUS, REG_DATE) VALUES ('user3@gmail.com', 'user3', '$2a$10$pRPUavDR5wZ2Pj4sMEhi2.BUmKH8e9kr6nyxPwn8SRerBZJ/923fa', '010-1234-5678', 1, '2023-11-30 01:12:20.000000');
INSERT INTO USER_ENTITY(EMAIL, USER_NAME, PASSWORD, PHONE, STATUS, REG_DATE) VALUES ('user4@gmail.com', 'user4', '$2a$10$pRPUavDR5wZ2Pj4sMEhi2.BUmKH8e9kr6nyxPwn8SRerBZJ/923fa', '010-1234-5678', 2, '2023-11-30 01:12:20.000000');

INSERT INTO NOTICE(TITLE, CONTENT, HITS, LIKES, REG_DATE, DELETED, USER_ID) VALUES ('제목1', '내용1', 0, 0, '2023-11-30 01:12:20.000000', 0, 1);
INSERT INTO NOTICE(TITLE, CONTENT, HITS, LIKES, REG_DATE, DELETED, USER_ID) VALUES ('제목2', '내용2', 0, 0, '2023-12-01 01:12:20.000000', 0, 1);
INSERT INTO NOTICE(TITLE, CONTENT, HITS, LIKES, REG_DATE, DELETED, USER_ID) VALUES ('제목3', '내용3', 0, 0, '2023-12-02 01:12:20.000000', 0, 2);

INSERT INTO NOTICE_LIKE(NOTICE_ID, USER_ID) VALUES(2, 1);
INSERT INTO NOTICE_LIKE(NOTICE_ID, USER_ID) VALUES(3, 1);

INSERT INTO USER_LOGIN_HISTORY(USER_ID, EMAIL, USER_NAME, LOGIN_DATE, IP_ADDR) VALUES(1, 'user1@gmail.com', 'user1', '2023-11-30 01:12:20.000000', '127.0.0.1');
INSERT INTO USER_LOGIN_HISTORY(USER_ID, EMAIL, USER_NAME, LOGIN_DATE, IP_ADDR) VALUES(2, 'user2@gmail.com', 'user2', '2023-11-30 01:12:20.000000', '127.0.0.1');
INSERT INTO USER_LOGIN_HISTORY(USER_ID, EMAIL, USER_NAME, LOGIN_DATE, IP_ADDR) VALUES(3, 'user3@gmail.com', 'user3', '2023-11-30 01:12:20.000000', '127.0.0.1');
INSERT INTO USER_LOGIN_HISTORY(USER_ID, EMAIL, USER_NAME, LOGIN_DATE, IP_ADDR) VALUES(4, 'user4@gmail.com', 'user4', '2023-11-30 01:12:20.000000', '127.0.0.1');
