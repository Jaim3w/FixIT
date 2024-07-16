package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.dataClassClientes
import Modelo.dataClassRoles
import android.content.Intent
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

        fun obtenerRol() : List<dataClassRoles>{
            val objconexion=ClaseConexion().cadenaConexion()
            val statement=objconexion?.createStatement()
            val resultSet=statement?.executeQuery("select * From Rol")!!

            val listadoRol = mutableListOf<dataClassRoles>()

            while(resultSet.next()){

                val uuidRol = resultSet.getString("UUID_rol")
               val  nombre = resultSet.getString("Nombre")

                val Rol = dataClassRoles(uuidRol,nombre)

                listadoRol.add(Rol)
            }
            return listadoRol
        }
//        fun obtenerRol2() : String?{
//            val objconexion=ClaseConexion().cadenaConexion()
//            val statement=objconexion?.createStatement()
//            val resultSet1=statement?.executeQuery("select UUID_rol from Usuario where Nombre = 'Empleado'")!!
//
//            var uuidRol:String? = null
//
//            if(resultSet1.next()){
//                uuidRol=resultSet1.getString("UUID_rol")
//                println("este es el uuid traido del if $uuidRol")
//            }
//            println("Este es el uuid traido de la funcion $uuidRol")
//            return uuidRol
//
//        }

        val RolTraido=obtenerRol()

//        val EmpleadoTRaido=obtenerRol2()


        fun hashSHA256(contrasenaEscrita: String):String{
            val bytes=MessageDigest.getInstance("SHA-256").digest(contrasenaEscrita.toByteArray())
            return bytes.joinToString("") { "%02x".format(it)}
        }

        Registrar.setOnClickListener{
            val pantallamenu=Intent(this,Menu1Activity::class.java)
            GlobalScope.launch(Dispatchers.IO){

                val objConexion=ClaseConexion().cadenaConexion()

                val contrasenaEncriptada= hashSHA256(ContrasenaAdmin.text.toString())
                val crearUsuario=objConexion?.prepareStatement("Insert into usuario(UUID_usuario,UUID_rol,CorreoElectronico,Contrasena) values(?,?,?,?) ")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, RolTraido[SpinnerRol.selectedItemPosition].UUID_rol)
                crearUsuario.setString(3,CorreoAdmin.text.toString())
                crearUsuario.setString(4,contrasenaEncriptada)

                crearUsuario.executeUpdate()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@RegisterAdmin, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                    CorreoAdmin.setText("")
                    ContrasenaAdmin.setText("")

                    startActivity(pantallamenu)
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
                val uuid=resulset.getString("UUID_rol")
                val nombre=resulset.getString("Nombre")
                val unRolCompleto=dataClassRoles(uuid,nombre)
                listadoRoles.add(unRolCompleto)
            }
            return listadoRoles
        }
        CoroutineScope(Dispatchers.IO).launch {
            val listaRoles=obtenerRoles()
            val nombreRol=listaRoles.map { it.Nombre }

            withContext(Dispatchers.Main){
              val adapter= ArrayAdapter(this@RegisterAdmin,android.R.layout.simple_spinner_dropdown_item,nombreRol)
                SpinnerRol.adapter=adapter
            }
        }


    }
}