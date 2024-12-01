plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.marill_many_events"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.marill_many_events"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"



        // Ensure BuildConfig is generated
        buildConfigField("String", "APPLICATION_ID", "\"com.example.marill_many_events\"")


        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true // Enable BuildConfig generation
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

    //implementation(files("/Users/qiantongguo/Library/Android/sdk/platforms/android-34/android.jar"))
    // Firebase BOM and Services
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    // Navigation Components
    implementation("androidx.navigation:navigation-fragment:2.3.5")
    implementation("androidx.navigation:navigation-ui:2.3.5")
    // Material Components
    implementation("com.google.android.material:material")
    // ZXing (QR Code Scanning)
    implementation ("com.google.zxing:core:3.4.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    // CameraX (Barcode Scanning)
    implementation ("com.google.mlkit:barcode-scanning:17.0.0")
    implementation ("androidx.camera:camera-core:1.2.0")
    implementation ("androidx.camera:camera-camera2:1.2.0")
    implementation ("androidx.camera:camera-lifecycle:1.2.0")

    // Geolocation OSMDroid for Maps
    implementation("org.osmdroid:osmdroid-android:6.1.14")


    // Material Design Components
    implementation("com.google.android.material:material")

    implementation(libs.fragment.testing)
    implementation(libs.androidx.espresso.intents)
    implementation(libs.androidx.camera.view)

    // JUnit dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.androidx.junit) // For unit testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.1")
    // AndroidX Test Dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5") // Ensure you have this
    androidTestImplementation("androidx.test:runner:1.5.2") // Ensure this is also present
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:core:1.5.0") // Make sure core is included
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")

    // Mockito for Mocking in Tests
    testImplementation("org.mockito:mockito-core:5.4.0")
    androidTestImplementation("org.mockito:mockito-android:5.4.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)

    // Glide (Image Loading)
    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.0") // For OkHttp integration if needed
    implementation("com.github.bumptech.glide:annotations:4.15.0")

    testImplementation(libs.core) // This is for core testing

    // Recyclerview for Viewing Events
    implementation("androidx.cardview:cardview:1.0.0") // card based layout
    implementation("androidx.recyclerview:recyclerview:1.2.1") // Recyclerview for viewing events



}