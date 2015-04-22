-- Creates the Users table
CREATE TABLE IF NOT EXISTS users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR (30) NOT NULL,
  password VARCHAR (50) NOT NULL,
  UNIQUE(name)
);
