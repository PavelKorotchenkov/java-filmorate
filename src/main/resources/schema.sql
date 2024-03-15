create TABLE IF NOT EXISTS users
(
    id       INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    varchar(255) UNIQUE,
    name     varchar(255),
    login    varchar(255) UNIQUE,
    birthday timestamp
);

create TABLE IF NOT EXISTS mpa
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(55)
);

create TABLE IF NOT EXISTS films
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        varchar(255),
    description varchar(255),
    releaseDate timestamp,
    duration    integer,
    mpa_id      integer,
    FOREIGN KEY (mpa_id) REFERENCES mpa (id) ON DELETE CASCADE
);

create TABLE IF NOT EXISTS genre
(
    id   INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(55) unique
);

create TABLE IF NOT EXISTS film_genre
(
    film_id  integer,
    genre_id integer,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE CASCADE
);

create TABLE IF NOT EXISTS user_film_like
(
    user_id    integer,
    film_id    integer,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films (id) ON DELETE CASCADE
);

create TABLE IF NOT EXISTS friendship
(
    user_id           integer,
    friend_id         integer,
    friendship_status boolean,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS director
(
    id   bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(55) unique
);

CREATE TABLE IF NOT EXISTS film_director
(
    film_id  bigint REFERENCES films (id) ON DELETE CASCADE,
    director_id integer REFERENCES director (id) ON DELETE CASCADE,
    PRIMARY KEY(film_id, director_id)
);

create TABLE IF NOT EXISTS reviews
(
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content text NOT NULL,
    isPositive boolean,
    user_id integer,
    film_id integer,
    useful integer,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE
);

create TABLE IF NOT EXISTS reviews_likes
(
    review_id integer,
    user_id integer,
    isPositive boolean,
    FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS feed
(
    event_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id bigint REFERENCES users (id) ON DELETE CASCADE,
    entity_id bigint,
    event_type varchar(10),
    operation varchar(10),
    event_timestamp bigint
);