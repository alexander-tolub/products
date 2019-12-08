create table if not exists product
(
    id serial
        constraint product_pk
            primary key,
    name varchar(100) not null,
    description varchar(1000) not null
);