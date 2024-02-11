package ui.screens.user_create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.fields.EditableTextField

@Composable
fun UserCreateUi(component: IUserCreateComponent) {
    UserCreateUiContent(onUserCreate = { name, surname ->
        component.createUser(name = name, surname = surname)
    })
}

@Composable
private fun UserCreateUiContent(onUserCreate: (name: String, surname: String) -> Unit) {
    var tempName by remember { mutableStateOf("") }
    var tempSurname by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        EditableTextField(
            value = tempName,
            onValueChange = { tempName = it },
            label = "имя",
            isError = tempName.isEmpty()
        )
        EditableTextField(
            value = tempSurname,
            onValueChange = { tempSurname = it },
            label = "фамилия"
        )
        Button(onClick = {
            onUserCreate(tempName, tempSurname)
        }, content = {
            Text("создать")
        }, enabled = tempName.isNotEmpty())
    }
}