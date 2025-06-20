plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
    id("signing")
}

android {
    namespace = "com.sjdroid.screenshotblocker"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
        targetSdk = 35

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    
    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                
                groupId = "io.github.sjdroid"
                artifactId = "screenshot-blocker"
                version = "1.0.0"
                
                pom {
                    name.set("Screenshot Blocker")
                    description.set("Android library that blocks screenshots using FLAG_SECURE")
                    url.set("https://github.com/sjdroid/screenshot-blocker")
                    
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.html")
                        }
                    }
                    
                    developers {
                        developer {
                            id.set("sjdroid")
                            name.set("Santhosh J.")
                            email.set("santhoshj.dev@gmail.com")
                        }
                    }
                    
                    scm {
                        connection.set("scm:git:github.com/sjdroid/screenshot-blocker.git")
                        developerConnection.set("scm:git:ssh://github.com/sjdroid/screenshot-blocker.git")
                        url.set("https://github.com/sjdroid/screenshot-blocker")
                    }
                }
            }
        }
        
        repositories {
            maven {
                name = "sonatype"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername") as String? ?: ""
                    password = project.findProperty("ossrhPassword") as String? ?: ""
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}