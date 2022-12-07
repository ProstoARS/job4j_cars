CREATE TABLE if NOT EXISTS auto_user
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL
);

comment on table auto_user is 'Пользователь разместивший объявления';
comment on column auto_user.id is 'Идентификационный номер пользователя';
comment on column auto_user.login is 'Регистрационное имя пользователя';
comment on column auto_user.password is 'Пароль пользователя';
