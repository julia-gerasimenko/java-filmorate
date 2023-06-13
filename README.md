# java-filmorate
Это бэкенд для сервиса, который будет работать с фильмами, пользователями, их предпочтениями.

## Описание

С помощью данного сервиса можно:

- создать и изменить данные о конкретном фильме;
- получать список всех созданных фильмов;
- получать фильмы по заданным ID;
- поставить или удалить лайк конкретному фильму;
- зарегистрировать нового пользователя;
- обновить данные существующего пользователя;
- получить список всех зарегистрированных пользователей;
- получать пользователя по заданным ID;
- добавить или удалить друзей любого зарегистрированного пользователя.

На диаграмме ниже представлены визуальные данные проекта и конкретные связи между таблицами.

![Java-filmorate project data base](src/main/resources/schema_filmorate.png)
[исходник](https://drive.google.com/file/d/1hiB4UPHfdWY66AxW3Es2vCXPKXt_fo8V/view?usp=sharing)

## Примеры SQL-запросов

Получить фильм по id:

```roomsql
SELECT *
FROM films 
WHERE film_id = 1;
```

Получить все названия фильмов, рейтинг которых R:

```roomsql
SELECT f.name
FROM films AS f
LEFT JOIN film_rating AS fr ON fr.film_id = f.film_id
LEFT JOIN rating_mpa AS r ON fr.rating_id = r.rating_id
WHERE r.name = 'R';
```

Получить фильмы, жанр которых "драма":

```roomsql
SELECT f.name
FROM films AS f
LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id
LEFT JOIN genres AS g ON fg.genre_id = g.genre_id
WHERE g.name = 'драма';
```

Вывести id подтвержденных друзей пользователя c id = 5:

```roomsql
SELECT u.name, fr.friend_user_id
FROM users AS u
LEFT JOIN friends AS fr ON f.user_id = fr.user_id
LEFT JOIN friend_status AS fs ON fr.status_id = fs.status_id
WHERE fs.status = 'подтверждено';
```