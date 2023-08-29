# java-filmorate
Template repository for Filmorate project.

Описание: Проект по созданию рейтинга фильмов при помощи оценок пользователей. 
![Проект базы данных](https://github.com/Shelopuhina/java-filmorate/blob/add-friends-likes/src/main/resources/entity-relationship%20diagram.png)

Примеры запросов:
PUT /users/{id}/friends/{friendId} — добавление в друзья.
DELETE /users/{id}/friends/{friendId} — удаление из друзей.
GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.


