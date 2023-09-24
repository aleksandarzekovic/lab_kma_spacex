//
//  ObservableShipsStore.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//

import shared

class ObservableShipsStore: ObservableObject {
    @Published public var state: ShipsState =  ShipsState(progress: false, ships: [])
    @Published public var sideEffect: ShipsSideEffect?

    let store: ShipsStore

    var stateWatcher : Closeable?
    var sideEffectWatcher : Closeable?

    init(store: ShipsStore) {
        self.store = store
        stateWatcher = self.store.watchState().watch { [weak self] state in
            self?.state = state
        }
        sideEffectWatcher = self.store.watchSideEffect().watch { [weak self] state in
            self?.sideEffect = state
        }
    }

    public func dispatch(_ action: ShipsAction) {
        store.dispatch(action: action)
    }

    deinit {
        stateWatcher?.close()
        sideEffectWatcher?.close()
    }
}

public typealias DispatchFunction = (ShipsAction) -> ()

