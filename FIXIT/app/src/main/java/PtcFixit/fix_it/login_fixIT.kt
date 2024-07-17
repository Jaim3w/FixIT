package PtcFixit.fix_it

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class login_fixIT : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_fix_it_taller)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreoAdmin=findViewById<EditText>(R.id.txtCorreologin)
        val txtContrasenaLogin=findViewById<EditText>(R.id.txtContrasena)
        val imgVerContraLogin=findViewById<ImageView>(R.id.imgVerContraLogin)
        val btnIniciar=findViewById<Button>(R.id.btnSiguienteLogin)

        fun hashSHA256(input: String):String{
            val bytes =MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
            return bytes.joinToString("") { "%02x".format(it) }
        }

        btnIniciar.setOnClickListener{
            val pantallaPrincipal=Intent(this,Menu1Activity::class.java)

            GlobalScope.launch(Dispatchers.IO){
                val objconexion=ClaseConexion().cadenaConexion()

                val contraseniaEncriptada= hashSHA256(txtContrasenaLogin.text.toString())


                val comprobacion=objconexion?.prepareStatement("select * from Usuario where correoElectronico = ? and Contrasena = ?")!!
                comprobacion.setString(1,txtCorreoAdmin.text.toString())
                comprobacion.setString(2,contraseniaEncriptada)
                val resultado=comprobacion.executeQuery()
                if(resultado.next()){
                    startActivity(pantallaPrincipal)
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@login_fixIT, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                        println("contraseña $contraseniaEncriptada ")
                    }
                }
                try{
                    if(txtCorreoAdmin == null || txtContrasenaLogin == null){
                        throw IllegalArgumentException("El nombre o el correo no pueden ser nulos")
                    }

                }catch (e : IllegalArgumentException){
                    println("Error ${e.message}")
                }
            }
            imgVerContraLogin.setOnClickListener{
                if (txtContrasenaLogin.inputType==InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                    txtContrasenaLogin.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }else{
                    txtContrasenaLogin.inputType=InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
            }
        }
    }
}