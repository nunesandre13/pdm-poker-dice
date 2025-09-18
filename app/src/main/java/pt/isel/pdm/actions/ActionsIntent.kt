package pt.isel.pdm.actions

import android.content.Context


sealed class ActionsIntent(val context: Context) {
    class Browser(val url: String, context: Context) : ActionsIntent(context)
    class Email(val emails: List<String>, context: Context) : ActionsIntent(context)
}