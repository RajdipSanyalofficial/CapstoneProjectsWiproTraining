//
//  FlashcardListView.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//


 import Foundation
 import SwiftUI
 import CoreData
 
 struct FlashcardListView: View {
 @Environment(\.managedObjectContext) private var viewContext
 @FetchRequest(entity: Flashcard.entity(), sortDescriptors: [])
 var flashcards: FetchedResults<Flashcard>
 
 var body: some View {
 NavigationView {
 ZStack {
 Color.blue.opacity(0.1)
 .edgesIgnoringSafeArea(/*@START_MENU_TOKEN@*/.all/*@END_MENU_TOKEN@*/)
 
 if flashcards.isEmpty {
 
 Text("No Flashcards Available!")
 .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
 .foregroundColor(.black)
 .padding()
 }
 
 else {
 List {
 ForEach(flashcards, id: \.self) { flashcard in
 VStack(alignment: .leading, spacing: 10) {
 Text(flashcard.word ?? "")
 .font(.title2)
 .fontWeight(.bold)
 .foregroundColor(.blue)
 Text(flashcard.translation ?? "")
 .font(.subheadline)
 .foregroundColor(.purple)
 Text(flashcard.exampleSentence ?? "")
 .font(.caption)
 .foregroundColor(.secondary)
 }
 .padding(.vertical)
 }
 .onDelete(perform: deleteFlashcard)
 }
 .listStyle(InsetGroupedListStyle())
 }
 }
 .navigationTitle("Flashcard App")
 .toolbar {
 EditButton() // Enables swipe-to-delete
 .foregroundColor(.green)
 }
 }
 }
 
 private func deleteFlashcard(at offsets: IndexSet) {
 offsets.map { flashcards[$0] }.forEach(viewContext.delete)
 
 do {
 try viewContext.save()
 } catch {
 print("Error Deleting Flashcard: \(error)")
 }
 }
 }
 
 
