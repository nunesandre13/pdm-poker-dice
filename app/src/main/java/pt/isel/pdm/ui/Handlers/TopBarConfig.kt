package pt.isel.pdm.ui.Handlers

sealed class TopBarConfig(val title : String) {
    class Simple(title: String) : TopBarConfig(title)
    class WithBack(title: String, val onBack: () -> Unit) : TopBarConfig(title)
    class WithNext(title: String, val onNext: () -> Unit) : TopBarConfig(title)
    class WithBackAndNext(
        title: String,
        val onBack: () -> Unit,
        val onNext: () -> Unit
    ) : TopBarConfig(title)
}
