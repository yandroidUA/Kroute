import java.io.File

val rootDirectory = File(rootProject.projectDir, "samples/app")

include(":samples:app:android")
findProject(":samples:app:android")?.name = "samples-app-android"
findProject(":samples:app:android")?.projectDir = File(rootDirectory, "android")

include(":samples:app:common")
findProject(":samples:app:common")?.name = "samples-app-common"
findProject(":samples:app:common")?.projectDir = File(rootDirectory, "common")

include(":samples:app:web")
findProject(":samples:app:web")?.name = "samples-app-web"
findProject(":samples:app:web")?.projectDir = File(rootDirectory, "web")

include(":samples:app:jvm-desktop")
findProject(":samples:app:jvm-desktop")?.name = "samples-app-jvm-desktop"
findProject(":samples:app:jvm-desktop")?.projectDir = File(rootDirectory, "jvm-desktop")
