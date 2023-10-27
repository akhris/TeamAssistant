package domain.valueobjects

import java.time.LocalDateTime

sealed class State {
    sealed class User {
        object IDLE : User()
        data class Vacation(val from: LocalDateTime, val to: LocalDateTime) : User()
        data class Sick(val from: LocalDateTime, val to: LocalDateTime) : User()
        data class BusinessTrip(val from: LocalDateTime, val to: LocalDateTime) : User()
        data class Custom(val state: String = "", val from: LocalDateTime, val to: LocalDateTime)
    }

    sealed class Project {
        object ACTIVE : Project()
        object ARCHIVED : Project()
    }

    sealed class Task {
        object ACTIVE : Task()
        data class InProgress(val progress: Float = 0.0f) : Task()
        object COMPLETED : Task()
        data class Failed(val reason: String = "") : Task()
    }
}