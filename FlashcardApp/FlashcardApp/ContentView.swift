//
//  ContentView.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//



import SwiftUI
import Foundation
import CoreData

struct ContentView: View {
    @Environment(\.managedObjectContext) private var viewContext
    @FetchRequest(entity: Flashcard.entity(), sortDescriptors: [])
    var flashcards: FetchedResults<Flashcard>
    
    var body: some View {
        TabView {
            FlashcardListView()
                .tabItem {
                    Label("Flashcards", systemImage: "list.dash")
                }
            
            AddFlashcardView()
                .tabItem {
                    Label("Add Flashcard", systemImage: "plus.circle")
                }
            
            QuizView()
                .tabItem {
                    Label("Quiz", systemImage: "questionmark.circle")
                }
            
            
            if !flashcards.isEmpty {
                ProgressChartView(progressData: generateProgressData())
                    .tabItem {
                        Label("Progress", systemImage: "chart.line.uptrend.xyaxis")
                    }
            } else {
                VStack {
                    Text("No Graph Available!")
                        .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                        .foregroundColor(.black)
                        .padding()
                }
                    .tabItem {
                        Label("Progress", systemImage: "chart.line.uptrend.xyaxis")
                    }
                    
            }
            
        }
        .accentColor(.purple)
    }
    
    // Function to generate progress data based on correct answers
    func generateProgressData() -> [(date: Date, score: Int)] {
        let today = Date()
        // Example logic: Assuming a score property exists in Flashcard
        var correctAnswersData: [(date: Date, score: Int)] = []
        
        // Logic to fetch correct answer count, replace with actual implementation
        let correctAnswersCount = flashcards.map { Int($0.score) }.reduce(0, +)
        
        
        for i in 0..<5 {
            let date = Calendar.current.date(byAdding: .day, value: -i, to: today)!
            correctAnswersData.append((date: date, score: correctAnswersCount))
        }
         
        
        return correctAnswersData
    }
}



