INSERT INTO roles (id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, role_name) VALUES (2, 'ROLE_USER');

-- ユーザー
INSERT INTO app_user (id, username, password, enabled)
VALUES (1, 'admin', '$2a$10$voV8pQ21wP6dTwNjYnNGJuCN0SguAxnjr/FRZiLACVzCwSXufADO2', true);

INSERT INTO app_user (id, username, password, enabled)
VALUES (7, 'testuser7', '$2a$10$eMvd4INcDDlOPmqvl2HciuPN.c6kMf0ZHe2o8qjQi4TFhExcunvIC', true);

INSERT INTO app_user (id, username, password, enabled)
VALUES (8, 'testuser8', '$2a$10$3j6iJiSXEi2/iPzvkTRi4.E1qhpOppRFJRVbxc8ssJuEa1HR.zzo6', true);

-- ユーザーロール
INSERT INTO app_user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO app_user_roles (user_id, role_id) VALUES (7, 2);s
INSERT INTO app_user_roles (user_id, role_id) VALUES (8, 2);

-- タスク
INSERT INTO task (title, description, assigned_to, completed, created_by, created_at) VALUES
('日報作成', '本日の業務内容をまとめて提出', 7, false, 1, CURRENT_TIMESTAMP);

INSERT INTO task (title, description, assigned_to, completed, created_by, created_at) VALUES
('資料提出', '週次資料を管理者に送付', 8, true, 1, CURRENT_TIMESTAMP);

INSERT INTO task (title, description, assigned_to, completed, created_by, created_at) VALUES
('コードレビュー対応', 'レビュー指摘箇所を修正', 7, false, 8, CURRENT_TIMESTAMP);

