-- Selects the user information for a given username
SELECT name, password FROM users WHERE name = :name;
