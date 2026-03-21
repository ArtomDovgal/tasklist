TRUNCATE TABLE users, tasks, users_tasks, users_roles RESTART IDENTITY CASCADE;


-- USERS
INSERT INTO users (name, username, password) VALUES
('John Doe', 'johndoe@gmail.com', '$2a$12$Uq39kDgtJrb8W920qdWcDed4M7mYyuVR4/OsV9JUvZAjOtHk0wuZ.'),
('Alice Smith', 'alice@smth.com', '$2a$12$uuN5uBnhkSq3eK6jYyJKvuqXOl6i5CZPsJSecZ/wfvk5I0lfZj4Nu'),
('Bob Johnson', 'bob@test.com', '$2a$12$ezeWdaM1ZEIZRmEgxRqbZ.dfSnUBBX5J71iEaOifIAEIpO.aPegAC');

-- TASKS
INSERT INTO tasks (title, description, status, expiration_date) VALUES
('Complete project', 'Finish the backend module', 'IN_PROGRESS', NOW() + INTERVAL '7 days'),
('Buy groceries', 'Milk, Bread, Eggs', 'TODO', NOW() + INTERVAL '1 day'),
('Workout', 'Go to the gym', 'DONE', NULL),
('Read book', 'Read 30 pages', 'TODO', NOW() + INTERVAL '3 days');

-- USERS_TASKS (зв’язки many-to-many)
INSERT INTO users_tasks (user_id, task_id) VALUES
(1, 1),
(1, 2),
(2, 3),
(3, 4),
(2, 1);

-- USERS_ROLES
INSERT INTO users_roles (user_id, role) VALUES
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER'),
(2, 'ROLE_USER'),
(3, 'ROLE_USER');
