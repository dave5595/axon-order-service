import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.1"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.10"
	kotlin("plugin.spring") version "1.5.10"
}

group = "com.finevotech"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenLocal()
	mavenCentral()
}

configurations {
	all{
		exclude("org.springframework.boot", "spring-boot-starter-logging")
	}

	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

dependencies {
	//finevo
	implementation("com.yoda.mo.api:yoda-market-order-api-provider:1.3.0")
	//codec
	implementation("commons-codec:commons-codec:1.15")
	//jackson
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	//reactor
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	//needed as Reactor Netty 1.0.x depends on Reactor Core 3.4.x
	//https://github.com/reactor/reactor-netty/issues/1388
	implementation("io.projectreactor:reactor-core:3.4.6")
	//jwt
	implementation("io.jsonwebtoken:jjwt-api:0.10.6")
	implementation("io.jsonwebtoken:jjwt-impl:0.10.6")
	implementation("io.jsonwebtoken:jjwt-jackson:0.10.6")
	//jetbrains
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.5.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	//spring starters
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.axonframework:axon-spring-boot-starter:4.5.2")/*{
		exclude(module =  "axon-server-connector")
	}*/
	//security commented out for test
//	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-log4j2")

	runtimeOnly("io.r2dbc:r2dbc-postgresql")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
