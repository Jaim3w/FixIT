package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.dataClassRoles
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.util.UUID

class RegisterAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val CorreoAdmin=findViewById<EditText>(R.id.txtCorreoRegister)
        val ContrasenaAdmin=findViewById<EditText>(R.id.txtContrasenaAdmin)
        val Registrar=findViewById<Button>(R.id.btnSiguienteRegister)
        val Vercontrasena=findViewById<ImageView>(R.id.imgVerContraRegister)
        val SpinnerRol=findViewById<Spinner>(R.id.spRoles)

        fun hashSHA256(contrasenaEscrita: String):String{
            val bytes=MessageDigest.getInstance("SHA-256").digest(contrasenaEscrita.toByteArray())
            return bytes.joinToString("") { "%02x".format(it)}
        }

        Registrar.setOnClickListener{
            GlobalScope.launch(Dispatchers.IO){

                val objConexion=ClaseConexion().cadenaConexion()

                val contrasenaEncriptada= hashSHA256(ContrasenaAdmin.text.toString())
                val crearUser=objConexion?.prepareStatement("Insert into Usuario(UUID_usuario,UUID_rol,CorreoElectronico,Contrasena) values(?,?,?,?) ")!!
                crearUser.setString(1, UUID.randomUUID().toString())
                crearUser.setString(2,UUID.randomUUID().toString())
                crearUser.setString(3,UUID.randomUUID().toString())
                crearUser.setString(4,CorreoAdmin.text.toString())
                crearUser.setString(5,contrasenaEncriptada)
                crearUser.executeUpdate()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegisterAdmin, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                    CorreoAdmin.setText("")
                    ContrasenaAdmin.setText("")
                }
            }
        }
        Vercontrasena.setOnClickListener {
            if (ContrasenaAdmin.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                ContrasenaAdmin.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                ContrasenaAdmin.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        fun obtenerRoles(): List<dataClassRoles>{
            val objconexion=ClaseConexion().cadenaConexion()
            
            val stateman=objconexion?.createStatement()
            val resulset=stateman?.executeQuery("select * from Rol")!!

            val listadoRoles = mutableListOf<dataClassRoles>()
            while (resulset.next()){
                val uuid=resulset.getString("RolUUID")
                val nombre=resulset.getString("nombreRol")
                val unRolCompleto=dataClassRoles(uuid,nombre)
                listadoRoles.add(unRolCompleto)
            }
            return listadoRoles
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listaRoles=obtenerRoles()
            val nombreRol=listaRoles.map { it.NOMBRE }

            withContext(Dispatchers.Main){
              val adapter= ArrayAdapter(this@RegisterAdmin,android.R.layout.simple_spinner_dropdown_item,nombreRol)
                SpinnerRol.adapter=adapter
            }
        }
    }
}