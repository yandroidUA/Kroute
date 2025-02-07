# Kroute

Kroute - it's a tiny Jetpack Compose Multiplatform navigation library. Its main purpose is to navigate you through the 
composables and pass an arguments (if any).

You can install a Kroute library by adding following into your Gradle `builds.gradle.kts`:
```
    implementation("io.github.yandroidua.kroute:koin-viewmodel:latest-version")
    implementation("io.github.yandroidua.kroute:lifecycle-koin:latest-version")
    implementation("io.github.yandroidua.kroute:lifecycle-viewmodel:latest-version")
    implementation("io.github.yandroidua.kroute:navigation-arguments:latest-version")
    implementation("io.github.yandroidua.kroute:navigation-router:latest-version")
    implementation("io.github.yandroidua.kroute:navigation-transition:latest-version")
    implementation("io.github.yandroidua.kroute:navigation-ui:latest-version")
    implementation("io.github.yandroidua.kroute:ui-koin:latest-version")
    implementation("io.github.yandroidua.kroute:ui-koin-viewmodel:latest-version")
    implementation("io.github.yandroidua.kroute:ui-navigation-koin-viewmodel:latest-version")
    implementation("io.github.yandroidua.kroute:ui-viewmodel:latest-version")
    implementation("io.github.yandroidua.kroute:viewmodel:latest-version")
```

**Please refer to a [sample app](https://github.com/yandroidUA/Kroute/tree/main/samples/app) for examples**.

|                                                      Android                                                      |                                                     iOS                                                     | Wasm  |
|:-----------------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------:|:-----:|
| <image src="https://github.com/yandroidUA/Kroute/blob/main/samples/assets/kroute-android-demo.gif" height="400"/> | <img src="https://github.com/yandroidUA/Kroute/blob/main/samples/assets/kroute-ios-demo.gif" height="400"/> |<img src="https://github.com/yandroidUA/Kroute/blob/main/samples/assets/kroute-wasm-browser-demo.gif" height="400"/>

## Extensions

Although Kroute can be used as a pure navigation library, it has several useful extensions that you can consider to 
utilize, the library designed this way that you can chose not to use any of these extensions.

1. Viewmodel - a tiny abstraction that defines a lifecycle-aware components that can be used to host a UI state and interact with the logic
2. Koin - an extension that integrates with a popular KMP dependency injection library - Koin, that gives you a control over dependencies that has been used in the route

These 2 can be combined in various ways and there are modules that brings a lot of utilities functionality you may want to use in case you have these 2 in the project.