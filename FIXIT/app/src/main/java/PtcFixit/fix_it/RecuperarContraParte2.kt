package PtcFixit.fix_it

import android.content.Intent
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
    companion object variablesGlobales{
        lateinit var correoIngresado : String
        val codigoRecu=(1000..9000).random()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contra_parte2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreo=findViewById<EditText>(R.id.txtNuevaContra)
        val btnContinuar=findViewById<Button>(R.id.btnContinuar)

        btnContinuar.setOnClickListener{

            val correoIngre=txtCorreo.text.toString()
            var valiCamp=false

            if(correoIngre.isEmpty()){
                txtCorreo.error="El correo es obligatorio"
                valiCamp=true
            }else{
                txtCorreo.error=null
            }
            if(!correoIngre.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+[.][a-z]+"))){
                txtCorreo.error="El correo no tiene un formato valido"
                valiCamp=true
            }else{
                txtCorreo.error=null
            }
            if(!valiCamp){
                try {
                    val intent=Intent(this,RecuperarContra::class.java)
                    CoroutineScope(Dispatchers.Main).launch {
                        correoIngresado=txtCorreo.text.toString()
                        startActivity(intent)
                        EnviarRecuperacion(
                            correoIngresado,
                            "Recuperacion de contraseña",
                            "Este es tu codigo de verificacion $codigoRecu"
                        )
                    }
                }catch (e:Exception){
                    println("El error es: $e")
                }
            }

        }
    }
}