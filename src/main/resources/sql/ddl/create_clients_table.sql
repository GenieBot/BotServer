CREATE TABLE IF NOT EXISTS clients (
  id UUID primary key not null,
  source_id VARCHAR(255) not null
);