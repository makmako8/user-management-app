INSERT INTO ROLE (id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO ROLE (id, role_name) VALUES (2, 'ROLE_USER');

INSERT INTO APP_USER (id, username, password, enabled)
VALUES (1, 'admin', '$2a$10$voV8pQ21wP6dTwNjYnNGJuCN0SguAxnjr/FRZiLACVzCwSXufADO2', true);

INSERT INTO APP_USER (id, username, password, enabled) VALUES (
  11,
  'testuser1',
  '$2a$10$5V67.UDQbVRAEDyw3hzbLetff2JtDgAPGyfZw4aSlPI7Ks8lth8hS',
  true
);

INSERT INTO APP_USER (id, username, password, enabled) VALUES (
  7, 'testuser7',
  '$2a$10$eMvd4INcDDlOPmqvl2HciuPN.c6kMf0ZHe2o8qjQi4TFhExcunvIC',
  true
);


INSERT INTO APP_USER (id, username, password, enabled) VALUES (
  8, 'testuser8',
  '$2a$10$3j6iJiSXEi2/iPzvkTRi4.E1qhpOppRFJRVbxc8ssJuEa1HR.zzo6',
  true
);

INSERT INTO APP_USER (id, username, password, enabled) VALUES (
  3, 'testuser1',
  '$2a$10$5V67.UDQbVRAEDyw3hzbLetff2JtDgAPGyfZw4aSlPI7Ks8lth8hS',
  true
);
INSERT INTO APP_USER (id, username, password, enabled) VALUES (
  9, 'testuser9',
  '$2a$10$XZtc3MFKbt5Bj2DvSkBhce2DHFRHRINYGaYKAdzyTt0rohSPQK1by',
  true
);
INSERT INTO APP_USER (id, username, password, enabled) VALUES (
  10, 'testuser10',
  '$2a$10$SsOQRsiWocfy1SvUrSwg5OjzhCLdqRCaehGeQ7KhIhd3ytn9RZ.9G',
  true
);



INSERT INTO APP_USER_ROLES (user_id, role_id) VALUES (1, 1);
INSERT INTO APP_USER_ROLES (user_id, role_id) VALUES (11, 2);
INSERT INTO APP_USER_ROLES (user_id, role_id) VALUES (7, 2);

INSERT INTO APP_USER_ROLES (user_id, role_id) VALUES (8, 2);
INSERT INTO APP_USER_ROLES (user_id, role_id) VALUES (9, 2);
INSERT INTO APP_USER_ROLES (user_id, role_id) VALUES (10, 2);


CREATE TABLE IF NOT EXISTS TASK (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    assigned_to BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (assigned_to) REFERENCES APP_USER(id)
);
INSERT INTO TASK (id, title, status, assigned_to, completed)
VALUES (1, 'Java Bronze 復習', '未完了', 8, false);

CREATE TABLE IF NOT EXISTS ROLE (
    id BIGINT PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL,
    status VARCHAR(50),
    assigned_to BIGINT,
    CONSTRAINT fk_user FOREIGN KEY (assigned_to) REFERENCES APP_USER(id)
);