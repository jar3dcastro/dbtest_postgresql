create table tmp_entity_1 (
    id serial primary key,
    int_field int not null,
    float_field decimal(10, 2) null,
    string_field varchar(100) null,
    datetime_field timestamp null
);