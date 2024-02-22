package domain

import androidx.compose.ui.graphics.vector.Group

data class TaskWithChildren(val task: Task, val childrenTasks: List<TaskWithChildren>)

fun TaskWithChildren.flatten(): List<Task> {
    return listOf(task).plus(childrenTasks.flatMap { it.flatten() })
}

fun List<Task>.group(parentTask: Task? = null): List<TaskWithChildren> {
    val parents = filter { it.parentTask?.id == parentTask?.id }
    return parents.map { p ->
        TaskWithChildren(
            task = p,
            childrenTasks = group(p)
        )
    }
}

fun EntitiesList.NotGrouped<Task>.groupByParents(): EntitiesList.Grouped<Task> {
    return flatten()
        .group()
        .toGroupedList()
}

fun List<TaskWithChildren>.toGroupedList(): EntitiesList.Grouped<Task> {
    return EntitiesList.Grouped(map { taskWithChildren ->
        GroupedItem(
            groupID = GroupID(categoryName = taskWithChildren.task.name, key = taskWithChildren.task.id),
            parentItem = taskWithChildren.task,
            items = taskWithChildren.childrenTasks.toGroupedList()
        )
    }
    )
}