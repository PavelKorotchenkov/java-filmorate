DELETE FROM friendship;
DELETE FROM mpa;
DELETE FROM genre;
DELETE FROM films;
DELETE FROM film_genre;
DELETE FROM user_film_like;
DELETE FROM users;

INSERT INTO mpa (name) VALUES ('G');
INSERT INTO mpa (name) VALUES ('PG');
INSERT INTO mpa (name) VALUES ('PG-13');
INSERT INTO mpa (name) VALUES ('R');
INSERT INTO mpa (name) VALUES ('NC-17');

INSERT INTO genre (name) VALUES ('Комедия');
INSERT INTO genre (name) VALUES ('Драма');
INSERT INTO genre (name) VALUES ('Мультфильм');
INSERT INTO genre (name) VALUES ('Триллер');
INSERT INTO genre (name) VALUES ('Документальный');
INSERT INTO genre (name) VALUES ('Боевик');


--INSERT INTO users (email, name, login, birthday)
--           VALUES ('user@email.com', 'usename', 'login', '1976-09-20');

--INSERT INTO users (email, name, login, birthday)
--           VALUES ('user2@email.com', 'usename2', 'login2', '1993-09-20');

--INSERT INTO users (email, name, login, birthday)
--           VALUES ('NEWUSER@email.com', 'smth', 'epiclogin', '2015-01-01');

--INSERT INTO users (email, name, login, birthday)
--           VALUES ('NEWUSER2@email.com', 'smth1', 'epiclogin3', '2016-01-01');

--SELECT * FROM users;

--SELECT * FROM users WHERE id = 1;
--SELECT * FROM users WHERE id = 2;
--SELECT * FROM users WHERE id = 3;
--SELECT * FROM users WHERE id = 4;