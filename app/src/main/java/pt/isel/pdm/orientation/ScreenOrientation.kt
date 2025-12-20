package pt.isel.pdm.orientation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ScreenOrientation(val orientation: OrientationType)