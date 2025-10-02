package pt.isel.pdm.navigation

enum class Screens(val route: String) {
    ROOT("root"),
    HOME_SCREEN("start_screen"),
    ABOUT("about"),
    PROFILE("profile"),
    START_MATCH("start-match"),
    CREATE_LOBBY("create-lobby"),

    AWAIING_GAME("awaing-game"),
    TITLE_SCREEN("title_screen")
}