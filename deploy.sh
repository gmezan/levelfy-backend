docker build -t springio/levelfy-backend .
docker run -d --restart unless-stopped -p 8080:8080 springio/levelfy-backend
