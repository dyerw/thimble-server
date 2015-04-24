-- selects posts for a given
SELECT id, file, poster, original FROM posts WHERE poster = :username;
