package PtcFixit.fix_it

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecuperarContraParte2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contra_parte2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreo=findViewById<EditText>(R.id.txtCorreo)
        val btnGuardar=findViewById<Button>(R.id.btnContinuar)

        btnGuardar.setOnClickListener{
            CoroutineScope(Dispatchers.Main).launch {
                val correoIngresado=txtCorreo.text.toString()

                val codigoRecu=(1000..9000).random()

                EnviarRecuperacion(
                    correoIngresado,
                    "Recuperacion de contraseña",
                    "Este es tu codigo de verificacion $codigoRecu"
                )
            }
        }
    }
}