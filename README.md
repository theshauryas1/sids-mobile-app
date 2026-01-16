# Nurture Monitor

A modern Android application for monitoring and tracking health metrics with AI-powered insights using Google Generative AI (Gemini).

## Project Overview

Nurture Monitor is an Android application built with Kotlin and Jetpack Compose. It provides users with a comprehensive platform to monitor health-related data, visualize trends through interactive charts, and receive intelligent insights powered by Google's Generative AI.

## Features

- **Health Data Tracking**: Monitor and record health metrics
- **Data Visualization**: Interactive charts built with Vico for visualizing health trends
- **AI-Powered Insights**: Leverages Google Generative AI (Gemini) for intelligent health analysis
- **Local Database**: Uses Room database for efficient data persistence
- **Preferences Management**: DataStore for secure preference storage
- **Responsive UI**: Built entirely with Jetpack Compose for a modern, responsive user interface
- **Network Communication**: Ktor HTTP client for backend communication

## Tech Stack

### Architecture & Framework
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Navigation**: Jetpack Navigation Compose
- **Dependency Injection**: Hilt (Dagger)

### Core Libraries
- **Local Database**: Room Database (2.6.1)
- **HTTP Client**: Ktor (2.3.8)
- **Serialization**: Kotlinx Serialization JSON
- **Data Storage**: DataStore Preferences
- **Charts**: Vico (1.13.1)
- **AI Integration**: Google Generative AI Client (0.2.2)

### Android Framework
- **Minimum SDK**: 26
- **Target SDK**: 34 (Android 14)
- **Java Compatibility**: Java 17
- **Compose Compiler**: 1.5.8

### Testing
- **Unit Testing**: JUnit 4
- **Instrumentation Testing**: Android Test Runner

## Project Structure

```
nurture app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/          # Kotlin source code
│   │       ├── res/           # Resources (layouts, strings, colors, etc.)
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts       # App-level build configuration
│   └── proguard-rules.pro      # ProGuard rules
├── build.gradle.kts           # Root-level build configuration
├── settings.gradle.kts        # Gradle settings
├── gradle.properties          # Gradle properties
├── local.properties           # Local development properties
└── gradle/                    # Gradle wrapper
```

## Getting Started

### Prerequisites
- Android Studio (latest version recommended)
- JDK 17 or higher
- Gradle 8.x or higher
- Google Generative AI API key (for Gemini features)

### Installation

1. **Clone the Repository**
   ```bash
   git clone <repository-url>
   cd nurture-app
   ```

2. **Setup Local Properties**
   Create a `local.properties` file in the root directory:
   ```
   sdk.dir=/path/to/android/sdk
   ```

3. **Configure API Keys**
   Add your Google Generative AI API key to your local configuration or build environment.

4. **Build the Project**
   ```bash
   ./gradlew build
   ```

5. **Run the Application**
   ```bash
   ./gradlew installDebug
   ```

## Permissions

The application requires the following permissions:
- `INTERNET`: For API communication
- `ACCESS_NETWORK_STATE`: To check network connectivity
- `ACCESS_WIFI_STATE`: To monitor WiFi status
- `VIBRATE`: For haptic feedback
- `POST_NOTIFICATIONS`: For sending notifications

## Key Modules

### MainActivity
The main entry point of the application, which sets up the navigation and UI.

### NurthureApp
Custom Application class that initializes Hilt dependency injection.

## API Integration

The application uses Ktor HTTP Client for backend communication with the following features:
- Content negotiation with Kotlinx Serialization
- Automatic JSON serialization/deserialization
- Network error handling

## Database

Room database is used for local data persistence with the following features:
- Type-safe database access
- Automatic migrations support
- Coroutine support for async database operations

## Build Configuration

### Build Types
- **Debug**: Full debugging capabilities
- **Release**: Optimized with ProGuard rules enabled

### Compose Configuration
- Kotlin Compiler Extension: 1.5.8
- Targets modern Compose features and Material3 design

## Supported Android Versions

| Version | Android Level |
|---------|---------------|
| Minimum | 26 (Android 8.0) |
| Target  | 34 (Android 14) |

## Development

### Code Style
- Kotlin with coroutine-first approach
- Material3 design patterns
- MVVM architecture with Compose

### Dependency Injection
Hilt is used for dependency injection:
- Module setup in application class
- Compose integration via `@AndroidEntryPoint`
- Hilt Navigation Compose for screen-scoped dependencies

## Known Issues & Limitations

- Currently uses cleartext traffic for local development (not recommended for production)
- ProGuard optimization is disabled in release builds

## Contributing

When contributing to this project:
1. Follow Kotlin coding conventions
2. Use Compose best practices
3. Maintain test coverage
4. Document significant changes

## Future Enhancements

- [ ] Enhanced chart customization
- [ ] Offline mode support
- [ ] Push notifications
- [ ] Advanced analytics
- [ ] Export data functionality

## Support

For issues, questions, or suggestions, please reach out or open an issue in the repository.

## License

[Add your license information here]

---

**Project Name**: Nurture Monitor  
**Version**: 1.0  
**Target Platform**: Android 8.0+
