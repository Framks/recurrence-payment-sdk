plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0") // ver opções para resolver vunerabilidade

    testImplementation(libs.junit.jupiter)
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.4")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.15.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    api(libs.commons.math3)
    implementation(libs.guava)
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
