//
//  Ships.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//


import SwiftUI
import shared

struct Ships: ConnectedView {
    
    struct Props {
        let ships: [ShipsDetail]
    }
    
    func map(state: ShipsState, dispatch: @escaping DispatchFunction) -> Props {
        return Props(ships: state.ships)
    }
    
    @SwiftUI.State var showsAlert: Bool = false
    
    func body(props: Props) -> some View {
        List {
            ForEach(props.ships, id: \.self) { ship in
                NavigationLink(
                    destination: ShipDetailsView(ship: ship)
                        
                ){
                    ShipItemRow(shipDetail: ship)
                        .buttonStyle(PlainButtonStyle())
                }
            }
            
        }
        .navigationTitle("Ships")
        .navigationBarTitleDisplayMode(.inline)
        .listStyle(PlainListStyle())
    }
}

extension ShipsDetail: Identifiable { }
