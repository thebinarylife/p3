DROP TABLE IF exists cats;

CREATE TABLE IF NOT exists cats
(
	id int  IDENTITY NOT NULL PRIMARY KEY, 
--	id int auto_increment          PRIMARY KEY, 
--	PRIMARY KEY(id),
	name varchar(12),
	type varchar(12)
);


