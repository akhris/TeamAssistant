package ui.screens.teams

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.subscribe
import domain.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import org.kodein.di.DI


class TeamsListComponent(
    private val di: DI,
    componentContext: ComponentContext
): ITeamsListComponent, ComponentContext by componentContext {

    private val scope =
        CoroutineScope(Dispatchers.Default + SupervisorJob())



    override val teams: Value<List<Team>>
        get() = TODO("Not yet implemented")

    override fun createNewTeam() {
        TODO("Not yet implemented")
    }

    override fun deleteTeam() {
        TODO("Not yet implemented")
    }


    private suspend fun invalidateTeams(){

    }

    init {
        componentContext
            .lifecycle
            .subscribe(onDestroy = {
                scope.coroutineContext.cancelChildren()
            })

    }



}