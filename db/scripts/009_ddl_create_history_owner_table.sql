create table if not exists history_owner
(
    id        serial primary key,
    driver_id int not null references driver (id),
    car_id    int not null references car (id),
    start_at timestamp without time zone,
    end_at timestamp without time zone
);

comment on table history_owner is 'История владения автомобилями';
comment on column history_owner.id is 'Идентификатор истории владения';
comment on column history_owner.driver_id is 'Идентификатор владельца';
comment on column history_owner.car_id is 'Идентификатор автомобиля';
comment on column history_owner.start_at is 'Начало владения автомобилем';
comment on column history_owner.end_at is 'Окончание владения автомобилем';