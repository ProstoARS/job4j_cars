CREATE TABLE if NOT EXISTS auto_post
(
    id           SERIAL PRIMARY KEY,
    description  text,
    created      timestamp,
    auto_user_id INT NOT NULL REFERENCES auto_user (id)
);

comment on table auto_post is 'Объявление пользователя';
comment on column auto_post.id is 'Идентификатор объявления';
comment on column auto_post.description is 'Содержание объявления';
comment on column auto_post.auto_user_id is 'Идентификатор пользователя разместившего объявление';