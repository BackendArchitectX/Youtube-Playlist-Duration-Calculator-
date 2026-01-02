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

```
Input:  PL0zysOflRCekMr91amXBNwWku4PmeFaFD

Output: 
  📺 Normal (1x):   9 hours, 26 minutes, 25 seconds
  ⚡ Speed 1.25x:   7 hours, 33 minutes, 8 seconds
  ⚡ Speed 1.5x:    6 hours, 17 minutes, 36 seconds
  ⚡ Speed 1.75x:   5 hours, 23 minutes, 40 seconds
  🚀 Speed 2x:     4 hours, 43 minutes, 12 seconds
```

---

## 🚀 Quick Start (5 Minutes)

### 1️⃣ Prerequisites

- **Java 11+** → [Download](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven 3.6+** → [Download](https://maven.apache.org/download.cgi)
- **YouTube API Key** → [Get free key](https://console.cloud.google.com/)

### 2️⃣ Clone & Setup

```bash
# Clone repository
git clone https://github.com/BackendArchitectX/Youtube-Playlist-Duration-Calculator-.git
cd Youtube-Playlist-Duration-Calculator-/demo

# Create config file with API key
echo "youtube.api.key=YOUR_API_KEY_HERE" > src/main/resources/application.properties

# Build project
mvn clean install

# Run application
mvn spring-boot:run
```

### 3️⃣ Test It

```bash
curl "http://localhost:8080/api/playlist/duration?playlistId=PL0zysOflRCekMr91amXBNwWku4PmeFaFD"
```

✅ **Done!** API is running on `http://localhost:8080`

---

## 📖 Usage

### The Endpoint

```
GET /api/playlist/duration?playlistId=YOUR_PLAYLIST_ID
```

### How to Get Playlist ID

1. Open any YouTube playlist
2. Copy the URL: `https://www.youtube.com/playlist?list=PLxxxxxxxxxxxx`
3. The `PLxxxxxxxxxxxx` part is your **Playlist ID**

**Valid examples:**
- ✅ `PL0zysOflRCekMr91amXBNwWku4PmeFaFD`
- ✅ `PLDIoUOhQQPlWqMCnVYNLXuVMplWBVmMt-`
- ❌ `jdBYwqmMMxU` (This is a video ID, not playlist ID)

### Example Request

```bash
curl "http://localhost:8080/api/playlist/duration?playlistId=PL0zysOflRCekMr91amXBNwWku4PmeFaFD"
```

### Example Response

```json
{
  "totalLength": "0 days, 9 hours, 26 minutes, 25 seconds",
  "at1_25x": "0 days, 7 hours, 33 minutes, 8 seconds",
  "at1_50x": "0 days, 6 hours, 17 minutes, 36 seconds",
  "at1_75x": "0 days, 5 hours, 23 minutes, 40 seconds",
  "at2_00x": "0 days, 4 hours, 43 minutes, 12 seconds"
}
```

### JavaScript Example

```javascript
async function getPlaylistDuration(playlistId) {
  const response = await fetch(
    `http://localhost:8080/api/playlist/duration?playlistId=${playlistId}`
  );
  const data = await response.json();
  
  console.log("Total:", data.totalLength);
  console.log("At 2x speed:", data.at2_00x);
}

getPlaylistDuration('PL0zysOflRCekMr91amXBNwWku4PmeFaFD');
```

### Python Example

```python
import requests

response = requests.get(
    'http://localhost:8080/api/playlist/duration',
    params={'playlistId': 'PL0zysOflRCekMr91amXBNwWku4PmeFaFD'}
)
data = response.json()

print(f"Total: {data['totalLength']}")
print(f"At 2x: {data['at2_00x']}")
```

---

## 🔧 Configuration

### Set Your API Key

**Option 1: Properties File** (recommended)
```properties
# src/main/resources/application.properties
youtube.api.key=YOUR_API_KEY_HERE
server.port=8080
```

**Option 2: Environment Variable**
```bash
# Linux/Mac
export YOUTUBE_API_KEY=YOUR_API_KEY_HERE
mvn spring-boot:run

# Windows (PowerShell)
$env:YOUTUBE_API_KEY="YOUR_API_KEY_HERE"
mvn spring-boot:run
```

### Change Port

```properties
# src/main/resources/application.properties
server.port=8081
```

---

## 🚀 Deployment

### As JAR File

```bash
mvn clean package
java -jar target/*.jar
```

### With Docker

```dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY target/*.jar app.jar
ENV YOUTUBE_API_KEY=${YOUTUBE_API_KEY}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t youtube-calc .
docker run -e YOUTUBE_API_KEY=YOUR_KEY -p 8080:8080 youtube-calc
```

---

## 📚 API Documentation

### Response Fields

| Field | Meaning |
|-------|---------|
| `totalLength` | Total watch time at normal speed (1x) |
| `at1_25x` | Estimated time if you watch at 1.25x speed |
| `at1_50x` | Estimated time if you watch at 1.5x speed |
| `at1_75x` | Estimated time if you watch at 1.75x speed |
| `at2_00x` | Estimated time if you watch at 2x speed |

### Status Codes

| Code | Meaning |
|------|---------|
| 200 | ✅ Success |
| 400 | ❌ Invalid playlist ID |
| 401 | ❌ API key missing/invalid |
| 403 | ❌ API quota exceeded |
| 500 | ❌ Server error |

---

## ❓ Common Issues

### 400 "Invalid playlistId"
**Problem:** You're using a video ID instead of a playlist ID

**Solution:**
- ❌ Wrong: `jdBYwqmMMxU` (11 chars, single video)
- ✅ Right: `PL0zysOflRCekMr91amXBNwWku4PmeFaFD` (34+ chars, playlist)

### 401 "Unauthorized"
**Problem:** API key not set or invalid

**Solution:**
```bash
# Check application.properties has correct key
cat src/main/resources/application.properties

# If missing, add it:
echo "youtube.api.key=YOUR_ACTUAL_KEY" > src/main/resources/application.properties
mvn spring-boot:run
```

### 403 "Quota Exceeded"
**Problem:** You've hit the YouTube API daily quota

**Solution:**
- Check quota in [Google Cloud Console](https://console.cloud.google.com/)
- Wait until quota resets (typically daily)
- Request quota increase if needed

### Port 8080 Already in Use
**Problem:** Another app is using port 8080

**Solution:**
```properties
# src/main/resources/application.properties
server.port=8081
```

### Maven Command Not Found
**Problem:** Maven not installed

**Solution:**
```bash
mvn -version  # Check if installed

# If not, download from:
# https://maven.apache.org/download.cgi
```

---

## 📁 Project Structure

```
demo/
├── src/main/java/com/youtubeplaylistduration/
│   ├── YoutubePlaylistDurationApplication.java
│   ├── controller/
│   │   └── PlaylistController.java
│   ├── service/
│   │   └── PlaylistService.java
│   └── model/
│       └── PlaylistResponse.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

---

## 🛠️ Tech Stack

- **Framework:** Spring Boot 3.x
- **Language:** Java 11+
- **Build:** Maven 3.6+
- **API:** YouTube Data API v3
- **Server:** Apache Tomcat (embedded)

---

## 💡 Pro Tips

- 📌 **Save URLs with IDs** - Bookmark requests to check playlists anytime
- ⚡ **Use 2x speed** - Save time on slower videos
- 🔑 **Never commit API keys** - Use environment variables in production
- 📱 **Integrate easily** - Use JavaScript/Python examples in your apps

---

## 🤝 Contributing

Found a bug? Want to improve it? We'd love your help!

```bash
1. Fork the repository
2. Create a feature branch: git checkout -b feature/YourFeature
3. Commit changes: git commit -m 'Add YourFeature'
4. Push to branch: git push origin feature/YourFeature
5. Open a Pull Request
```

---

## 📄 License

MIT License - See [LICENSE](LICENSE) for details.

You can use this project for **commercial**, **personal**, or **educational** purposes!

---

## 🔗 Useful Links

- 📺 **YouTube API** → https://developers.google.com/youtube/v3/docs
- 🚀 **Spring Boot** → https://docs.spring.io/spring-boot/docs/
- 🏗️ **Maven** → https://maven.apache.org/guides/
- ☕ **Java** → https://docs.oracle.com/javase/tutorial/

---

## 📧 Support & Contact

- **Report Issues** → [GitHub Issues](https://github.com/BackendArchitectX/Youtube-Playlist-Duration-Calculator-/issues)
- **GitHub** → [BackendArchitectX](https://github.com/BackendArchitectX)
- **LinkedIn** → [Your Profile](https://linkedin.com/in/yourprofile)

---

<div align="center">

**Made with ❤️ by BackendArchitectX**

⭐ **If this helps you, please star the repository!** ⭐

[⬆ Back to Top](#youtube-playlist-duration-calculator)

</div>