DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS candidates;
CREATE TABLE post (id SERIAL PRIMARY KEY, name TEXT);
CREATE TABLE candidates (id SERIAL PRIMARY KEY, name TEXT, country TEXT, region TEXT, city TEXT, lastName TEXT, sex TEXT, description TEXT, photoId TEXT);

