//
//  iosApp.swift
//  ios
//
//  Created by Andrii Yanechko on 2025-02-07.
//

import SwiftUI
import ComposeApp

@main
struct iosApp: App {

    init() {
        DiHelperKt.doInitKoin(platformConfiguration: KoinApplicationPlatformConfiguration())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
