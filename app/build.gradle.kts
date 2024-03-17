plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.kyungsuksong.doomsday"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kyungsuksong.doomsday"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "AsteroidApiKey", getNasaAsteroidApi())
        buildConfigField("String", "FireApiKey", getNasaFireApi())
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    secrets {
        // Optionally specify a different file name containing your secrets.
        // The plugin defaults to "local.properties"
        propertiesFileName = "secrets.properties"

        // A properties file containing default secret values. This file can be
        // checked in version control.
        defaultPropertiesFileName = "local.properties"

        // Configure which keys should be ignored by the plugin by providing regular expressions.
        // "sdk.dir" is ignored by default.
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // material3
    implementation(libs.androidx.material3)
    // dagger hilt
    implementation(libs.google.dagger.hilt.android)
    ksp(libs.google.dagger.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.work)
    // navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.compose)
    // room
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler) // To use Kotlin Symbol Processing (KSP)
    implementation(libs.androidx.room.ktx)
    // glide
    implementation(libs.github.bumtech.glide)
    // compose material
    implementation(libs.androidx.compose.material)
    // compose constraintlayout
    implementation(libs.androidx.constraintlayout.compose)
    // work manager
    implementation(libs.androidx.work.runtime.ktx)
    // moshi (JSON to Kotlin class converter)
    implementation(libs.squareup.moshi)
    implementation(libs.squareup.moshi.kotlin)
    // retrofit2
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.converter.moshi)
    implementation(libs.squareup.retrofit2.converter.scalars)
    implementation(libs.squareup.okhttp3.logging.interceptor)
    // google map
    implementation(libs.google.map.android.compose)
    implementation(libs.google.map.android.compose.utils)
    implementation(libs.google.android.gms.play.services.maps)
    implementation(libs.google.android.gms.play.services.gcm)
    implementation(libs.google.map.android.utils.ktx)
    // balloon
    implementation(libs.github.skydoves.balloon.compose)
}

fun getNasaAsteroidApi(): String {
    return project.findProperty("nasa_asteroid_api_key") as String
}

fun getNasaFireApi(): String {
    return project.findProperty("nasa_fire_api_key") as String
}