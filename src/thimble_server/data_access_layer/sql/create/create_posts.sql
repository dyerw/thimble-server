-- Creates the Posts table
CREATE TABLE IF NOT EXISTS posts (
  id INTEGER PRIMARY KEY ASC,
  file VARCHAR (100),
  poster INTEGER,
  FOREIGN KEY(poster) REFERENCES users(id)
);
