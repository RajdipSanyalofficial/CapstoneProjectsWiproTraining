//
//  AddFlashcardView.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//

import Foundation
import SwiftUI

struct AddFlashcardView: View {
    @Environment(\.managedObjectContext) private var viewContext
    @State private var word = ""
    @State private var translation = ""
    @State private var exampleSentence = ""
    @State private var showAlert = false

    var body: some View {
        NavigationView {
            ZStack {
                Color.blue.opacity(0.1)
                    .edgesIgnoringSafeArea(/*@START_MENU_TOKEN@*/.all/*@END_MENU_TOKEN@*/)
                
                VStack(spacing: 20) {
                    TextField("Enter Word", text: $word)
                        .padding()
                        .background(Color.yellow.opacity(0.2))
                        .cornerRadius(8)
                        .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
                        .disableAutocorrection(true)
                    
                    TextField("Enter Translation", text: $translation)
                        .padding()
                        .background(Color.green.opacity(0.2))
                        .cornerRadius(8)
                        .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
                        .disableAutocorrection(true)
                    
                    TextField("Example Sentence", text: $exampleSentence)
                        .padding()
                        .background(Color.orange.opacity(0.2))
                        .cornerRadius(8)
                        .autocapitalization(/*@START_MENU_TOKEN@*/.none/*@END_MENU_TOKEN@*/)
                        .disableAutocorrection(true)
                    
                    Button(action: saveFlashcard) {
                        Text("Save Flashcard")
                            .padding()
                            .frame(maxWidth: .infinity)
                            .background(Color.purple)
                            .foregroundColor(.white)
                            .cornerRadius(10)
                            .shadow(radius: 5)
                    }
                    .padding(.horizontal)
                    .alert(isPresented: $showAlert) {
                        Alert(title: Text("Success"), message: Text("Flashcard Saved Successfully!"), dismissButton: .default(Text("OK")))
                    }
                    
                    Spacer()
                }
                .padding()
                .navigationTitle("Add Flashcard")
                .background(LinearGradient(gradient: Gradient(colors: [Color.white, Color.pink.opacity(0.1)]), startPoint: .top, endPoint: .bottom))
            }
        }
    }

    private func saveFlashcard() {
        let newFlashcard = Flashcard(context: viewContext)
        newFlashcard.word = word
        newFlashcard.translation = translation
        newFlashcard.exampleSentence = exampleSentence
        newFlashcard.score = 0
        newFlashcard.nextReviewDate = Date()

        do {
            try viewContext.save()
            showAlert = true
            clearForm()
        } catch {
            print("Failed to Save Flashcard: \(error)")
        }
    }

    private func clearForm() {
        word = ""
        translation = ""
        exampleSentence = ""
    }
}
