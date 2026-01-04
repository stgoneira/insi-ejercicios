plugins {
	java
	id("org.springframework.boot") version "4.0.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "cl.losclaveles.web"
version = "0.0.1-SNAPSHOT"
description = "Minisitio para el registro de nuevos socios de Fundación Los Claveles"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-thymeleaf-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // JMS
    implementation("jakarta.jms:jakarta.jms-api:3.1.0")
    runtimeOnly("org.apache.activemq:artemis-jakarta-client:2.41.0")
    // GSON
    implementation("com.google.code.gson:gson:2.13.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
