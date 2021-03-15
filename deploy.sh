docker stop levelfy-backend-1
docker rm levelfy-backend-1
git pull
sudo mvn package
docker build -t springio/levelfy-backend .
docker run -d --restart unless-stopped -p 8080:8080 --name levelfy-backend-1  springio/levelfy-backend
