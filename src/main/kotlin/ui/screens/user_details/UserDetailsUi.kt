package ui.screens.user_details

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.User
import tests.testUser1

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserDetailsUi(user: User) {

    Column(modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = String.format("%s %s %s", user.name, user.middleName, user.surname).trim(),
            style = MaterialTheme.typography.h5
        )

        ListItem(text = {
            Text(user.email)
        }, overlineText = {
            Text("e-mail")
        })

        ListItem(text = {
            Text(user.phoneNumber)
        }, overlineText = {
            Text("телефон")
        })

        ListItem(text = {
            Text(user.roomNumber)
        }, overlineText = {
            Text("кабинет")
        })
    }

}

@Preview
@Composable
fun userDetailsPreview() {
    UserDetailsUi(testUser1)
}