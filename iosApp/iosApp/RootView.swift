//
//  RootView.swift
//  iosApp
//
//  Created by Aleksandar Zekovic on 24.9.23..
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct RootView: View {
    @EnvironmentObject var store: ObservableShipsStore
    @SwiftUI.State var errorMessage: String?
    
    var body: some View {
        ZStack {
            NavigationView {
                Ships()
            }.zIndex(0)
            if let errorMessage = self.errorMessage {
                VStack {
                    Spacer()
                    Text(errorMessage)
                        .foregroundColor(.white)
                        .padding(10.0)
                        .background(Color.black)
                        .cornerRadius(3.0)
                }
                .padding(.bottom, 10)
                .zIndex(1)
                .transition(.asymmetric(insertion: .move(edge: .bottom), removal: .opacity) )
            }
        }
    }
}

struct RootView_Previews: PreviewProvider {
    static var previews: some View {
        RootView()
    }
}
