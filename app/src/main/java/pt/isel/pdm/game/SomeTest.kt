import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun BasicConstraintLayoutExample() {

    // O ConstraintLayout é o "container" principal
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        // 1. Criar "referências" (Refs) para cada Composable que queres posicionar.
        // É como dar-lhes um "ID" para o ConstraintLayout.
        val (text1, button1, text2) = createRefs()

        // 2. Definir os Composables e "ancorá-los" usando o modificador .constrainAs()

        Text(
            text = "Olá, ConstraintLayout!",
            modifier = Modifier.constrainAs(text1) {
                // Ancorar o topo do texto ao topo do "parent" (o ConstraintLayout)
                top.linkTo(parent.top, margin = 16.dp)

                // Centrar horizontalmente
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Button(
            onClick = { /* Ação do botão */ },
            modifier = Modifier.constrainAs(button1) {
                // Ancorar o topo do botão à parte de baixo do text1
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                // Centrar horizontalmente
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Text("Clica-me")
        }

        Text(
            text = "Rodapé esquerdo",
            modifier = Modifier.constrainAs(text2) {
                // Ancorar a parte de baixo ao "parent.bottom"
                bottom.linkTo(parent.bottom, margin = 16.dp)

                // Ancorar o início (esquerda) ao "parent.start"
                start.linkTo(parent.start, margin = 16.dp)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBasicConstraintLayout() {
    BasicConstraintLayoutExample()
}