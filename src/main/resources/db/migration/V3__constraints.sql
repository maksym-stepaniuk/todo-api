ALTER TABLE users
ADD CONSTRAINT uq_users_email UNIQUE (email);

ALTER TABLE projects
ADD CONSTRAINT uq_projects_user_name UNIQUE (user_id, name);

CREATE INDEX IF NOT EXISTS idx_projects_user_id
    ON projects(user_id);

CREATE INDEX IF NOT EXISTS idx_tasks_project_id
    ON tasks(project_id);

CREATE INDEX IF NOT EXISTS idx_tasks_status
    ON tasks(status);

ALTER TABLE tasks
    ADD CONSTRAINT chk_tasks_status
    CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE') );

ALTER TABLE tasks
    ADD CONSTRAINT chk_tasks_priority
    CHECK (priority BETWEEN 1 AND 5);
