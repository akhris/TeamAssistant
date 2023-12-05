package ui.screens.master_detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import domain.IEntity
import ui.screens.BaseComponent

open class BaseMasterDetailsComponent<T : IEntity>(
    componentContext: ComponentContext,
    private val createMasterComponent: (componentContext: ComponentContext, onItemSelected: (String) -> Unit) -> IMasterComponent<T>,
    private val createDetailsComponent: (componentContext: ComponentContext, itemID: String) -> IDetailsComponent<T>,
) : IMasterDetailComponent<T>, BaseComponent(componentContext) {

    private val masterNavigation = StackNavigation<MasterConfig>()
    private val detailsNavigation = StackNavigation<DetailsConfig>()


    override val masterStack: Value<ChildStack<*, IMasterDetailComponent.Master<T>>> = childStack(
        source = masterNavigation,
        initialConfiguration = MasterConfig.ItemsList,
//            handleBackButton = true,
        childFactory = ::createChild,
        key = "master stack"
    )

    override val detailsStack: Value<ChildStack<*, IMasterDetailComponent.Details<T>>> = childStack(
        source = detailsNavigation,
        initialConfiguration = DetailsConfig.None,
        childFactory = ::createChild,
        key = "details stack"
    )

    private fun createChild(
        config: MasterConfig,
        componentContext: ComponentContext,
    ): IMasterDetailComponent.Master<T> {
        return when (config) {
            MasterConfig.ItemsList -> IMasterDetailComponent.Master.ItemsList(
                createMasterComponent(componentContext) {
                    detailsNavigation.replaceCurrent(DetailsConfig.ItemDetails(it))
                }
            )
        }
    }

    private fun createChild(
        config: DetailsConfig,
        componentContext: ComponentContext,
    ): IMasterDetailComponent.Details<T> {
        return when (config) {
            is DetailsConfig.ItemDetails -> IMasterDetailComponent.Details.ItemDetails(
                createDetailsComponent(
                    componentContext,
                    config.itemID
                )
            )

            DetailsConfig.None -> IMasterDetailComponent.Details.None()
        }
    }

    @Parcelize
    private sealed class MasterConfig : Parcelable {
        object ItemsList : MasterConfig()
    }


    @Parcelize
    private sealed class DetailsConfig : Parcelable {
        object None : DetailsConfig()
        class ItemDetails(val itemID: String) : DetailsConfig()
    }

}