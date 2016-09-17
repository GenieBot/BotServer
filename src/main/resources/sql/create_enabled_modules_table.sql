CREATE TABLE IF NOT EXISTS enabled_modules (
  network UUID primary key not null,
  module SERIAL not null,
  foreign key (network) references networks(id),
  foreign key (module) references modules(id)
);