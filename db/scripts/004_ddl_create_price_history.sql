CREATE TABLE price_history
(
    id      SERIAL PRIMARY KEY,
    before  BIGINT not null,
    after   BIGINT not null,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    post_id INT REFERENCES auto_post (id)
);

comment on table price_history is 'История изменения цены автомобиля';
comment on column price_history.id is 'Идентификатор истории';
comment on column price_history.before is 'Старая цена';
comment on column price_history.after is 'Новая цена';
comment on column price_history.created is 'Дата изменения цены';
comment on column price_history.post_id is 'Внешний ключ к идентификатору объявления';