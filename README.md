# Comment-to-Ticket Triage

a Web app that captures user comments and uses a Hugging Face model to determine whether a comment should become a support ticket.

- Deployed at https://pulsedesk-app-43844cc36acf.herokuapp.com/

![firefox_dBkULF7adS-ezgif com-resize(1)](https://github.com/user-attachments/assets/cfc3e9d3-30b4-4616-977a-0639f59a1d54)

## Technologies

### Backend
- Spring Boot
- Spring Data JPA + Hibernate
- H2 database
- WebClient (Spring WebFlux)
- Lombok
- Jackson
- Maven

### Frontend
- React

### Deployment
- Docker
- Heroku

## Prerequisites
- Java 17
- Node.js 18
- Maven 3.9+
- Docker (optional)

## Getting Started
1. Clone the repository
```
git clone https://github.com/kajusviliusis/comment-to-ticket.git
```
2. Provide the required environment variable:
- `HUGGINGFACE_API_KEY`=<your_hf_api_key>

3. Run the Backend
```
mvn spring-boot:run
```
Backend will start at:
http://localhost:8080/

Run the Frontend
```
cd frontend
npm install
npm run dev
```
Frontend will start at:
http://localhost:5173/

### Running with Docker
1. Build the image
```
docker build -t pulsedesk .
```
2. Start the container
```
docker run -p 8080:8080 -e HUGGINGFACE_API_KEY=<your_key> pulsedesk
```
- Application will start at:
http://localhost:8080/

Access Swagger at:
http://localhost:8080/swagger-ui.html

