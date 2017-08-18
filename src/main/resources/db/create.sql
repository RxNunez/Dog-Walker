SET MODE PostgreSQL;

CREATE TABLE IF NOT EXISTS weed (
  id int PRIMARY KEY auto_increment,
  dogname VARCHAR,
  breed VARCHAR,
  color VARCHAR,
  walkerid int

);

CREATE TABLE IF NOT EXISTS store (
id int PRIMARY KEY auto_increment,
walkername VARCHAR
)