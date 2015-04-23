-- selects post information for a given post ID
SELECT id, file, poster FROM posts WHERE id = :postid;
