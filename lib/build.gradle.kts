plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.37.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.jackson.databind)
    implementation(libs.jackson.datatype.jsr310)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(16)
    }
}

sourceSets {
    test {
        java {
            // ToDo reativar quando os testes forem replanejados (mappers do Mercado Pago ainda não existem
            // e os testes do PagBank cobrem o contrato antigo).
            exclude("com/recurrence/mercadopago/**")
            exclude("com/recurrence/pagbank/**")
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

// Publicação no Maven Central: coordenadas, POM e assinatura são lidos do
// gradle.properties da raiz. O plugin gera também os jars de sources e javadoc.
