plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.daruratindonesianurgentresponse"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.daruratindonesianurgentresponse"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"AIzaSyB92bJvJzAEc6agNqLYHzoGVyD8hgiH1hQ\"")
        buildConfigField("String", "FOURSQUARE_KEY", "\"fsq3ub+ztlRqiPGKXrerbx1+L622UnuSeVeFryGsXDOQ47k=\"")
        buildConfigField("String", "PHONE_POLICE", "\"083829801796\"")
        buildConfigField("String", "PHONE_AMBULANCE", "\"083829801796\"")
        buildConfigField("String", "PHONE_FIREFIGHTER", "\"083829801796\"")
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
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
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.preference:preference:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Bottom Navigation
    implementation("com.ismaeldivita.chipnavigation:chip-navigation-bar:1.3.4")

    //Google Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")

    //Google Place
    implementation("com.google.android.libraries.places:places:3.3.0")

    //API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")

    //Simple Location
//    implementation("com.github.delight-im:Android-SimpleLocation:v1.1.0")
}