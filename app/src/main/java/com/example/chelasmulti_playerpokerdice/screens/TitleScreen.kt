package com.example.chelasmulti_playerpokerdice.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.chelasmulti_playerpokerdice.Greeting

@Composable
fun TitleScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column {
            Text(
                text = "Welcome to Poker Dice!",
                modifier = Modifier.padding(innerPadding)
            )
            Button(value= "Start ") { }

        }
    }
}
