-- Creates the Posts table
CREATE TABLE IF NOT EXISTS posts (
  id INTEGER PRIMARY KEY ASC,
  file VARCHAR (100),
  poster INTEGER,
  original INTEGER CHECK(original <= 1),
  FOREIGN KEY(poster) REFERENCES users(id)
);
