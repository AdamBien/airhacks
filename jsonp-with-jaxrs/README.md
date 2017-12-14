# Build
mvn clean package && docker build -t com.airhacks/jsonp .

# RUN

docker rm -f jsonp || true && docker run -d -p 8080:8080 -p 4848:4848 --name jsonp com.airhacks/jsonp 