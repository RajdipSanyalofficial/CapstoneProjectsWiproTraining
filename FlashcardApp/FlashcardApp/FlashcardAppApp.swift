//
//  FlashcardAppApp.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//

import SwiftUI

@main
struct FlashcardAppApp: App {
    let persistenceController = PersistenceController.shared

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environment(\.managedObjectContext, persistenceController.container.viewContext)
        }
    }
}
