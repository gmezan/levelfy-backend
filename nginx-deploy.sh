sudo apt update
sudo apt install nginx

sudo apt-get update
sudo apt-get install software-properties-common
sudo add-apt-repository universe
sudo add-apt-repository ppa:certbot/certbot
sudo apt-get update
sudo apt-get install certbot python3-certbot-nginx

sudo certbot --nginx

echo "/etc/nginx/sites-enabled/default"
