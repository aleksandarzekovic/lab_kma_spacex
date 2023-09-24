//
//  ShipItemRow.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import shared
import URLImage

struct ShipItemRow: View {
    var shipDetail: ShipsDetail
    
    var body: some View {
        VStack {
            HStack {
                if let imageUrl = shipDetail.image, let url = URL(string: imageUrl) {
                    URLImage(url) { image in
                        image
                            .resizable()
                            .scaledToFill()
                    }
                    .frame(width: 60, height: 60)
                    .clipShape(Rectangle())
                } else {
                    Image(systemName: "photo.fill")
                        .resizable()
                        .scaledToFill()
                        .frame(width: 60, height: 60)
                        .clipShape(Rectangle())
                }
                
                VStack(alignment: .leading, spacing: 4) {
                    if let name = shipDetail.name {
                        Text(name)
                            .font(.headline)
                            .fontWeight(.bold)
                    }
                    if let homePort = shipDetail.home_port {
                        Text(homePort)
                            .font(.subheadline)
                            .opacity(0.6)
                    }
                }
                Spacer()
            }
            .frame(maxWidth: .infinity)
            .padding(10)
        }
    }
}

struct ShipItem_Previews: PreviewProvider {
    static var previews: some View {
        ShipItemRow(shipDetail: ShipsDetail(
            id: "1",
            name: "ShipName",
            image: nil,
            home_port: "Port",
            type: nil))
        }
    }

