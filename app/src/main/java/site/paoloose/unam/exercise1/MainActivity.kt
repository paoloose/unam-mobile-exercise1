package site.paoloose.unam.exercise1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import site.paoloose.unam.exercise1.ui.Exercise1App
import site.paoloose.unam.exercise1.ui.theme.Exercise1Theme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Exercise1Theme {
                Exercise1App()
            }
        }
    }
}
