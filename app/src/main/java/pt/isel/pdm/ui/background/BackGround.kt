package pt.isel.pdm.ui.background

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.pdm.ui.column.ColumnScaffold
import pt.isel.pdm.ui.topBar.TopBar
import pt.isel.pdm.ui.topBar.TopBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultBackGround(
    vararg content: @Composable () -> Unit,
    topBarConfig : TopBarConfig,
    modifier: Modifier
){
    ColumnScaffold(topBar = { TopBar(topBarConfig) }, content= content,)
}

@Preview(showBackground = true)
@Composable
fun DefaultBackGroundPreview() {
    DefaultBackGround(
        topBarConfig = TopBarConfig.Simple("Exemplo"),
        modifier = Modifier
    )
}
