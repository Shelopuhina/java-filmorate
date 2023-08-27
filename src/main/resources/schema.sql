DROP TABLE IF EXISTS friend_list CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS genre CASCADE;
DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;

CREATE TABLE IF NOT EXISTS mpa (
mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
user_id INTEGER PRIMARY KEY,
email varchar (50) NOT NULL,
login varchar(50) NOT NULL,
name varchar (50),
birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
film_id int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(100) NOT NULL,
description varchar(200) NOT NULL,
release_date date NOT NULL,
duration INTEGER NOT NULL,
mpa_id INTEGER REFERENCES mpa (mpa_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_likes (
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,  
PRIMARY KEY (film_id,user_id)
);

CREATE TABLE IF NOT EXISTS genre (
genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
name varchar(40) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
genre_id INTEGER REFERENCES genre (genre_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friend_list (
user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
PRIMARY KEY (user_id, friend_id)
);

