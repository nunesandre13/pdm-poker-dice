package pt.isel.pdm.ui.topBar

sealed class TopBarConfig(val title: String) {
    interface Profile {
        val onProfileClick: () -> Unit
    }
    open class Simple(title: String) : TopBarConfig(title)
    open class WithBack(title: String, open val onBack: () -> Unit) : TopBarConfig(title)
    open class WithNext(title: String, open val onNext: () -> Unit) : TopBarConfig(title)
    open class WithBackAndNext(
        title: String,
        open val onBack: () -> Unit,
        open val onNext: () -> Unit
    ) : TopBarConfig(title)

    class WithProfile(
        title: String,
        override val onProfileClick: () -> Unit
    ) : Profile, Simple(title)

    class WithBackProfile(
        title: String,
        onBack: () -> Unit,
        override val onProfileClick: () -> Unit
    ) : Profile, WithBack(title, onBack)

    class WithNextAndProfile(
        title: String,
        onNext: () -> Unit,
        override val onProfileClick: () -> Unit
    ) : Profile, WithNext(title, onNext)

    class WithBackNextAndProfile(
        title: String,
        onBack: () -> Unit,
        onNext: () -> Unit,
        override val onProfileClick: () -> Unit
    ) : Profile, WithBackAndNext(title, onBack, onNext)
}
