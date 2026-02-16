plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")

    testImplementation(libs.junit.jupiter)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    api(libs.commons.math3)
    implementation(libs.guava)
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}


tasks.named<Test>("test") {
    useJUnitPlatform()
}
