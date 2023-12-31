plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.myapplication2"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.myapplication2"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    implementation("com.google.protobuf:protobuf-kotlin:3.25.0")
//    implementation("com.google.protobuf:protobuf-java:3.25.0")
//    implementation("androidx.datastore:datastore:1.0.0")
//
//    // optional - RxJava2 support
//    implementation("androidx.datastore:datastore-rxjava2:1.0.0")
//
//    // optional - RxJava3 support
//    implementation("androidx.datastore:datastore-rxjava3:1.0.0")
    //implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")
  //  implementation("com.squareup.moshi:moshi:1.12.0")
  //  implementation("com.squareup.moshi:moshi-kotlin:1.12.0")
    // data store
//    implementation("androidx.datastore:datastore:1.0.0")
//
//    // coroutines
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
//
//    //lifecycle
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
//
//    // lottie
//    implementation("com.airbnb.android:lottie:3.4.0")
    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0-alpha04")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    implementation("com.google.code.gson:gson:2.8.6") // Use the latest version available

}






