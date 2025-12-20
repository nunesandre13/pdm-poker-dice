package pt.isel.pdm.navigation

import androidx.compose.runtime.Composable
import pt.isel.pdm.orientation.EnableScreenOrientation
import pt.isel.pdm.orientation.OrientationType
import pt.isel.pdm.orientation.SetLandScapeOrientationOnly
import pt.isel.pdm.orientation.SetPortraitOrientationOnly

@Composable
fun OrientationType.HandleScreenOrientation() {
    when(this){
        OrientationType.PORTRAIT -> SetPortraitOrientationOnly()
        OrientationType.LANDSCAPE -> SetLandScapeOrientationOnly()
        OrientationType.FREE -> EnableScreenOrientation()
    }
}