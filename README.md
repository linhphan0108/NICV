# Weather Forecast applies MVVM combined with [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
## Hello World!
**Hello** this is my simple **Weather Forecast** project as a applicant's challenging assignment for applying for the Android developer position at [Viet Nam Technology Center
National Australia Bank Limited
](https://www.nab.com.au/corporate/global-relationships/contact-us).

In this project I apply MVVM in combination with clean architecture and more...
## MVVM (Model–View–ViewModel)
MVVM is now one of the most loved patterns out there, as it has had plenty of time to mature. The web community adopted the pattern after the Microsoft team formalized it in 2005. It eventually made its way into every UI-based framework.
One key advantage of MVVM is that it offers the right amount of decoupling. Another good thing is that the learning curve is similar to the other patterns.

MVVM has three main components: Model, View, and ViewModel.
<div align="center">
    <img src="https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/docs/mvvm_diagram.png" width="500" alt="mvvm diagram" />
</div>

Unlike MVP and MVC, there’s a fourth component: The Binder. This is the mechanism that links the Views to the ViewModels.
The Binder is usually handled by the platform or a third party library, so the developer doesn’t have to write it. For Android, you have the DataBinding library at our disposal.

## Why MVVM with Clean Architecture?
MVVM separates our view (i.e. Activities and Fragments) from our business logic. MVVM is enough for small projects, but when our codebase becomes huge, our ViewModels start bloating. Separating responsibilities becomes hard.

MVVM with Clean Architecture is pretty good in such cases. It goes one step further in separating the responsibilities of our code base. It clearly abstracts the logic of the actions that can be performed in our app.

## Advantages of Using Clean Architecture

1. our code is even more easily testable than with plain MVVM. 
   
2. our code is further decoupled (the biggest advantage.)
   
3. The package structure is even easier to navigate.
   
4. The project is even easier to maintain.
   
5. our team can add new features even more quickly.

## Architecture
<div align="center">
<img src="https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/docs/project_structure.png" width="600" alt="project structure" />
</div>

**Presentation layer**: is the most familiar layer to us, it contains activities, fragments and view models etc. This layer has two responsibilities: get triggered then request data and show data that comes from domain layer. Additionally this is the layer that system gives resources to run our app.

MVVM structure is a good way to make presentation layer reliable. Using view model is the key point, it reduces lifecycle effects and gives an coroutine scope easily (viewModelScope).

In our design the view model executes interactors in desired coroutine scope and post it to a live data as DataHolder. Then this live data observed in activity/fragment and necessary UI operations performed according to it’s success/fail/loading state.

By that way activity/fragment only knows and observes view model, that separates framework from inner circle. 

**Domain layer**: is the center of the structure, it connects the data and presentation layer using interfaces and interactors. It is also completely independent and can be tested regardless of external components. Each domain layer has a unique use case, repository and business model.

**Data layer**: has all the repositories which the domain layer can use. Data layer handles all enterprise logic and produces data for our app. All complex logic must belong here. It must also be as independent as possible from platform. So it can be unit tested and modifiable. It mainly consists of 3 parts: Data Source, Repository Implementation and Mapper.

## Project structure
This project contains 5 main packages: presentation, domain, data, buildSrc and common
### 1. Presentation

- **base package** contains base classes such as BaseActivity, BaseViewModel, BaseAdapter,...

- **callback package** contains interface callbacks

- **common package** contains common classes such Contants,...

- **di package** contains dependency injection classes using Hilt

- **extensions package** contains classes defining kotlin extension functions used mostly by this layer.

- **feature package** contains all features (screens) in the application. It has activities, viewmodels, popups, that call to usecases down to Domain layer

- **mapper package** contains mapper classes that support to map objects between Domain and Presentation

- **model package** contains ui-related models

- **util package** contains helper classess

- **WeatherForecastApp class** and of course the custom application.

### 2. Domain

- **di package** contains dependency injection classes using Hilt

- **entity package package** contains all of entities

- **repository package** contains interfaces that will be implemented in the Data layer

- **useCase package** contains usecases that will be called by ViewModels in the Presentation layer

### 3. Data

- **di package** contains dependency injection classes that use Hilt

- **entity package** contains all of entities

- **extensions package** contains classes defining kotlin extension functions used mostly by this layer.

- **local package** contains database classes using Room

- **mapper package** contains mapper classes that mapping objects between Data layer and Domain layer

- **remote package** contains api define classes using Retrofit, Okhttp3

- **repository package** contains the implementations of interfaces in Domain layer

- **secureapi package** contains a JNI APIKeyLibrary object that is used to get sensetive data, secret keys, license keys,... from the .so file. (see [Storing secret keys and API keys](#2-storing-sensitve-data-runtime-data))

### 4. Common
This module conatins constants, util classes which are shared to other modules.

### 5. BuildSrc
This module contains nothing but a Dependencies.kt declaring all dependencies is using in entire project.

## Security
Security is the key requirement while building an application that’s dealing with APIs, Tokens received from a server, storing credentials in local DB, sending secure data onto the network, and many other scenarios. The developers must try their best to build a secure application to prevent it from being bypassed by a hacker.

And Weather Forecast is no exception, in spire of that this is just a really simple and tiny project. so in the project I build 3 senarios for securing secret keys and sensetive data.

### 1. Storing secret keys and API keys (hardcoded data)
Secret keys are stored in a [.so file](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/data/src/main/jniLibs/arm64-v8a/libsecureapi.so) using Android NDK (Native Development Kit) to build. ([see this project](https://github.com/linhphan0108/secureapi))

How do I retreive the keys? see [RemoteModule#provideSSLCertificate](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/data/src/main/java/com/linhphan/data/di/RemoteModule.kt)


### 2. Storing sensitve data (runtime data)
Runtime sensitive data is stored locally using [EncryptedSharedPreferences](https://developer.android.com/reference/androidx/security/crypto/EncryptedSharedPreferences) an implementation of SharedPreferences that encrypts keys and values. [see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/presentation/src/main/java/com/linhphan/presentation/util/SecuredSharePreference.kt)

### 3. Security with HTTPS and SSL
The Secure Sockets Layer (SSL)—now technically known as Transport Layer Security (TLS)—is a common building block for encrypted communications between clients and servers.

How did you integrate HTTPS and SSL? see [RemoteModule#provideOkHttpClient](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/data/src/main/java/com/linhphan/data/di/RemoteModule.kt)

## Other security technique
1. preventing rooted devices from using the app.

2. Enabling proguard

## Theme structure supports night mode
Dark theme is available in Android 10 (API level 29) and higher. It has many benefits:

1. Can reduce power usage by a significant amount (depending on the device’s screen technology).

2. Improves visibility for users with low vision and those who are sensitive to bright light.

3. Makes it easier for anyone to use a device in a low-light environment.

In Weather Forecast, themes are structure as in the diagram:
<div align="center">
<img src="https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/docs/theme-structure.gif" width="80%" alt="project structure" />
</div>

### Platform theme
[**See**](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/presentation/src/main/res/values/platfromThemes.xml) 
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- platform theme-->
    <style name="Platform.Theme.LP" parent="Platform.V21.Theme.LP" />
    <style name="Platform.V21.Theme.LP" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
    </style>
    <!-- end platform theme-->
</resources>

```
[**And here**](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/presentation/src/main/res/values-v23/platfromThemes.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- platform theme-->
    <style name="Platform.Theme.LP" parent="Platform.V23.Theme.LP" />
    <style name="Platform.V23.Theme.LP" parent="Platform.V21.Theme.LP">
        <!-- Attributes which are only available from API 23 -->
    </style>
</resources>

```

### Base theme 
[**See**](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/presentation/src/main/res/values/baseThemes.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- base theme-->
    <style name="Base.Theme.LP" parent="Platform.Theme.LP">
        <!--        global fonts used in whole app.-->
        <item name="android:textViewStyle">@style/MyTextAppearance</item>
        <item name="android:buttonStyle">@style/MyButtonStyle</item>
    </style>
    <!-- end base theme-->
</resources>

```
### App theme 
[**light**](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/presentation/src/main/res/values/themes.xml) 
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Theme.WeatherForecast" parent="Theme.LP.Light" />

    <style name="Theme.LP.Light" parent="Base.Theme.LP">
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryVariant">@color/colorPrimaryVariant</item>
        <item name="colorOnPrimary">@color/colorOnPrimary</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/colorSecondary</item>
        <item name="colorSecondaryVariant">@color/colorSecondaryVariant</item>
        <item name="colorOnSecondary">@color/colorOnSecondary</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
        <!-- Customize your theme here. -->

        <item name="colorSurface">@color/colorOnPrimary</item>
        <item name="android:colorBackground">@color/colorOnPrimary</item>
    </style>
</resources>

```
[**dark**](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/presentation/src/main/res/values-night/themes.xml)
```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Theme.WeatherForecast" parent="Theme.LP.Dark" />

    <style name="Theme.LP.Dark" parent="Base.Theme.LP">
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryVariant">@color/colorPrimaryVariant</item>
        <item name="colorOnPrimary">@color/colorOnPrimary</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/colorSecondary</item>
        <item name="colorSecondaryVariant">@color/colorSecondaryVariant</item>
        <item name="colorOnSecondary">@color/colorOnSecondary</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
        <!-- Customize your theme here. -->
        <item name="colorSurface">@color/colorPrimary</item>
        <item name="android:colorBackground">@color/colorPrimary</item>
    </style>
</resources>

```
## Accessibility supports

1. Talkback: Weather Forecast has contentDescription for supporting the screen reader.

2. Scaling Text: Weather Foreast allows user to change the text size for entire app.

## Testing
Local tests using junit4 and mockito
[see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/data/src/test/java/com/linhphan/data/repository/ForecastRepositoryTest.kt)
and [see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/domain/src/test/java/com/linhphan/domain/usecase/ForecastUseCaseTest.kt)

Instrumented tests using Robolectric with AndroidJunit4 + AndroidX Test APIs
[see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/tree/master/presentation/src/test/java/com/linhphan/presentation/feature)
and [see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/tree/master/presentation/src/androidTest/java/com/linhphan/presentation/feature/home/viewmodel)

Instrumented tests for Room Dao Object (database access object) [see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/data/src/androidTest/java/com/linhphan/data/loca/ForecastDaoTest.kt)

Ui tests using Expresso + data binding + Hilt
[see](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/tree/master/presentation/src/androidTest/java/com/linhphan/presentation)

## Test coverage for unit tests
The project contains additional configuration for Jacoco that enables coverage report for Unit Tests (initially Jacoco reports cover Android Instrumentation Tests).

The configuration file could find [here](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/jacoco.gradle)

To run tests coverage, from the project directory WeatherForecast execute:
```gradle
./gradlew testDebugUnitTestCoverage
// or 
./gradlew -stacktrace --info testDebugUnitTestCoverage
```
The coverage report can be found in WeatherForecast/build/coverage-report/index.html
<div align="center">
    <img src="https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/docs/test_coverage_linhphan.png" width=70% />
</div>

## How to Run
1. Clone the project down to your computer.
2. Open it in Android Studio (Android Studio 4.0 or newer are recommended)
3. To build debug version you just need to click on the play button on Android Studio.
4. To build release version you need to use your signed key and modify the file [keystore.properties](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/keystore.properties) respectively.
5. Or just play with this [**apk**](https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/docs/weather-forecast-LP-1.0.0-debug.apk)

## Demo
<div align="center">
    <img src="https://github.com/linhphan0108/NAB_Android_Technical_Challenge_Test/blob/master/docs/app_demo.gif" width=30% />
</div>

## Tasks list
1. Programming language: Kotlin is required, Java is optional ✔

2. Design app's architecture (suggest MVVM) ✔

3. Apply LiveData mechanism ✔

4. UI should be looks like in the attachment ✔

5. Write UnitTests ✔

6. Acceptance Tests

7. Exception handling ✔

8. Caching handling ✔

9. Secure Android app from:

   a. Decompile APK ✔

   b. Rooted device ✔

   c. Data transmission via network ✔

   d. Encryption for sensitive information ✔

10. Accessibility for Disability Supports:

    a. Talkback: Use a screen reader ✔

    b. Scaling Text: Display size and font size: To change the size of items on your screen, adjust the display size or font size ✔

11. Entity relationship diagram for the database and solution diagrams for the components, infrastructure design if any ✔

12. Readme file includes:

    a. Brief explanation for the software development principles, patterns & practices being applied ✔

    b. Brief explanation for the code folder structure and the key Java/Kotlin libraries and frameworks being used ✔

    c. All the required steps in order to get the application run on local computer ✔

    d. Checklist of items the candidate has done. ✔

## Thanks
Last but not least, I would really apprciated you for taking time to review my project so far, this is just such a simple project having done in my spare time in 1 week (of course I have tasks at my company as well) can't express all my abilities and experiences, so I hope I can have chance to talk and and discuss with you about the Android development.

Thanks ❤️ and happy coding! 💻

## References
[Clean Architecture + MVVM](https://medium.com/huawei-developers/android-clean-app-base-library-clean-architecture-mvvm-part-i-e8614978877f)

[Better Android Apps Using MVVM with Clean Architecture](https://www.toptal.com/android/android-apps-mvvm-with-clean-architecture)

[The Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

[Refactoring Android Themes with Style](https://medium.com/monzo-bank/refactoring-android-themes-with-style-restructuring-themes-15230569e50)
