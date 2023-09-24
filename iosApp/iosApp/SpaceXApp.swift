//
//  SpaceXApp.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//


import SwiftUI
import shared

@main
struct SpaceXApp: App {
    
    let store: ShipsStore
    let observableShipsStore: ObservableShipsStore
    
    init() {
        store = ShipsStore.Companion().create()
        observableShipsStore = ObservableShipsStore(store: store)
    }
    
    var body: some Scene {
        WindowGroup {
            RootView().environmentObject(observableShipsStore)
        }
    }
}


