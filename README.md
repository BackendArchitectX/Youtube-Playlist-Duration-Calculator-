# YouTube Playlist Duration Calculator

<div align="center">

![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**A Spring Boot REST API that calculates YouTube playlist duration**  
Get total watch time + estimated time at different playback speeds (1.25x, 1.5x, 1.75x, 2.0x)

[🚀 Quick Start](#-quick-start) • [📖 Usage](#-usage) • [🔧 Config](#-configuration) • [❓ Issues](#-common-issues) • [📚 Docs](#-api-documentation)

</div>

---

## 🎯 What It Does

Input a YouTube **playlist ID** → Get back **total duration** + watch time estimates at different speeds.

**Perfect for:** Planning learning time, deciding if a course is worth watching, optimizing study hours.

```txt
Input:  PL0zysOflRCekMr91amXBNwWku4PmeFaFD

Output:
  📺 Normal (1x):   9 hours, 26 minutes, 25 seconds
  ⚡ Speed 1.25x:   7 hours, 33 minutes, 8 seconds
  ⚡ Speed 1.5x:    6 hours, 17 minutes, 36 seconds
  ⚡ Speed 1.75x:   5 hours, 23 minutes, 40 seconds
  🚀 Speed 2x:      4 hours, 43 minutes, 12 seconds
🚀 Quick Start
1️⃣ Prerequisites
Java 17+ → Download

Maven 3.6+ → Download

YouTube API Key → Get free key

2️⃣ Clone & Setup
bash
# Clone repository
git clone https://github.com/BackendArchitectX/Youtube-Playlist-Duration-Calculator-.git
cd Youtube-Playlist-Duration-Calculator-/demo

# Create config file with API key
echo "youtube.api.key=YOUR_API_KEY_HERE" > src/main/resources/application.properties

# Build project
mvn clean install

# Run application
mvn spring-boot:run
✅ Done! API is running on http://localhost:8080

📖 Usage
Making a Request
bash
GET http://localhost:8080/api/playlist/{playlistId}
Example:

bash
curl http://localhost:8080/api/playlist/PL0zysOflRCekMr91amXBNwWku4PmeFaFD
Example Response
json
{
  "totalLength": "0 days, 9 hours, 26 minutes, 25 seconds",
  "at1_25x": "0 days, 7 hours, 33 minutes, 8 seconds",
  "at1_50x": "0 days, 6 hours, 17 minutes, 36 seconds",
  "at1_75x": "0 days, 5 hours, 23 minutes, 40 seconds",
  "at2_00x": "0 days, 4 hours, 43 minutes, 12 seconds"
}
🔧 Configuration
Set Your API Key
Option 1: Properties File (Recommended)

text
# src/main/resources/application.properties
youtube.api.key=YOUR_API_KEY_HERE
server.port=8080
Option 2: Environment Variable

bash
# Linux/Mac
export YOUTUBE_API_KEY=YOUR_API_KEY_HERE
mvn spring-boot:run
powershell
# Windows (PowerShell)
$env:YOUTUBE_API_KEY="YOUR_API_KEY_HERE"
mvn spring-boot:run
Change Port
text
# src/main/resources/application.properties
server.port=8081
🚀 Deployment
As JAR File
bash
# Build executable JAR
mvn clean package

# Run the JAR
java -jar target/demo-0.0.1-SNAPSHOT.jar
With Docker
Dockerfile:

text
FROM openjdk:17-jre-slim
WORKDIR /app
COPY target/*.jar app.jar
ENV YOUTUBE_API_KEY=${YOUTUBE_API_KEY}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
Build and Run:

bash
# Build Docker image
docker build -t youtube-playlist-calculator .

# Run container
docker run -e YOUTUBE_API_KEY=YOUR_KEY -p 8080:8080 youtube-playlist-calculator
📚 API Documentation
Endpoint
text
GET /api/playlist/{playlistId}
Path Parameters
Parameter	Type	Required	Description
playlistId	String	Yes	YouTube playlist ID
Response Fields
Field	Type	Description
totalLength	String	Total watch time at normal speed (1x)
at1_25x	String	Estimated time at 1.25x speed
at1_50x	String	Estimated time at 1.5x speed
at1_75x	String	Estimated time at 1.75x speed
at2_00x	String	Estimated time at 2x speed
Status Codes
Code	Meaning
200	✅ Success - Playlist duration calculated
400	❌ Bad Request - Invalid playlist ID
401	❌ Unauthorized - API key missing/invalid
403	❌ Forbidden - API quota exceeded
404	❌ Not Found - Playlist doesn't exist
500	❌ Internal Server Error
❓ Common Issues
🔴 401 "Unauthorized"
Problem: Your YouTube API key is missing, invalid, or not loaded by the app.

Solution:

bash
# Verify API key in application.properties
cat src/main/resources/application.properties

# If missing, add it:
echo "youtube.api.key=YOUR_ACTUAL_KEY" > src/main/resources/application.properties

# Restart application
mvn spring-boot:run
🔴 403 "Quota Exceeded"
Problem: You've exceeded the YouTube API daily quota (10,000 units/day by default).

Solution:

Check quota usage in Google Cloud Console

Wait until quota resets (resets daily at midnight Pacific Time)

Request quota increase if needed

🔴 Port 8080 Already in Use
Problem: Another application is using port 8080.

Solution:

text
# src/main/resources/application.properties
server.port=8081
Or kill the process:

bash
# Linux/Mac
lsof -ti:8080 | xargs kill -9

# Windows (PowerShell)
netstat -ano | findstr :8080
taskkill /PID <PID> /F
🔴 Maven Command Not Found
Problem: Maven is not installed or not in PATH.

Solution:

bash
# Check if Maven is installed
mvn -version

# If not installed, download from:
# https://maven.apache.org/download.cgi

# Add Maven to PATH (Linux/Mac)
export PATH=/path/to/maven/bin:$PATH

# Add Maven to PATH (Windows)
# Add C:\path\to\maven\bin to System Environment Variables
📁 Project Structure
text
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/        # REST endpoints
│   │   │   ├── service/           # Business logic
│   │   │   ├── model/             # DTOs & response models
│   │   │   ├── exception/         # Custom exceptions & global handlers
│   │   │   └── config/            # Configuration classes
│   │   └── resources/
│   │       ├── static/            # Frontend (HTML/CSS/JS)
│   │       └── application.properties
│   └── test/
│       └── java/                  # Unit & integration tests
└── pom.xml                        # Maven dependencies
🛠️ Technology Stack
Layer	Technology	Purpose
Backend	Java 17	Modern language features (records, pattern matching)
Spring Boot 3.2.2	Application framework & auto-configuration
Spring Web	RESTful web services
YouTube Data API v3	Fetch playlist & video metadata
Frontend	HTML5/CSS3/JavaScript	Responsive user interface
Fetch API	Asynchronous HTTP requests
Build	Maven	Dependency management & build automation
Deployment	Docker	Containerization & portability
🎨 Design Patterns
MVC (Model-View-Controller): Clear separation of concerns

Dependency Injection: Loose coupling via Spring IoC container

DTO Pattern: Data transfer between layers

RESTful Architecture: Stateless client-server communication

Exception Handling: Global exception handlers for consistent error responses

💡 Pro Tips
📌 Bookmark Playlists - Save playlist IDs to check durations anytime
⚡ Speed Up Learning - Use 1.5x-2x speed for faster content consumption
🔑 Secure API Keys - Never commit keys to Git; use environment variables in production
📱 Easy Integration - Simple REST API works with any programming language
🎯 Batch Processing - Check multiple playlists to plan your learning schedule

🤝 Contributing
Found a bug? Want to add features? Contributions are welcome!

bash
# 1. Fork the repository
# 2. Create feature branch
git checkout -b feature/AmazingFeature

# 3. Commit changes
git commit -m 'Add AmazingFeature'

# 4. Push to branch
git push origin feature/AmazingFeature

# 5. Open Pull Request
Please ensure:

Code follows Spring Boot best practices

Unit tests are included

Documentation is updated

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

You are free to use this project for:

✅ Commercial purposes

✅ Personal projects

✅ Educational purposes

✅ Modification and distribution

🔗 Useful Links
📺 YouTube Data API → https://developers.google.com/youtube/v3/docs
🚀 Spring Boot Docs → https://docs.spring.io/spring-boot/docs/
🏗️ Maven Documentation → https://maven.apache.org/guides/
☕ Java Tutorials → https://docs.oracle.com/javase/tutorial/
🐳 Docker Hub → https://hub.docker.com/

📧 Support & Contact
💬 Report Issues → GitHub Issues
👨‍💻 GitHub Profile → @BackendArchitectX
📧 Email → Open an issue for support

<div align="center">
Made with ❤️ by BackendArchitectX

⭐ If this project helps you, please star the repository! ⭐

⬆ Back to Top

</div> ```