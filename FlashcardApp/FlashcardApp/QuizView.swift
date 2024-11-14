//
//  QuizView.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//


import SwiftUI
import Foundation

struct QuizView: View {
    @FetchRequest(entity: Flashcard.entity(), sortDescriptors: [])
    var flashcards: FetchedResults<Flashcard>
    
    @State private var playerName: String = ""
    @State private var showQuiz = false // Track if the quiz has started
    @State private var currentIndex = 0
    @State private var userAnswer = ""
    @State private var showResult = false
    @State private var correct = false
    @State private var feedbackMessage = ""
    @State private var quizCompleted = false // Track whether the quiz is completed
    @State private var score = 0 // Track the score (number of correct answers)
    @State private var totalQuestions = 0 // Track the total number of questions
    @State private var correctAnswers: [String] = [] // Store correct answers for all questions
    @State private var buttonColor: Color = .purple
    @State private var showNameError = false // New state to track if the name is empty

    var body: some View {
        ZStack {
            // Set the background color to white
            Color.blue.opacity(0.1)
                .edgesIgnoringSafeArea(.all)
            
            VStack {
                
                if flashcards .isEmpty {
                    Text("No Flashcards Available For Quiz!")
                        .font(/*@START_MENU_TOKEN@*/.title/*@END_MENU_TOKEN@*/)
                        .foregroundColor(.black)
                        .padding()
                } else if !showQuiz {
                    // Initial screen for entering player name
                    VStack {
                        Text("Enter Your Name To Start The Quiz")
                            .font(.title)
                            .foregroundColor(.black)
                            .padding()
                        
                        TextField("Your Name", text: $playerName)
                            .padding()
                            .background(Color.gray.opacity(0.2))
                            .cornerRadius(10)
                            .disableAutocorrection(true)
                            .autocapitalization(.words)
                            .foregroundColor(.black)
                        
                        if showNameError {
                            Text("Please Enter Your Name Before Starting The Quiz.")
                                .foregroundColor(.red)
                                .font(.footnote)
                                .padding(.top, 5)
                        }
                        
                        Button(action: {
                            if playerName.isEmpty {
                                // Show error if the name is empty
                                showNameError = true
                            } else {
                                showNameError = false // Reset error if name is provided
                                startQuiz() // Reset and start the quiz
                            }
                        }) {
                            Text("Start Quiz")
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(buttonColor)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                        }
                        .padding(.horizontal)
                    }
                    .padding()
                } else if flashcards.isEmpty {
                    Text("No Flashcards Available For Quiz.")
                        .font(.title)
                        .foregroundColor(.black)
                        .padding()
                } else if quizCompleted {
                    // Quiz completion screen with user's name and score
                    VStack {
                        Text("Thank You, \(playerName)!")
                            .font(.largeTitle)
                            .fontWeight(.bold)
                            .foregroundColor(.green)
                            .padding()

                        // Display the final score
                        Text("Your Score: \(score) out of \(totalQuestions)")
                            .font(.title2)
                            .fontWeight(.medium)
                            .foregroundColor(.black)
                            .padding()

                        // Display the correct answers for all questions
                        Text("Correct Answers:")
                            .font(.headline)
                            .foregroundColor(.orange)
                            .padding(.top)

                        ScrollView {
                            VStack(spacing: 10) {
                                ForEach(0..<flashcards.count, id: \.self) { index in
                                    HStack {
                                        Text(flashcards[index].word ?? "")
                                            .bold()
                                            .foregroundColor(.black)
                                        Text("->")
                                            .foregroundColor(.black)
                                        Text(flashcards[index].translation ?? "")
                                            .foregroundColor(.black.opacity(0.7))
                                    }
                                    .padding()
                                    .background(Color.black.opacity(0.1))
                                    .cornerRadius(10)
                                }
                            }
                        }
                        .padding()
                        
                        Spacer()

                        // Retry button to start the quiz again
                        Button(action: retryQuiz) {
                            Text("Reattempt Quiz")
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(buttonColor)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                        }
                        .padding(.horizontal)
                        
                        Spacer()
                    }
                } else {
                    // Quiz question and answer input
                    VStack(spacing: 30) {
                        // Question card with shadow and corner radius
                        VStack(spacing: 20) {
                            Text("Translate:")
                                .font(.headline)
                                .foregroundColor(.black)
                            
                            Text(flashcards[currentIndex].word ?? "")
                                .font(.largeTitle)
                                .fontWeight(.bold)
                                .foregroundColor(.purple)
                                .padding()
                                .background(Color.black.opacity(0.1))
                                .cornerRadius(15)
                                .shadow(radius: 10)
                        }
                        .padding()
                        .background(Color.white)
                        .cornerRadius(20)
                        .shadow(radius: 10)

                        // Answer input with a light background
                        TextField("Your Answer", text: $userAnswer)
                            .padding()
                            .background(Color.gray.opacity(0.1))
                            .cornerRadius(10)
                            .disableAutocorrection(true)
                            .autocapitalization(.none)
                            .foregroundColor(.black)
                            .shadow(radius: 5)

                        // Submit button with shadow and vibrant colors
                        Button(action: checkAnswer) {
                            Text("Submit")
                                .padding()
                                .frame(maxWidth: .infinity)
                                .background(buttonColor)
                                .foregroundColor(.white)
                                .cornerRadius(10)
                                .shadow(radius: 5)
                        }
                        .padding(.horizontal)

                        // Feedback result with smooth animation and scaling effect
                        if showResult {
                            Text(feedbackMessage)
                                .font(.headline)
                                .foregroundColor(correct ? .green : .red)
                                .padding()
                                .scaleEffect(showResult ? 1 : 0.5)
                                .opacity(showResult ? 1 : 0)
                                .animation(.easeInOut(duration: 0.5), value: showResult)
                        }

                        // Progress indicator with a custom color
                        ProgressView(value: Double(currentIndex + 1), total: Double(totalQuestions))
                            .accentColor(.orange)
                            .padding()
                    }
                    .padding()
                }
            }
        }
        .navigationTitle("Quiz Questions")
        .navigationBarTitleDisplayMode(.inline)
        .onAppear {
            totalQuestions = flashcards.count // Set total number of questions
        }
    }

    private func checkAnswer() {
        guard !userAnswer.isEmpty else {
            feedbackMessage = "Please Enter An Answer!"
            correct = false
            showResult = true
            return
        }
        
        let flashcard = flashcards[currentIndex]
        if userAnswer.lowercased() == flashcard.translation?.lowercased() {
            correct = true
            feedbackMessage = "Correct!"
            score += 1 // Increment score for correct answer
            correctAnswers.append(flashcard.translation ?? "") // Store the correct answer
            updateFlashcardScore(flashcard, correct: true)
        } else {
            correct = false
            feedbackMessage = "Incorrect! Try again later."
            correctAnswers.append(flashcard.translation ?? "") // Store the correct answer for feedback
        }
        
        showResult = true
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
            nextQuestion()
        }
    }

    private func nextQuestion() {
        showResult = false
        
        if currentIndex + 1 < flashcards.count {
            currentIndex += 1
            userAnswer = "" // Clear the answer box
        } else {
            quizCompleted = true // End the quiz if last question is reached
        }
    }

    private func updateFlashcardScore(_ flashcard: Flashcard, correct: Bool) {
        flashcard.score += correct ? 1 : -1
        flashcard.nextReviewDate = Calendar.current.date(byAdding: .day, value: correct ? 3 : 1, to: Date())
        try? flashcard.managedObjectContext?.save()
    }

    // Function to start/restart the quiz
    private func startQuiz() {
        currentIndex = 0
        score = 0
        correctAnswers.removeAll()
        userAnswer = ""
        showResult = false
        quizCompleted = false
        showQuiz = true
    }
    
    // Retry quiz by resetting the necessary states
    private func retryQuiz() {
        startQuiz()
    }
}
