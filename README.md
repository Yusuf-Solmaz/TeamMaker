<h1 align="center">
Team Maker  
</h1>

<p align="center">
  <img src="https://img.shields.io/badge/-Kotlin-7c6fe1?style=flat&logo=kotlin&logoColor=white">
  <img src="https://img.shields.io/badge/Jetpack_Compose-4285f4?style=flat&logo=jetpackcompose&logoColor=white">
  
  Team Maker is an application designed to streamline the creation of balanced teams with fair skill distribution for various sports. Whether you're setting up a competitive event or organizing a casual game, Team Maker provides an intuitive platform to manage teams and competitions effectively.
</p>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#key-features">Key Features</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
        <li><a href="#configuration-steps">Configuration Steps</a></li>
      </ul>
    </li>
    <li><a href="#screenshots">Screenshots</a></li>
    <li><a href="#demo">Demo</a></li>
    <li><a href="#open-source-libraries">Open-Source Libraries</a></li>
    <li><a href="#architecture">Architecture</a></li>
    <li><a href="#api-and-services">API and Services</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#licence">Licence</a></li>
  </ol>
</details>

<!-- KEY FEATURES -->
## Key Features
* Create Competitions: Easily set up matches for a variety of sports such as football, volleyball, basketball, and more. 
* Add and Manage Players: Add players to your roster with detailed profiles, including:
  * Profile Picture
  * First Name
  * Last Name
  * Position
  * Skill Rating (used to balance teams)
* Update and Delete Players: Modify player details or remove players as needed.
* Form Matches: Select players from your list to create teams and organize matches.
* View Match Details: Access comprehensive details of created competitions, including team compositions.
* Check Location and Time: View essential location and time information for your events.
* Weather Information: Get weather forecasts based on the location of your competition to better plan your events.

With Team Maker, you can register, log in, and customize your experience by adding games and participants. Build balanced teams, track your competitions, and ensure a fair and enjoyable gaming experience. Start organizing your sports events and creating balanced teams with Team Maker!

<!-- Screenshots -->
## Screenshots
| On Boarding Page First          | Sign Up                      | Sign In                     | Reset Password          | Choose Competition        | Update Competition        | Add Competition  
|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|
| <img src="/Screenshots/Screenshot_onboarding_page_first.png" width="180"/> | <img src="/Screenshots/Screenshot_create_an_account_page.png" width="260"/> |<img src="/Screenshots/Screenshot_login_page.png" width="260"/> | <img src="/Screenshots/Screenshot_reset_password_screen.png" width="180"/> | <img src="/Screenshots/Screenshot_choose_competition_type.png" width="140"/> | <img src="/Screenshots/Screenshot_update_competition.png" width="140"/> | <img src="/Screenshots/Screenshot_add_competition.png" width="140"/> 

| Options                  | Players                   | Add Player Dialog             | Create Competition        | Competition Detail    | Saved Competition | Delete Saved Competition
|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|-----------------------------------|
|<img src="/Screenshots/Screenshot_options_page.png" width="220"/> | <img src="/Screenshots/Screenshot_players_page.png" width="220"/> | <img src="/Screenshots/Screenshot_add_player_page.png" width="260"/> | <img src="/Screenshots/Screenshot_create_competition_page.png" width="150"/> | <img src="/Screenshots/Screenshot_competition_detail_page.png" width="120"/> | <img src="/Screenshots/Screenshot_saved_competition_page.png" width="130"/>| <img src="/Screenshots/Screenshot_delete_dialog.png" width="120"/>

<!-- Open-Source Libraries -->
## Open-Source Libraries
* Minimum SDK level 26
* [Dependency Injection (Hilt) (2.51.1)](https://developer.android.com/training/dependency-injection/hilt-android) - Used for dependency injection, simplifying the management of application components.
* [Jetpack Compose (1.9.1)](https://developer.android.com/develop/ui/compose) - A modern toolkit for building native UI in Android.
* [Navigation (1.2.0)](https://developer.android.com/develop/ui/compose/navigation) - Handles in-app navigation in a type-safe manner.
* [Compose Lifecycle (2.8.4)](https://developer.android.com/develop/ui/compose/lifecycle) - Manages lifecycle-aware components in Jetpack Compose.
* [Coroutines](https://developer.android.com/kotlin/coroutines?hl=tr) - Provides a simple way to manage background threads, making asynchronous programming easier and more efficient.
* [Flow](https://developer.android.com/kotlin/flow) - A reactive streams API in Kotlin used for managing data streams asynchronously.
* [DataStore (1.1.1)](https://developer.android.com/topic/libraries/architecture/datastore) - Handles data storage and persistence, replacing SharedPreferences for more complex data structures.
* [MVVM](https://developer.android.com/topic/libraries/architecture/viewmodel#implement) - A design pattern used to separate concerns, making the application more modular, testable, and maintainable.
  * [Lifecycle (2.8.4)](https://developer.android.com/topic/libraries/architecture/lifecycle) - Manages Android lifecycle-aware components.
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores
      UI-related data that isn't destroyed on UI changes.
  * [UseCases](https://developer.android.com/topic/architecture/domain-layer) - Located domain
      layer that sits between the UI layer and the data layer.
  * [Repository](https://developer.android.com/topic/architecture/data-layer) - Located in the data
      layer that contains application data and business logic.
* [Retrofit (2.11.0)](https://square.github.io/retrofit/) A type-safe HTTP client for Android and Java
* [OkHttp (5.0.0-alpha.14)](https://square.github.io/okhttp/) An HTTP client that efficiently makes network requests
* [Gson (2.11.0)](https://mvnrepository.com/artifact/com.google.code.gson/gson) - A library for serializing and deserializing JSON data.
* [Firebase](https://firebase.google.com/) - A suite of tools used for backend services including authentication, Firestore database, storage, crash reporting, analytics, and performance monitoring.
    * [Firebase Authentication (23.0.0)](https://firebase.google.com/docs/auth) Firebase Authentication
      provides backend services, easy-to-use SDKs, and ready-made UI libraries to authenticate users
      to your app.
    * [Firebase Firestore (25.0.0)](https://firebase.google.com/docs/firestore) Cloud Firestore is a
      flexible, scalable database for mobile, web, and server development from Firebase and Google
      Cloud.
    * [Firebase Storage (21.0.0)](https://firebase.google.com/docs/storage?hl=en)
    * [Firebase Crashlytics (19.0.3)](https://firebase.google.com/docs/crashlytics) Firebase Crashlytics is a
      lightweight, real-time crash reporter that helps you track, prioritize, and fix stability
      issues that erode your app quality.
    * [Firebase Performance (21.0.1)](https://firebase.google.com/docs/perf-mon?hl=en)
    * [Firebase Analytics (22.0.2)](https://firebase.google.com/docs/analytics) Firebase Analytics is a free
      app measurement solution that provides insight on app usage and user engagement.
* [Location (21.3.0)](https://developers.google.com/android/guides/setup?hl=en) - Used for obtaining device location data.
* [SharedPreferences](https://developer.android.com/training/data-storage/shared-preferences) Store
  private primitive data in key-value pairs. Standard Android library with no specific version (comes with the Android SDK).
* [Lottie Animation (6.3.0)](https://lottiefiles.com/blog/working-with-lottie-animations/getting-started-with-lottie-animations-in-android-app) - Used to display animations in the app.
* [Coil (2.6.0)](https://coil-kt.github.io/coil/compose/) - An image loading library for Android backed by Kotlin Coroutines.
  
<!-- Architecture -->
## Architecture
This Android app uses the MVVM (Model-View-ViewModel) pattern and Clean Architecture principles, organized into four main modules for better scalability and maintainability.

MVVM

- Model: Manages data and business logic, separate from the UI.
- View: Displays the data and interacts with the user.
- ViewModel: Connects the View and Model, handling UI-related logic and state management.

Clean Architecture & Multi Module
- App Module: The core module that integrates all other modules and provides the main entry point of the application.
- Data Module: Handles data sources, such as APIs and databases, and provides data to the Domain Layer.
- Domain Module: Contains the core business logic and use cases, which are independent of external frameworks.
- Feature Module: Encapsulates the app's features, allowing for modular development and testing of individual functionalities.
  
![image](https://github.com/user-attachments/assets/eb3bf886-2376-4cb6-9234-ece71d036a68)

<!-- API and Services -->
## API and Services
The application integrates with the following APIs and services:
* OpenWeather API: Provides weather data and forecasts. More information can be found [here](https://openweathermap.org/api).
* Restful API: Utilized for data communication and integration to get weather data.
* Retrofit: A type-safe HTTP client for Android, used for interacting with RESTful APIs.
* OkHttp: An HTTP client used for making network requests and handling responses efficiently.
* Firebase: Used for various backend services, including:
  * Authentication: User sign-in and management.
  * Firestore Database: Real-time database for storing and syncing app data.
  * Storage: File storage and retrieval.
  * Crashlytics: Real-time crash reporting and diagnostics.
  * Performance Monitoring: Monitoring and optimizing app performance.
  * Analytics: Data collection and insights on app usage.
* Google Services: Used for location services, allowing the app to access and manage location data. [More detail](https://developers.google.com/android/reference/com/google/android/gms/common/package-summary)

<!-- CONTACT -->
## Contact

Bengisu Şahin - bengisusaahin@gmail.com

Talha Çalışır - talha5797@gmail.com

Yusuf Mücahit Solmaz - yusufmucahitsolmaz@gmail.com

<!-- LICENCE -->
## Licence
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.


