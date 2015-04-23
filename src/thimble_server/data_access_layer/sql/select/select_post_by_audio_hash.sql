-- selects row in posts corresponding to a given audio hash
SELECT id, file, poster FROM posts WHERE file = :audio-hash;
