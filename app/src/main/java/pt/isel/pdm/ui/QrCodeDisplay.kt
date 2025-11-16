import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lightspark.composeqr.QrCodeView
import java.util.UUID

@Composable
fun MyQrCode() {
    QrCodeView(
        data = UUID.randomUUID().toString() ,
        modifier = Modifier.size(300.dp)
    )
}

@Preview
@Composable
fun QrCode(){
    MyQrCode()
}