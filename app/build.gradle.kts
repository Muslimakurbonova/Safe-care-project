
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.programmsoft.sevgisherlari"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.programmsoft.safecare"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "1.0.0"
        multiDexEnabled = true
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.appcompat.v161)
    implementation(libs.viewbindingpropertydelegate)
    implementation(libs.viewbindingpropertydelegate.noreflection)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.lottie)
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    //noinspection GradleDependency,GradleDependency,NotInterpolated
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.rxandroid)
    //noinspection GradleDependency
    implementation(libs.androidx.room.rxjava3)
    //noinspection GradleDependency
    implementation(libs.review.ktx)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.gson)
    // Kotlin + coroutines
    implementation(libs.androidx.work.runtime.ktx)
    // optional - RxJava2 support
    implementation(libs.androidx.work.rxjava2)
    implementation(libs.rxandroid)
    //noinspection GradleDependency
    implementation(libs.androidx.room.rxjava3)
    //noinspection GradleDependency
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.recyclerview.v7)
    implementation(libs.androidx.paging.common.ktx)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.okhttp)
    //OkHttp Interceptor
    implementation(libs.logging.interceptor)
    implementation(libs.likebutton)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment.ktx)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
}