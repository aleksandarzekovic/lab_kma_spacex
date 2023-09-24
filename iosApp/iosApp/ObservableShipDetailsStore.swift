//
//  ObservableShipDetailsStore.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared

class ObservableShipDetailsStore: ObservableObject {
    @Published public var state: ShipDetailsState =  ShipDetailsState(progress: false, ship: nil)
    @Published public var sideEffect: ShipDetailsSideEffect?

    let store: ShipDetailsStore

    var stateWatcher : Closeable?
    var sideEffectWatcher : Closeable?

    init(store: ShipDetailsStore) {
        self.store = store
        stateWatcher = self.store.watchState().watch { [weak self] state in
            self?.state = state
        }
        sideEffectWatcher = self.store.watchSideEffect().watch { [weak self] state in
            self?.sideEffect = state
        }
    }

    public func dispatch(_ action: ShipDetailsAction) {
        store.dispatch(action: action)
    }

    deinit {
        stateWatcher?.close()
        sideEffectWatcher?.close()
    }
}
