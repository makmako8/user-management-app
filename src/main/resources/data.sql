MERGE INTO roles (id, role_name) KEY(id) VALUES (1, 'ROLE_ADMIN');
MERGE INTO roles (id, role_name) KEY(id) VALUES (2, 'ROLE_USER');


MERGE INTO app_user (id, username, password, enabled)
KEY(id) VALUES (1, 'admin', '$2a$10$voV8pQ21wP6dTwNjYnNGJuCN0SguAxnjr/FRZiLACVzCwSXufADO2', true);

MERGE INTO app_user (id, username, password, enabled)
KEY(id) VALUES (7, 'testuser7', '$2a$10$eMvd4INcDDlOPmqvl2HciuPN.c6kMf0ZHe2o8qjQi4TFhExcunvIC', true);
MERGE INTO app_user (id, username, password, enabled)
KEY(id) VALUES (8, 'testuser8', '$2a$10$3j6iJiSXEi2/iPzvkTRi4.E1qhpOppRFJRVbxc8ssJuEa1HR.zzo6', true);




MERGE INTO app_user_roles (user_id, role_id) KEY(user_id, role_id) VALUES (1, 1);
MERGE INTO app_user_roles (user_id, role_id) KEY(user_id, role_id) VALUES (7, 2);
MERGE INTO app_user_roles (user_id, role_id) KEY(user_id, role_id) VALUES (8, 2);
