create database spaceInvaders
use spaceInvaders
go

create table highscore(
	id int not null primary key IDENTITY(1,1),
	username varchar(60) not null,
	score int not null
);

SELECT * FROM highscore

drop database spaceInvaders


INSERT INTO highscore(username, score)
VALUES ('John', 10000);

INSERT INTO highscore(id, username, score)
VALUES (3, 'f', 3000);

INSERT INTO highscore(id, username, score)
VALUES (4, 'f2', 4000);

INSERT INTO highscore(id, username, score)
VALUES (5, 'f3', 5000);

INSERT INTO highscore(id, username, score)
VALUES (6, 'f4', 2300);

SELECT TOP 5 username, score
FROM highscore
ORDER BY score DESC

SELECT id FROM highscore WHERE id = (SELECT MAX(id) FROM highscore)
