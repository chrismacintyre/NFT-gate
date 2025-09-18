plugins {
	java
	id("org.springframework.boot") version "3.5.5"
	id("io.spring.dependency-management") version "1.1.7"
    id("org.web3j.solidity") version "0.6.0"
    id("org.web3j") version "4.14.0"
}

group = "com.chrismacintyre"
version = "0.0.1-SNAPSHOT"
description = "NFT-gating demo"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}
web3j {
    generatedPackageName = "org.web3j.generated.contracts"
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.named("processResources") {
    dependsOn("generateContractWrappers")
}

