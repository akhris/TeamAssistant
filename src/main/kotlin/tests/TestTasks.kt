package tests

import domain.Task
import domain.valueobjects.State

val testTask1 = Task(
    name = "clean the room",
    description = "make the room cleaned using vacuum cleaner and other special tools",
    state = State.Task.ACTIVE
)

val testTask2 = Task(
    name = "review: Week 34",
    description = "write the review about last week's work",
    state = State.Task.InProgress(.3f)
)