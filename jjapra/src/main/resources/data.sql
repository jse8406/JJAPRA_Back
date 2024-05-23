INSERT INTO member (id, password, name, email, phone_num) VALUES ('test1', 'test1','test1','test1@naver.com','010123456478');
INSERT INTO member (id, password, name, email, phone_num) VALUES ('test2', 'test2','test2','test2@naver.com','010123456479');
INSERT INTO issue (projectId, title, description,writer) values (1, '제목', '내용','test1');
INSERT INTO issue (projectId, title, description, writer) values (1, '제목2','내용2','test2');
INSERT INTO issue (projectId, title, description, writer) values (1, '제목3','내용3','test1');
INSERT INTO project (title, description) values ('DDing', 'hi');
INSERT INTO project (title, description) values ('Channy', 'hi');
INSERT INTO project (title, description) values ('Unsta9ram', 'hi');
INSERT INTO project (title, description) values ('Miging', 'hi');
INSERT INTO project (title, description) values ('Suzzang', 'hi');
INSERT INTO projectMember (project_id, member_id) values (1, 'test1');
INSERT INTO projectMember (project_id, member_id) values (1, 'test2');
INSERT INTO projectMember (project_id, member_id) values (2, 'test1');
INSERT INTO