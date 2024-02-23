# java-filmorate
![sql schema.](src/main/resources/shema.png)

В БД хранятся пользователи и фильмы. 
Функционал:
1. Можно показать список общих друзей пользователя с другим пользователем:
   SELECT friend_id 
   FROM user_friend 
   WHERE user_id = 2 AND friendship_status = True AND friend_id IN (SELECT friend_id 
   FROM user_friend 
   WHERE user_id = 1 AND friendship_status = True);
   
2. Можно показать, кто поставил лайки фильму ('Matrix' для примера):
   SELECT f.title, u.name FROM users AS u 
   JOIN user_film_like AS l ON u.id = l.user_id 
   JOIN films AS f ON l.film_id = f.id 
   WHERE f.title LIKE 'Matrix';

3. И каким фильмам пользователи (с именем 'Pavel') поставил лайк:
   SELECT u.name, f.title FROM users AS u
   JOIN user_film_like AS l ON u.id = l.user_id
   JOIN films AS f ON l.film_id = f.id
   WHERE u.name LIKE 'Pavel';