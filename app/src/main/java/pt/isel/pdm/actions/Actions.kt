package pt.isel.pdm.actions
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri


fun onAction(actionsIntent: ActionsIntent){
    when(actionsIntent){
        is ActionsIntent.Email -> sendEmailTo( actionsIntent.context,actionsIntent.emails)
        is ActionsIntent.Browser -> openOnBrowser(actionsIntent.context,actionsIntent.url)
    }
}

fun sendEmailTo(
    context: Context,
    emails: List<String>
) {
    val intent = Intent(Intent.ACTION_SENDTO,"mailto:".toUri() ).apply {
        putExtra(Intent.EXTRA_EMAIL, emails.toTypedArray()) }
    context.startActivity(intent)
}


fun openOnBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    context.startActivity(intent)
}

