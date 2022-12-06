CREATE TABLE if NOT EXISTS auto_user(
    id SERIAL PRIMARY KEY ,
    login VARCHAR NOT NULL UNIQUE ,
    password VARCHAR NOT NULL
);

CREATE TABLE if NOT EXISTS auto_post(
    id SERIAL PRIMARY KEY ,
    text text,
    created timestamp,
    auto_user_id INT NOT NULL REFERENCES auto_user(id)
);