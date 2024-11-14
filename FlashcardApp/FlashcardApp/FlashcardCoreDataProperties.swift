//
//  FlashcardCoreDataProperties.swift
//  FlashcardApp
//
//  Created by RPS on 19/10/24.
//

import Foundation
import CoreData

extension FlashcardCoreDataClass {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<FlashcardCoreDataClass> {
        return NSFetchRequest<FlashcardCoreDataClass>(entityName: "Flashcard")
    }

    @NSManaged public var word: String?
    @NSManaged public var translation: String?
    @NSManaged public var exampleSentence: String?
    @NSManaged public var nextReviewDate: Date?
    @NSManaged public var score: Int16

}

extension FlashcardCoreDataClass : Identifiable {

}
