# 🎄 Christmas Grapes AI 🍇

<p align="center">
  <b>Make wishes, track progress, and watch your dreams become reality throughout the year!</b>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#architecture">Architecture</a> •
  <a href="#installation">Installation</a> •
  <a href="#usage">Usage</a> •
  <a href="#roadmap">Roadmap</a> •
  <a href="#license">License</a>
</p>

ChristmasGrapes is a Kotlin Multiplatform application that helps you make wishes and track them throughout the year. It's like New Year's resolutions but more fun and interactive!

Inspired by the tradition of making wishes for each month, this app allows users to generate AI-powered wishes, save their personal wishes, and review them over time.

This is a Kotlin Multiplatform project that uses 100% Compose UI and targets:
1. Android
2. iOS

## 📱 Features

🌟 **Key Features:**

- 🍇 **AI Wish Generation**: Click the Grape and generate a wish using OpenAI API
- ✨ **Wish Creation**: Create and customize your own wishes
- 📅 **Monthly Organization**: Assign wishes to specific months
- 📊 **Progress Tracking**: Mark wishes as completed and track your progress
- 📈 **Annual Summary**: View a comprehensive summary of your wishes and achievements
- 📤 **Social Sharing**: Share your wishes on social media or with friends
- 💫 **Beautiful UI**: Enjoy a delightful user interface with animations and visual feedback
- 📱 **Cross-Platform**: Works on Android and iOS with the same codebase

## 📷 Screenshots

### Android

| <img src="https://github.com/user-attachments/assets/3044ad5b-b171-40e3-a497-6d991e58d3b1" width="250"> | <img src="https://github.com/user-attachments/assets/ecefb046-978e-45d4-81ed-61664c1a9821" width="250"> | <img src="https://github.com/user-attachments/assets/a91cb9bc-0ac8-4d27-a09a-96ae818de5ed" width="250"> | <img src="https://github.com/user-attachments/assets/284ec2e5-e065-478d-99d8-eb915c68a426" width="250"> | <img src="https://github.com/user-attachments/assets/e547ce86-407c-4a92-b497-4190b8cc5375" width="250">
|---|---|---|---|---|


### iOS

| <img src="https://github.com/user-attachments/assets/82c3e960-1d3c-40b1-8bf8-0c014c3aee71" width="250"> | <img src="https://github.com/user-attachments/assets/5acaf410-4223-458b-a7a0-4613234eed53" width="250"> | <img src="https://github.com/user-attachments/assets/f4e1b210-6dcd-4e8a-bb4a-add63f01b293" width="250"> | <img src="https://github.com/user-attachments/assets/3603473d-8d2d-47d1-9e35-527cd1e578a3" width="250"> | <img src="https://github.com/user-attachments/assets/2d7ec42f-da35-45e5-9bd5-54423bc0a690" width="250">
|---|---|---|---|---|


## 🛠️ Tech Stack

This app is built with modern technologies and practices:

- **[Kotlin Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)**: Single codebase & Share UI for Android and iOS
- **[MVVM Architecture](https://developer.android.com/topic/architecture)**: Separation of UI, business logic and data
- **[Coroutines & Flow](https://kotlinlang.org/docs/coroutines-overview.html)**: Asynchronous programming
- **[Ktor](https://ktor.io/)**: Networking library for API calls and response parsing
- **[Kotlinx.Serialization](https://github.com/Kotlin/kotlinx.serialization)**: JSON serialization/deserialization
- **[Material3](https://m3.material.io/)**: Modern material design components
- **[Supabase](https://supabase.io/)**: Database and backend services
- **[SQLDelight](https://cashapp.github.io/sqldelight/)**: Type-safe database access
- **[Koin](https://insert-koin.io/)**: Dependency injection
- **[OpenAI API](https://openai.com/api/)**: AI-powered wish generation

## 🏗️ Architecture

The application follows the MVVM (Model-View-ViewModel) architecture pattern with a clean separation of concerns:

```
christmasgrapes/
├── model/             # Data models and entities
├── repository/        # Data access layer
├── viewmodel/         # ViewModels for business logic
├── ui/                # UI components and screens
│   ├── components/    # Reusable UI components
│   ├── screens/       # Main application screens
│   └── theme/         # Theme definitions
├── util/              # Utility functions and extensions
├── di/                # Dependency injection modules
└── db/                # Database configurations and queries
```

## 🚀 Installation

### Prerequisites

- Android Studio
- Xcode 13 or newer (for iOS)
- JDK 17 or newer

### Building from Source

1. Clone the repository:

2. Open the project in Android Studio

3. Configure your local.properties file with the required API keys:
```properties
openai.api.key=your_openai_key
supabase.api.key=your_supabase_key
supabase.url.key=your_supabase_url
```

4. Build the project:
```bash
./gradlew build
```

### Running the App

- **Android**: Run on an Android device or emulator using Android Studio
- **iOS**: Open the iosApp folder in Xcode and run on a simulator or device

## 🎮 Usage

1. **Home Screen**: Tap the grapes to generate a random wish using AI
2. **Make Wish Screen**: Create a custom wish and assign it to a month
3. **Wishes Screen**: View all your wishes organized by month
4. **Month Detail Screen**: See and manage wishes for a specific month
5. **Summary Screen**: View your annual summary and progress

## 🔮 Roadmap

We're constantly working to improve Christmas Grapes. Some planned features include:

- 🔔 **Reminders**: Get notified about wishes as their assigned month approaches
- 👥 **Advanced Social Sharing**: More options for sharing your wishes and achievements
- 📊 **Enhanced Statistics**: More detailed tracking and visualization of progress
- 🎨 **Customizable Themes**: Choose from various visual themes
- 🖥️ **Desktop & Web Support**: Expand to desktop and web platforms

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<p align="center">
  Made by <a href="https://github.com/CagriOrcan">Çağrı Orcan</a>
</p>

<p align="center">
  Stay tuned for more updates! 🎄🍇
</p>
