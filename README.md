 Nurthure Monitor

A modern web-based SIDS (Sudden Infant Death Syndrome) monitoring prototype application designed to provide real-time infant monitoring with a calming, anxiety-reducing interface.

## 🌟 Features

- **Live Monitoring** - Real-time vital signs display with intuitive visual indicators
- **Smart Alerts** - Configurable alert system with clear, actionable notifications
- **Trend Analysis** - Historical data visualization for pattern recognition
- **AI Integration** - Gemini AI-powered insights and recommendations
- **Data Export** - Export monitoring data for healthcare provider review
- **Raspberry Pi Connection** - Connect to Raspberry Pi-based sensor hardware

## 🎨 Design Philosophy

- **Reduces anxiety** - Calm, reassuring interface design
- **Shows state without judgment** - Neutral, informative displays
- **Avoids diagnosis** - Information only, no medical conclusions
- **Never overrides hardware** - Respects device-level safety decisions

## 🚀 Getting Started

### Prerequisites

- Modern andriod app (Chrome, Firefox, Safari, Edge)
- Raspberry Pi with sensors (for hardware integration)

### Running Locally

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/nurthure-monitor.git
   ```

2. Open `index.html` in your browser, or serve with a local server:
   ```bash
   # Using Python
   python -m http.server 8000
   
   # Using Node.js
   npx serve .
   ```

3. Navigate to `http://localhost:8000`

## 📁 Project Structure

```
nurthure-monitor/
├── index.html          # Main HTML file
├── styles.css          # Global styles
├── app.js              # Main application logic
└── js/
    ├── alerts.js       # Alert management
    ├── connection.js   # Device connection handling
    ├── export.js       # Data export functionality
    ├── gemini.js       # Gemini AI integration
    ├── storage.js      # Local data storage
    └── trends.js       # Trend analysis
```

## 🔧 Configuration

Configure device connection settings in the Settings panel within the app.

## 📱 Related Projects

- **Nurthure Android App** - Native Android implementation using Kotlin & Jetpack Compose

## 📄 License

This project is for educational and prototype purposes.

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

---

Made with ❤️ for infant safety

