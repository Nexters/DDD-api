
plugins {
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"

	val kotlinVersion = "1.9.25"

	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion
	kotlin("plugin.allopen") version kotlinVersion
	kotlin("kapt") version kotlinVersion
}

group = "com.ddd"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-aop")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	runtimeOnly("com.mysql:mysql-connector-j")
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.3")

	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.getByName("jar") {
	enabled = false
}

tasks.register("copyConfig", Copy::class) {
	copy {
		from("./ddd-config/backend/api")
		include("*.yml", "*.xml", "*.json")
		into("src/main/resources")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
