[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Apache 2 License](https://img.shields.io/github/license/InsertKoinIO/koin)](https://github.com/InsertKoinIO/koin/blob/main/LICENSE.txt)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.yandroidua.kroute/navigation-ui)

# Kroute

Kroute is a tiny Jetpack Compose Multiplatform navigation library. Its main purpose is to navigate through composables and pass arguments (if any).

**Supported platfroms**:
- Android
- iOS
- Wasm Browser

*If you'd like to use this library for other targets, please let me know by openning an issue*.

You can install the Kroute library by adding the following to your `build.gradle.kts`:
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
| <image src="https://github.com/yandroidUA/Kroute/blob/main/samples/assets/kroute-demo-android.gif" height="400"/> | <img src="https://github.com/yandroidUA/Kroute/blob/main/samples/assets/kroute-ios-demo.gif" height="400"/> |<img src="https://github.com/yandroidUA/Kroute/blob/main/samples/assets/kroute-wasm-browser-demo.gif" height="400"/>

## Extensions

Although Kroute can be used as a pure navigation library, it also provides several useful extensions that you may find helpful. The library is designed in a way that allows you to choose whether or not to use these extensions.

- ViewModel – A lightweight abstraction that defines lifecycle-aware components, which can be used to host UI state and interact with business logic.
- Koin – An extension that integrates with the popular KMP dependency injection library, Koin. It gives you control over the dependencies used within a route.

These two extensions can be combined in various ways. Additionally, there are modules that provide a range of utility functions, which you may find useful if you include both extensions in your project.
