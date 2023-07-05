CREATE TABLE IF NOT EXISTS car_photo
(
    id SERIAL PRIMARY KEY,
    post_id INT NOT NULL UNIQUE REFERENCES auto_post (id),
    photo BYTEA NOT NULL
);

comment on table car_photo is 'Фото продаваемого автомобиля';
comment on column car_photo.id is 'Идентификатор фото';
comment on column car_photo.post_id is 'Идентификатор объявления о продаже авто';
comment on column car_photo.photo is 'Фото';
