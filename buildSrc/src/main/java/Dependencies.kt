import Versions.androidx_test_version
import Versions.espresso_version
import Versions.jacoco_version
import Versions.kotlin_version
import Versions.lifecycle_version
import Versions.mockito_version
import Versions.retrofit_version
import Versions.room_version
import Versions.scalable_unit_version

object Configs {
    const val applicationId = "com.linhphan.weatherforecast"
    const val compileSdkVersion = 31
    const val minSdkVersion = 21
    const val targetSdkVersion = 31
    const val versionCode = 2
    const val versionName = "1.0.1"
}

object Versions{
    const val kotlin_version = "1.5.31"
    const val hilt_version = "2.40.1"
    const val retrofit_version = "2.9.0"
    const val lifecycle_version = "2.4.0"
    const val room_version = "2.4.2"
    const val scalable_unit_version = "1.0.6"
    const val mockito_version = "4.0.0"
    const val androidx_test_version = "1.4.0"
    const val espresso_version = "3.4.0"
    const val jacoco_version = "0.8.7"
}

object TopLevelDependencies{
    const val gradleBuildTool = "com.android.tools.build:gradle:7.1.2"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt_version}"
    const val jacoco = "org.jacoco:org.jacoco.core:$jacoco_version"
}

object Androidx{
    const val coreKtx = "androidx.core:core-ktx:1.7.0"
    const val appCompat = "androidx.appcompat:appcompat:1.3.1"
    const val material = "com.google.android.material:material:1.4.0"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val androidCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
    const val viewModelLifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycle_version}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${lifecycle_version}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.6"
}

object TestDependencies{
    const val junit4 = "junit:junit:4.13.2"
    const val junitExt = "androidx.test.ext:junit:1.1.3"
    const val mockitoCore = "org.mockito:mockito-core:${mockito_version}"
    const val mockitoInline = "org.mockito:mockito-inline:${mockito_version}"
    const val kotlinCoroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2"
    const val kotlinJunitTest = "org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version"
    const val truth = "com.google.truth:truth:1.1.2"
    const val robolectric = "org.robolectric:robolectric:4.6"

    const val androidxCoreTesting = "androidx.arch.core:core-testing:2.1.0"
    const val androidxCoreKtx = "androidx.test:core-ktx:$androidx_test_version"
    const val androidxExtJunit = "androidx.test.ext:junit:1.1.3"
    const val androidxTestRunner = "androidx.test:runner:$androidx_test_version"
    const val androidxTestRule = "androidx.test:rules:$androidx_test_version"

    const val hiltAndroidTest = "com.google.dagger:hilt-android-testing:${Versions.hilt_version}"

    const val mockitoDexMark = "com.linkedin.dexmaker:dexmaker-mockito-inline:2.28.1"
    const val espresso = "androidx.test.espresso:espresso-core:$espresso_version"
    const val espressoIdlingResource = "androidx.test.espresso:espresso-idling-resource:$espresso_version"
    const val fragmentTest = "androidx.fragment:fragment-testing:$androidx_test_version"
    const val hamcrest = "org.hamcrest:hamcrest-all:1.3"
    const val hamcrestCore = "org.hamcrest:hamcrest-core:1.3"
}

object Others{
    //network dependencies
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofit_version"
    const val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:$retrofit_version"
    const val okhttpInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.2"

    // hilt dependency injection
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt_version}"
    const val hiltAndroidCompiler =  "com.google.dagger:hilt-android-compiler:${Versions.hilt_version}"

    //local storage
    const val roomCommon = "androidx.room:room-common:$room_version"
    const val roomCompiler = "androidx.room:room-compiler:$room_version"
    const val roomKtx = "androidx.room:room-ktx:$room_version"
    //Safely manage keys and encrypt files and sharedpreferences
    const val cryptoSharePreference = "androidx.security:security-crypto:1.1.0-alpha03"

    // provides a new size unit (scalable dp & sp).
    const val sdp = "com.intuit.sdp:sdp-android:$scalable_unit_version"
    const val ssp = "com.intuit.ssp:ssp-android:$scalable_unit_version"
}