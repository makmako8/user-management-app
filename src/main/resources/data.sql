-- 1. roles テーブル（ロール定義）
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

INSERT INTO roles (id, role_name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER');


-- 2. app_user テーブル（ユーザー情報）
CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL
);

INSERT INTO app_user (id, username, password, enabled) VALUES
(1, 'admin', '$2a$10$voV8pQ21wP6dTwNjYnNGJuCN0SguAxnjr/FRZiLACVzCwSXufADO2', true),
(2, 'testuser7', '$2a$10$eMvd4INcDDlOPmqvl2HciuPN.c6kMf0ZHe2o8qjQi4TFhExcunvIC', true),
(3, 'testuser8', '$2a$10$3j6iJiSXEi2/iPzvkTRi4.E1qhpOppRFJRVbxc8ssJuEa1HR.zzo6', true);

-- 3. app_user_roles（中間テーブル：ユーザーとロールの関係）
CREATE TABLE IF NOT EXISTS app_user_roles (
    user_id BIGINT,
    role_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES app_user(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
INSERT INTO app_user_roles (user_id, role_id) VALUES (1, 2);
INSERT INTO app_user_roles (user_id, role_id) VALUES
(1, 1), -- admin = ROLE_ADMIN
(2, 2), -- testuser7 = ROLE_USER
(3, 2); -- testuser8 = ROLE_USER


-- 4. task テーブル（タスク）
CREATE TABLE IF NOT EXISTS task (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    completed BOOLEAN,
    assigned_to BIGINT,
    FOREIGN KEY (assigned_to) REFERENCES app_user(id)
);

INSERT INTO task (id, title, status, completed, assigned_to) VALUES
(1, 'Java Bronze 復習', '未完了', false, 2),
(2, 'Spring Security の見直し', '進行中', false, 3);
