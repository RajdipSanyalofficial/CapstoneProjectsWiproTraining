//
//  ProgressChartView.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//

import SwiftUI
import Foundation
import Charts

struct ProgressChartView: View {
   var progressData: [(date: Date, score: Int)]
   
   var body: some View {
       ZStack {
           Color.blue.opacity(0.1)
               .edgesIgnoringSafeArea(.all)
           VStack {
               Text("Learning Progress Of User")
                   .font(.title2)
                   .padding(.bottom, 20)
               
               if progressData.isEmpty {
                   Text("No Data Available")
                       .foregroundColor(.black)
                       
               } else {
                   // Create the chart with correct answer data
                   Chart(progressData, id: \.date) { data in
                       LineMark(
                           x: .value("Date", data.date),
                           y: .value("Score", data.score)
                       )
                       .interpolationMethod(.catmullRom)
                       .symbol(.circle)
                       .foregroundStyle(Color.purple)
                   }
                   .frame(height: 300)
                   .padding()
                   .background(Color(.systemGray6))
                   .overlay(
                       RoundedRectangle(cornerRadius: 10)
                           .stroke(Color.black, lineWidth: 2)
                   )
                   .padding()
               }
           }
       }
   }
}

