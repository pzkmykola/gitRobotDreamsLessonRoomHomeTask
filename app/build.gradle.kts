plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("com.google.devtools.ksp") - for room
    id ("com.google.gms.google-services")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
//    id("org.jetbrains.kotlin.kapt") - for hilt
//    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.roomongit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.roomongit"
        minSdk = 24
        targetSdk = 34
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

    viewBinding { enable = true }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    //room database
//    implementation("androidx.room:room-runtime:2.6.1")
//    annotationProcessor("androidx.room:room-compiler:2.6.1")
//    ksp("androidx.room:room-compiler:2.6.1")

    //google maps
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.maps.android:android-maps-utils:3.5.3")
    
    //firebase-database
    //implementation("com.google.firebase:firebase-database:20.3.1")
    implementation (platform("com.google.firebase:firebase-bom:32.7.3"))
    //implementation ("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-analytics-ktx")
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    //coroutines
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //hilt
//    implementation ("com.google.dagger:hilt-android:2.50")
//    kapt ("com.google.dagger:hilt-compiler:2.50")

    //glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //picasso
    implementation ("com.squareup.picasso:picasso:2.8")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

//kapt{
//    correctErrorTypes = true
//}
