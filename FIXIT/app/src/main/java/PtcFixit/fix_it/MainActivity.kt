package PtcFixit.fix_it

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkAuthenticationAndNavigate()

        val btnIniciarSesionTaller = findViewById<Button>(R.id.btnIniciarSesionTaller)
        val btnRegistrarseTaller = findViewById<Button>(R.id.btnRegistrarseTaller)
        val txtOlvideMicontra = findViewById<TextView>(R.id.txtOlvideMicontra)

        txtOlvideMicontra.setOnClickListener {
            val intentO =Intent(this@MainActivity,RecuperarContraParte2::class.java)
            startActivity(intentO)
            finish()
        }

        btnIniciarSesionTaller.setOnClickListener {
            startActivity(Intent(this@MainActivity, login_fixIT::class.java))
            finish()
        }

        btnRegistrarseTaller.setOnClickListener {
            startActivity(Intent(this@MainActivity, RegistrarseAdmin::class.java))
            finish()
        }
    }

    private fun checkAuthenticationAndNavigate() {
        if (isLoggedIn) {
            startActivity(Intent(this, Menu1Activity::class.java))
            finish()
        }
    }
}