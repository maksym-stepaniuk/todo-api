CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE tasks (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       title VARCHAR(100) NOT NULL,
                       description VARCHAR(500),
                       status VARCHAR(50) NOT NULL,
                       priority INTEGER NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       due_at TIMESTAMP
);
