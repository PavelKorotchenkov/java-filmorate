INSERT INTO mpa (name)
SELECT name FROM (
  VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17')
) AS m(name)
WHERE NOT EXISTS (
  SELECT 1 FROM mpa WHERE mpa.name = m.name
);

INSERT INTO genre (name)
SELECT name FROM (
  VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')
) AS g(name)
WHERE NOT EXISTS (
  SELECT 1 FROM genre WHERE genre.name = g.name
);
