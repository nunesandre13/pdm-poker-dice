package pt.isel.pdm // ou utils

import android.net.Uri

object DeepLinks {
    private const val SCHEME = "pdm://"
    const val LOBBY_BASE = "${SCHEME}lobby"
    const val MATCH_BASE = "${SCHEME}match"

    fun createMatchUri(matchId: Int): Uri {
        return Uri.parse("$MATCH_BASE/$matchId")
    }

    fun createLobbyUri(): Uri {
        return Uri.parse(LOBBY_BASE)
    }
}