package PtcFixit.fix_it

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class splash_screen : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)
        val motionLayout = findViewById<View>(R.id.motionLayout)
        ViewCompat.setOnApplyWindowInsetsListener(motionLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        GlobalScope.launch(Dispatchers.Main) {
            delay(3000)

            val intent = if (isFirstTime()) {
                markFirstTime()
                Intent(this@splash_screen, inicio::class.java)
            } else {
                Intent(this@splash_screen, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", true)
    }

    private fun markFirstTime() {
        sharedPreferences.edit().putBoolean("isFirstTime", false).apply()
    }
}