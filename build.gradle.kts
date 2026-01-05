plugins {
	java
	jacoco
	id("org.springframework.boot") version "4.0.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.hyuse98"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

jacoco {
	toolVersion = "0.8.14"
	reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.1.1"
extra["springModulithVersion"] = "2.0.5"
val datasourceMicrometerVersion by extra("2.2.1")

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-amqp")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")
	implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

	//Observability
//	implementation("net.ttddyy.observation:datasource-micrometer-spring-boot")
//	implementation("org.springframework.boot:spring-boot-starter-zipkin")
//	implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
//	implementation("io.micrometer:micrometer-tracing-bridge-brave")

//	runtimeOnly("io.micrometer:micrometer-registry-prometheus")
//	runtimeOnly("io.micrometer:micrometer-registry-otlp")

	//Database
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	runtimeOnly("org.postgresql:postgresql")

	//Modulith
	implementation("org.springframework.modulith:spring-modulith-events-api")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	implementation("org.springframework.modulith:spring-modulith-starter-jpa")
	runtimeOnly("org.springframework.modulith:spring-modulith-actuator")
	runtimeOnly("org.springframework.modulith:spring-modulith-events-amqp")
	runtimeOnly("org.springframework.modulith:spring-modulith-observability")

	//JWT
	implementation("io.jsonwebtoken:jjwt-api:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
	implementation("org.springframework.boot:spring-boot-starter-security")

	//Miscellanea
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	//Dev
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	//Test
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
	testImplementation("org.springframework.boot:spring-boot-starter-amqp-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.testcontainers:testcontainers-rabbitmq")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
//		mavenBom("net.ttddyy.observation:datasource-micrometer-bom:$datasourceMicrometerVersion")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// https://docs.gradle.org/current/userguide/jacoco_plugin.html

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
}

tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = "0.5".toBigDecimal()
			}
		}

		rule {
			isEnabled = false
			element = "CLASS"
			includes = listOf("org.gradle.*")

			limit {
				counter = "LINE"
				value = "TOTALCOUNT"
				maximum = "0.3".toBigDecimal()
			}
		}
	}
}