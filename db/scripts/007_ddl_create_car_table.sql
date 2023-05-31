create table if not exists car
(
    id        serial primary key,
    brand     varchar not null,
    model     varchar not null,
    engine_id int     not null references engine (id)
);

comment on table car is 'Автомобиль';
comment on column car.id is 'Идентификатор автомобиля';
comment on column car.brand is 'Марка автомобиля';
comment on column car.model is 'Модель автомобиля';
comment on column car.engine_id is 'Идентификатор двигателя автомобиля';
