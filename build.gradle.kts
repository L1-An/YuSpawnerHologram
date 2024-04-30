plugins {
    `java-library`
    `maven-publish`
    id("io.izzel.taboolib") version "1.56"
    id("org.jetbrains.kotlin.jvm") version "1.5.10"
}

taboolib {
    description {
        name(rootProject.name)
        desc("Visualize your spawner respawn time!")
        links {
            name("homepage").url("https://github.com/L1-An")
        }
        contributors {
            name("L1An")
        }
        dependencies {
            name("MythicMobs").with("bukkit")
            name("PlaceholderAPI").with("bukkit").optional(true)
            name("HolographicDisplays").with("bukkit").optional(true)
            name("DecentHolograms").with("bukkit").optional(true)
            name("Adyeshach").with("bukkit").optional(true)
        }
    }
    install("common")
    install("common-5")
    install("module-chat")
    install("module-configuration")
    install("module-lang")
    install("module-metrics")
    install("platform-bukkit")
    install("expansion-command-helper")
    classifier = null
    version = "6.0.12-69"
}

repositories {
    mavenCentral()
    maven(url = "https://mvn.lumine.io/repository/maven-public/")
    maven { url = uri("https://repo.tabooproject.org/repository/releases/") } // TabooLib
    maven { url = uri("https://repo.codemc.io/repository/maven-public/") } // HolographicDisplays
    maven { url = uri("https://jitpack.io") } // DecentHolograms
}

dependencies {
    compileOnly("ink.ptms:nms-all:1.0.0")
    compileOnly("ink.ptms.core:v11902:11902-minimize:mapped")
    compileOnly("ink.ptms.core:v11902:11902-minimize:universal")
    implementation("org.yaml:snakeyaml:2.0")
    compileOnly(kotlin("stdlib"))
    compileOnly(fileTree("libs"))
    compileOnly("io.lumine:Mythic-Dist:5.3.5")
    compileOnly("ink.ptms.adyeshach:all:2.0.0-snapshot-1")
    compileOnly("me.filoghost.holographicdisplays:holographicdisplays-api:3.0.0")
    compileOnly("com.github.decentsoftware-eu:decentholograms:2.8.4")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.tabooproject.org/repository/releases")
            credentials {
                username = project.findProperty("taboolibUsername").toString()
                password = project.findProperty("taboolibPassword").toString()
            }
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            groupId = project.group.toString()
        }
    }
}