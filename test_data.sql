INSERT INTO ozon_users (login, password, is_deleted) VALUES 
	('name', 'password', false),
	('login', 'pass', true),
	('1', '1', false);

INSERT INTO products (content, is_deleted, owner_id) VALUES
	('lamp', false, 1),
	('phone', false, 2),
	('sofa', true, 1);

INSERT INTO comments (content, is_anonymous, is_deleted, author_id, product_id) VALUES
	('the best!', FALSE, FALSE, 1, 2),
	('i hate it', TRUE, FALSE, 1, 3),
	('nice', FALSE, TRUE, 3, 1);