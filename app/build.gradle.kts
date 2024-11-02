plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.marill_many_events"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marill_many_events"
        minSdk = 34
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
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    implementation("androidx.navigation:navigation-fragment:2.3.5")
    implementation("androidx.navigation:navigation-ui:2.3.5")
    implementation("com.google.android.material:material")

    implementation(libs.fragment.testing)

    // JUnit dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.androidx.junit) // For unit testing

    // AndroidX Test dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Ensure you have this
    androidTestImplementation("androidx.test:runner:1.5.2") // Ensure this is also present
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.2")
    androidTestImplementation("androidx.test:core:1.5.0") // Make sure core is included

    testImplementation("org.mockito:mockito-core:5.0.0")
    androidTestImplementation("org.mockito:mockito-android:5.0.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)

    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.0") // For OkHttp integration if needed
    implementation("com.github.bumptech.glide:annotations:4.15.0")

    testImplementation(libs.core) // This is for core testing
}