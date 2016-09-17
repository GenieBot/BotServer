CREATE TABLE IF NOT EXISTS channels (
  id UUID primary key not null,
  source_id VARCHAR(255) not null,
  network UUID not null,
  foreign key (network) references networks(id)
);