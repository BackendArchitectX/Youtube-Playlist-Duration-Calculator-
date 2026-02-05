# YouTube Playlist Duration Calculator

<p align="center">
  <a href="#"><img src="https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white" alt="YouTube"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot" alt="Spring Boot"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java"/></a>
  <a href="#"><img src="https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven" alt="Maven"/></a>
  <a href="#"><img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License"/></a>
</p>

**A Spring Boot REST API that calculates YouTube playlist duration** — get total watch time + estimated time at different playback speeds (1.25x, 1.5x, 1.75x, 2.0x)

---

## Table of Contents

- [Demo](#demo)
- [What It Does](#what-it-does)
- [Quick Start](#quick-start)
- [Usage](#usage)
- [Configuration](#configuration)
- [Deployment](#deployment)
- [API Documentation](#api-documentation)
- [Common Issues](#common-issues)
- [Project Structure](#project-structure)
- [Technology Stack](#technology-stack)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

---

## Demo

> Quick animated demo (placeholder). Replace `assets/demo.gif` with your real demo GIF.

<p align="center">
  <img src="assets/demo.gif" alt="Demo GIF" width="700"/>
</p>

### Screenshots

<p align="center">
  <img src="assets/screenshot-1.png" alt="Screenshot 1" width="600" style="margin: 10px;"/>
  <img src="assets/screenshot-2.png" alt="Screenshot 2" width="600" style="margin: 10px;"/>
</p>

---

## What It Does

Input a YouTube **playlist ID** → Get back **total duration** + watch time estimates at different speeds.

**Perfect for:** Planning learning time, deciding if a course is worth watching, optimizing study hours.

**Example**

```
Input:  PL0zysOflRCekMr91amXBNwWku4PmeFaFD

Output:
  📺 Normal (1x):   9 hours, 26 minutes, 25 seconds
  ⚡ Speed 1.25x:   7 hours, 33 minutes, 8 seconds
  ⚡ Speed 1.5x:    6 hours, 17 minutes, 36 seconds
  ⚡ Speed 1.75x:   5 hours, 23 minutes, 40 seconds
  🚀 Speed 2x:      4 hours, 43 minutes, 12 seconds
```

---

## Quick Start

### Prerequisites

- Java 17+  
- Maven 3.6+  
- YouTube Data API v3 key

### Clone & Setup

```bash
git clone https://github.com/BackendArchitectX/Youtube-Playlist-Duration-Calculator-.git
cd Youtube-Playlist-Duration-Calculator-/demo

# Create config file with API key
echo "youtube.api.key=YOUR_API_KEY_HERE" > src/main/resources/application.properties

# Build
mvn clean install

# Run
mvn spring-boot:run
```

API will be available at `http://localhost:8080`.

---

## Usage

**Request**

```bash
GET http://localhost:8080/api/playlist/{playlistId}
```

**Example**

```bash
curl http://localhost:8080/api/playlist/PL0zysOflRCekMr91amXBNwWku4PmeFaFD
```

**Response**

```json
{
  "totalLength": "0 days, 9 hours, 26 minutes, 25 seconds",
  "at1_25x": "0 days, 7 hours, 33 minutes, 8 seconds",
  "at1_50x": "0 days, 6 hours, 17 minutes, 36 seconds",
  "at1_75x": "0 days, 5 hours, 23 minutes, 40 seconds",
  "at2_00x": "0 days, 4 hours, 43 minutes, 12 seconds"
}
```

---

## Configuration

### Properties File (recommended)

```properties
# src/main/resources/application.properties
youtube.api.key=YOUR_API_KEY_HERE
server.port=8080
```

### Environment Variable

```bash
# Linux/Mac
export YOUTUBE_API_KEY=YOUR_API_KEY_HERE
mvn spring-boot:run

# Windows PowerShell
$env:YOUTUBE_API_KEY="YOUR_API_KEY_HERE"
mvn spring-boot:run
```

---

## Deployment

### As JAR

```bash
mvn clean package
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

### Docker

Dockerfile (example):

```dockerfile
FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/*.jar app.jar
ENV YOUTUBE_API_KEY=${YOUTUBE_API_KEY}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t youtube-playlist-calculator .
docker run -e YOUTUBE_API_KEY=YOUR_KEY -p 8080:8080 youtube-playlist-calculator
```

---

## API Documentation

**Endpoint**

```
GET /api/playlist/{playlistId}
```

**Path Parameters**

| Name      | Type   | Required | Description          |
|-----------|--------|----------|----------------------|
| playlistId| String | Yes      | YouTube playlist ID  |

**Response Fields**

| Field       | Type   | Description                              |
|-------------|--------|------------------------------------------|
| totalLength | String | Total watch time at normal speed (1x)    |
| at1_25x     | String | Estimated time at 1.25x speed            |
| at1_50x     | String | Estimated time at 1.5x speed             |
| at1_75x     | String | Estimated time at 1.75x speed            |
| at2_00x     | String | Estimated time at 2x speed               |

**Status Codes**

- `200` — Success  
- `400` — Bad Request (invalid playlist ID)  
- `401` — Unauthorized (API key missing/invalid)  
- `403` — Forbidden (quota exceeded)  
- `404` — Not Found (playlist doesn't exist)  
- `500` — Internal Server Error

---

## Common Issues

### 401 Unauthorized

Check `src/main/resources/application.properties` for `youtube.api.key`. Restart the app after updating.

### 403 Quota Exceeded

Check quota in Google Cloud Console. Request quota increase if needed.

### Port in Use

Change `server.port` in `application.properties` or kill the process using the port.

### Maven Not Found

Install Maven and ensure `mvn` is in your `PATH`.

---

## Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── model/
│   │   │   ├── exception/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── static/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

## Technology Stack

- Java 17  
- Spring Boot 3.2.2  
- Spring Web  
- YouTube Data API v3  
- Maven  
- Docker

---

## Contributing

1. Fork the repo  
2. Create a branch: `git checkout -b feature/AmazingFeature`  
3. Commit: `git commit -m "Add AmazingFeature"`  
4. Push & open PR

Please include unit tests and update docs.

---

## License

MIT — see LICENSE file.

---

## Contact

- GitHub: @BackendArchitectX  
- Issues: Use GitHub Issues for bug reports and feature requests

---

<p align="center">
Made with ❤️ by BackendArchitectX — ⭐ If this project helps you, please star the repo!
</p>
