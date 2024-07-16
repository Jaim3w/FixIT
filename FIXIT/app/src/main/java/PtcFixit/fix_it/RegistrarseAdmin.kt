package PtcFixit.fix_it

import Modelo.ClaseConexion
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import oracle.security.crypto.core.MessageDigest
import java.util.UUID

class RegistrarseAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrarse_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtCorreo=findViewById<EditText>(R.id.txtCorreoRegister)
        val txtContrasenaRegister=findViewById<EditText>(R.id.txtContrasenaAdmin)
        val imgVer=findViewById<ImageView>(R.id.imgVerContraRegister)
        val spinner=findViewById<Spinner>(R.id.spRoles)
        val btnRegistrar=findViewById<Button>(R.id.btnSiguienteRegister)

        //funcion para la encriptacion
        fun hashSHA256(contraseniaEscrita : String):String{
            val bytes=java.security.MessageDigest.getInstance("SHA_256").digest(contraseniaEscrita.toByteArray())
            return bytes.joinToString(""){"0%2x".format(it)}
        }
        //fun para crear cuenta
        btnRegistrar.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                val objConexion=ClaseConexion().cadenaConexion()
                val contraseniaEncriptada=hashSHA256(txtContrasenaRegister.text.toString())
                val crearUsuario=
                    objConexion?.prepareStatement("INSERT INTO Usuario(UUID_usuario,UUID_rol,CorreoElectronico,Contrasena) values(?,?,?,?)")!!
                 crearUsuario.setString(1,UUID.randomUUID().toString())
                //falta un crear que es el de rol del spinner que seria el numero 2 luego correo 3 y el otro 4
                crearUsuario.setString(2,txtCorreo.text.toString())
                crearUsuario.setString(3,contraseniaEncriptada)
                crearUsuario.executeQuery()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegistrarseAdmin, "Usuario creado", Toast.LENGTH_SHORT).show()
                    txtCorreo.setText("")
                    txtContrasenaRegister.setText("")
                }
            }
        }



        imgVer.setOnClickListener{
            if(txtContrasenaRegister.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                txtContrasenaRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else{
                txtContrasenaRegister.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }
}