CREATE TABLE IF NOT EXISTS networks (
  id UUID primary key not null,
  source_id VARCHAR(255) not null,
  client UUID not null,
  foreign key (client) references clients(id)
);