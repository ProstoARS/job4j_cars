create table if not exists driver
(
    id serial primary key,
    name varchar not null
);

comment on table driver is 'Владелец автомобиля';
comment on column driver.id is 'Идентификатор владельца';
comment on column driver.name is 'Имя владельца';
