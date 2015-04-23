--updates file name for a given post id
UPDATE posts
SET file = :audiokey
WHERE id = :id;
