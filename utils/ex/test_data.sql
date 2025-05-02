-- Добавление продуктов
INSERT INTO products (content, is_deleted, seller_id)
VALUES
    ('Product 1 content', false, 1), -- Продукт от bde20
    ('Product 2 content', false, 2), -- Продукт от bde200
    ('Product 3 content', false, 1); -- Еще один продукт от bde20

-- Добавление покупок
INSERT INTO purchases (is_deleted, owner_id, product_id)
VALUES
    (false, 1, 1), -- bde20 купил Product 1
    (false, 2, 2), -- bde200 купил Product 2
    (false, 1, 3); -- bde20 купил Product 3

-- Добавление комментариев
INSERT INTO comments (content, is_anonymous, is_checked, is_deleted, author_id, product_id)
VALUES
    ('Comment 1 content', false, true, false, 1, 1), -- Комментарий от bde20 к Product 1
    ('Comment 2 content', false, true, false, 2, 2), -- Комментарий от bde200 к Product 2
    ('Comment 3 content', false, true, false, 1, 3); -- Комментарий от bde20 к Product 3

-- Добавление запросов на проверку комментариев
INSERT INTO comment_requests (is_deleted, comment_id)
VALUES
    (false, 1), -- Запрос на проверку Comment 1
    (false, 2), -- Запрос на проверку Comment 2
    (false, 3); -- Запрос на проверку Comment 3

-- Добавление запрещенных слов
INSERT INTO banned_words (word)
VALUES
    ('badword1'),
    ('badword2'),
    ('badword3');