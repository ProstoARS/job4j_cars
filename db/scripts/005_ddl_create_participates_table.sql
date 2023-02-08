CREATE TABLE participates
(
    id      serial PRIMARY KEY,
    user_id int not null REFERENCES auto_user (id),
    post_id int not null REFERENCES auto_post (id)
);

comment on table participates is 'Подписка';
comment on column participates.id is 'Идентификатор подписки';
comment on column participates.user_id is 'Идентификатор подписавшегося пользователя';
comment on column participates.post_id is 'Идентификатор объявления';