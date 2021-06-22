build:
	docker build -t game-store:latest .

run: 
	docker run -it -p 8080:8080 game-store