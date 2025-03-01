#!/bin/bash

# 1. Аутентификация (Sign Up)
SIGN_RESPONSE=$(curl -s -X GET "http://localhost:18018/auth/sign/bde20/123")
TOKEN=$(echo "$SIGN_RESPONSE" | jq -r '.token' 2>/dev/null)

# 2. Вход (Log In) если регистрация не удалась
if [ -z "$TOKEN" ]; then
    LOGIN_RESPONSE=$(curl -s -X GET "http://localhost:18018/auth/log/1/1")
    TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.token')
fi

if [ -z "$TOKEN" ]; then
    echo "Ошибка аутентификации!"
    exit 1
fi

# 3. Создание комментария
COMMENT_RESPONSE=$(curl -s -X POST "http://localhost:18018/comments" \
  -H "Cookie: token=$TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "product": {
      "id": 2,
      "owner": { "id": 2, "login": "log" }
    },
    "author": { "id": 3, "login": "1" },
    "content": "hiu",
    "isAnonymous": false,
    "isDeleted": false
  }')

COMMENT_ID=$(echo "$COMMENT_RESPONSE" | jq -r '.id')

# 4. Получение комментариев для продукта
curl -X GET "http://localhost:18018/comments/2/0/10" \
  -H "Cookie: token=$TOKEN"

# 5. Обновление комментария
curl -X PUT "http://localhost:18018/comments/4" \
  -H "Cookie: token=$TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 6,
    "product": {
      "id": 2,
      "owner": { "id": 2, "login": "log" }
    },
    "author": { "id": 3, "login": "1" },
    "content": "hui",
    "isAnonymous": false,
    "isDeleted": false
  }'

# 6. Удаление комментария
curl -X DELETE "http://localhost:18018/comments/5" \
  -H "Cookie: token=$TOKEN"

# 7. Получение запросов на проверку
curl -X GET "http://localhost:18018/comment-requests/1/10" \
  -H "Cookie: token=$TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"isChecked": true}'

# 8. Получение непроверенных запросов
curl -X GET "http://localhost:18018/comment-requests/unchecked/2/10" \
  -H "Cookie: token=$TOKEN"

# 9. Обновление статуса запроса
curl -X PUT "http://localhost:18018/comment-requests/6" \
  -H "Cookie: token=$TOKEN" \
  -d '{
    "comment": {
      "id": 6,
      "product": { "id": 2, "owner": { "id": 2 }, "content": "hiu" },
      "author": { "id": 3 },
      "content": "hiu",
      "isAnonymous": false
    },
    "isChecked": true,
    "checker": { "id": 4 },
    "isDeleted": true
  }'

# 10. Удаление запроса
curl -X DELETE "http://localhost:18018/comment-requests/2" \
  -H "Cookie: token=$TOKEN"