package ui.screens.user_details

import LocalCurrentUser
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import domain.User
import kotlinx.coroutines.delay
import tests.testUser1
import ui.FABController
import ui.FABState
import ui.UiSettings
import ui.screens.master_detail.IDetailsComponent
import utils.log


@Composable
fun UserDetailsUi(component: IDetailsComponent<User>) {

    val user by remember(component) { component.item }.collectAsState(null)

    var tempUser by remember(user) { mutableStateOf(user) }

    user?.let {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            RenderUserCard(
                it,
                it.id == LocalCurrentUser.current?.id,
                onUserEdited = { editedUser ->
                    tempUser = editedUser
//                component.updateUser(editedUser)
                }
            )
        }
    }

}

@Composable
fun RenderUserCard(
    user: User,
    isEditable: Boolean,
    onUserEdited: ((User) -> Unit)? = null
) {

    var tempUser by remember(user) { mutableStateOf(user) }

    Card {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            //1. User Icon
            Icon(
                modifier = Modifier.size(128.dp),
                painter = painterResource("vector/person_black_24dp.svg"),
                contentDescription = "user icon"
            )
            //2. Name/surname fields
            RenderUserDetailsTextField(
                value = tempUser.name,
                onValueChange = { tempUser = tempUser.copy(name = it) },
                isEditable = isEditable,
                label = "имя"
            )
            RenderUserDetailsTextField(
                value = tempUser.middleName,
                onValueChange = { tempUser = tempUser.copy(middleName = it) },
                isEditable = isEditable,
                label = "отчество"

            )
            RenderUserDetailsTextField(
                value = tempUser.surname,
                onValueChange = { tempUser = tempUser.copy(surname = it) },
                isEditable = isEditable,
                label = "фамилия"
            )
            //3. Room
            RenderUserDetailsTextField(
                value = tempUser.roomNumber,
                onValueChange = { tempUser = tempUser.copy(roomNumber = it) },
                isEditable = isEditable,
                label = "комната"

            )
            //4. Phone number
            RenderUserDetailsTextField(
                value = tempUser.phoneNumber,
                onValueChange = { tempUser = tempUser.copy(phoneNumber = it) },
                isEditable = isEditable,
                label = "телефон"
            )
            //5. Email
            RenderUserDetailsTextField(
                value = tempUser.email,
                onValueChange = { tempUser = tempUser.copy(email = it) },
                isEditable = isEditable,
                label = "email"
            )
//
//            if (isEditable && tempUser != user) {
//                Row {
//                    OutlinedButton(content = {
//                        Text("отмена")
//                    }, onClick = {
//                        tempUser = user
//                    })
//
//                    Button(content = {
//                        Text("сохранить")
//                    }, onClick = {
//                        onUserEdited?.let { it(tempUser) }
//                    })
//                }
//            }

            LaunchedEffect(tempUser) {
//                if (tempUser == user) {
//                    return@LaunchedEffect
//                }
                delay(UiSettings.Debounce.debounceTime)
                onUserEdited?.let { it(tempUser) }
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RenderUserDetailsTextField(
    value: String,
    onValueChange: (newValue: String) -> Unit,
    isEditable: Boolean,
    label: String
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