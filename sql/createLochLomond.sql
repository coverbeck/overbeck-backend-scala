create table if not exists loch_lomond (
    id serial PRIMARY KEY,
    recording_date DATE,
    percent_full numeric(6, 3),
    created_timestamp timestamp not null default current_timestamp
);
create unique index on loch_lomond (recording_date);
