//
//  ShipDetailsView.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import URLImage

struct ShipDetailsView: View {
    
    var ship: ShipsDetail
    
    var body: some View {
        VStack(alignment: .center, spacing: 20) {
            if let imageUrl = ship.image, let url = URL(string: imageUrl) {
                URLImage(url) { image in
                    image
                        .resizable()
                        .scaledToFill()
                }
                .frame(height: 200)
                .clipped()
            } else {
                Image(systemName: "photo.fill")
                    .resizable()
                    .scaledToFill()
                    .frame(height: 200)
                    .clipped()
            }
        
            
            if let name = ship.name {
                Text("Name: \(name)")
            }
            
            
            if let homePort = ship.home_port {
                Text("Home Port: \(homePort)")
            }
            
            
            if let type = ship.type {
                Text("Type: \(type)")
            }
        }
        
    }
}
