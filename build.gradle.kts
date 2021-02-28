import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    java
    kotlin("jvm") version "1.4.31"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "me.sagiri"
version = "1.0-SNAPSHOT"


var sourceCompatibility = "1.8"
var targetCompatibility = "1.8"



repositories {
    mavenCentral()
    maven{
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
//    testCompile("junit", "junit", "4.12")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

}