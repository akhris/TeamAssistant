package ui.screens.user_details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import domain.User
import kotlinx.coroutines.delay
import tests.testUser1
import ui.UiSettings
import ui.fields.EditableTextField
import ui.screens.BaseDetailsScreen
import ui.screens.master_detail.IDetailsComponent


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserDetailsUi(component: IDetailsComponent<User>) {

    val user by remember(component) { component.item }.collectAsState(null)

//    var tempUser by remember(user) { mutableStateOf(user) }

    user?.let {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            RenderUserCard(
                it,
                it.id == component.currentUser.id,
                onUserEdited = { editedUser ->
//                    tempUser = editedUser
                    component.updateItem(editedUser)
                }
            )
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RenderUserCard(
    user: User,
    isEditable: Boolean,
    onUserEdited: ((User) -> Unit)? = null,
) {

    var tempUser by remember { mutableStateOf(user) }

    BaseDetailsScreen(
        title = {
            Column {
                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tempUser.name,
                    isEditable = isEditable,
                    onValueChange = {
                        tempUser = tempUser.copy(name = it)
                    },
                    label = "имя",
                    withClearIcon = true
                )

                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tempUser.middleName,
                    isEditable = isEditable,
                    onValueChange = {
                        tempUser = tempUser.copy(middleName = it)
                    },
                    label = "отчество",
                    withClearIcon = true
                )

                EditableTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = tempUser.surname,
                    isEditable = isEditable,
                    onValueChange = {
                        tempUser = tempUser.copy(surname = it)
                    },
                    label = "фамилия",
                    withClearIcon = true
                )
            }
        },
        description = {
            EditableTextField(
                modifier = Modifier.fillMaxWidth(),
                value = tempUser.roomNumber,
                isEditable = isEditable,
                onValueChange = {
                    tempUser = tempUser.copy(roomNumber = it)
                },
                label = "комната",
                withClearIcon = true
            )

            EditableTextField(
                modifier = Modifier.fillMaxWidth(),
                value = tempUser.phoneNumber,
                isEditable = isEditable,
                onValueChange = {
                    tempUser = tempUser.copy(phoneNumber = it)
                },
                label = "телефон",
                withClearIcon = true
            )

            EditableTextField(
                modifier = Modifier.fillMaxWidth(),
                value = tempUser.email,
                isEditable = isEditable,
                onValueChange = {
                    tempUser = tempUser.copy(email = it)
                },
                label = "e-mail",
                withClearIcon = true
            )
        }
    )


    LaunchedEffect(tempUser) {
        if (tempUser == user) {
            return@LaunchedEffect
        }
        delay(UiSettings.Debounce.debounceTime)
        onUserEdited?.let { it(tempUser) }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderUserDetailsTextField(
    value: String,
    onValueChange: (newValue: String) -> Unit,
    isEditable: Boolean,
    label: String,
) {
    if (isEditable)
        TextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true
        )
    else
        ListItem(
            overlineText = {
                Text(label)
            },
            text = { Text(value) }
        )
}

@Preview
@Composable
fun userDetailsPreview() {
    RenderUserCard(testUser1, isEditable = false)
}