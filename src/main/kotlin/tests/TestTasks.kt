package tests

import domain.SubCheck
import domain.Task
import domain.valueobjects.Attachment
import domain.valueobjects.State

val testTask1 = Task(
    name = "clean the room",
    description = "make the room cleaned using vacuum cleaner and other special tools",
    state = State.Task.ACTIVE,
    creator = testUser1,
    users = listOf(testUser1, testUser2),
    attachments = listOf(
        Attachment.Folder(name = "altium projects", path = """C:\altium"""),
        Attachment.File(name = "project review", path = """C:\review.docx"""),
        Attachment.InternetLink(name = "vendor's site", link = """http://www.ti.com"""),
        Attachment.Email(name = "Task manager", email = """boss@mail.ru""")
    ),
    subchecks = listOf(
        SubCheck(name = "Открыть папку с проектом", description = "Адрес указан во вложении"),
        SubCheck(name = "Открыть файл проекта", description = "В соответствующей программе"),
        SubCheck(name = "Отправить результат", description = "На e-mail task manager")

    )
)

val testTask2 = Task(
    name = "review: Week 34",
    description = "write the review about last week's work",
    state = State.Task.InProgress(.3f)
)