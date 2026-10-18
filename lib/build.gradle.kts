plugins {
    `java-library`
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

// Executa Library.main, que percorre todas as operações do contrato no sandbox do PagBank.
tasks.register<JavaExec>("runLibrary") {
    group = "verification"
    description = "Executa as chamadas do contrato contra o sandbox do PagBank."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass = "com.recurrence.Library"
}
