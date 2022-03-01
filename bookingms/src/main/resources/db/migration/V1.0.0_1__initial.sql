create table cargo
(
    id                    bigserial not null primary key,
    booking_id            varchar(255) unique,
    origin_id             varchar(255),
    spec_arrival_deadline timestamp,
    spec_destination_id   varchar(255),
    spec_origin_id        varchar(255)
);

create table leg
(
    id                 bigserial not null primary key,
    cargo_id           int8      references cargo (id),
    load_location_id   varchar(255),
    load_time          timestamp not null,
    unload_location_id varchar(255),
    unload_time        timestamp not null,
    voyage_id          varchar(255)
);
