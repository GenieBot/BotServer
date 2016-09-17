CREATE TABLE IF NOT EXISTS module_data (
  network UUID primary key not null,
  module SERIAL not null,
  data json,
  foreign key (network) references networks(id),
  foreign key (module) references modules(id)
);