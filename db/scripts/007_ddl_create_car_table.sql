create table if not exists car
(
    id        serial primary key,
    name      varchar not null,
    engine_id int     not null unique references engine (id)
);

comment on table car is 'Автомобиль';
comment on column car.id is 'Идентификатор автомобиля';
comment on column car.name is 'Название автомобиля';
comment on column car.engine_id is 'Идентификатор двигателя автомобиля';
