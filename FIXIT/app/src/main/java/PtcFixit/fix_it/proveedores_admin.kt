package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.RCVproveedor
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import reccyclerviewherlperProveedores.Adaptador
import java.sql.SQLException

class proveedores_admin : AppCompatActivity() {

    private val REQUEST_CODE_CREAR_PROVEEDOR = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Asegúrate de tener implementado este método si no es propio del SDK de Android
        setContentView(R.layout.activity_proveedores_admin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregarNuevoProveedor = findViewById<Button>(R.id.btnAgregarProveedor)
        val rcvProveedores = findViewById<RecyclerView>(R.id.rcvProveedores)
        rcvProveedores.layoutManager = LinearLayoutManager(this)

        btnAgregarNuevoProveedor.setOnClickListener {
            val intent = Intent(this, crear_proveedores::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREAR_PROVEEDOR)
        }

        // Cargar proveedores al iniciar la actividad

    }

    override fun onResume() {
        super.onResume()

        cargarProveedores()
    }

            private fun cargarProveedores() {
        CoroutineScope(Dispatchers.IO).launch {
            val proveedoresList = obtenerProveedores()
            withContext(Dispatchers.Main) {
                val adaptador = Adaptador(proveedoresList)
                val rcvProveedores = findViewById<RecyclerView>(R.id.rcvProveedores)
                rcvProveedores.adapter = adaptador
            }
        }
    }

    private fun obtenerProveedores(): List<RCVproveedor> {
        val objConexion = ClaseConexion().cadenaConexion() ?: return emptyList()
        val listaProveedores = mutableListOf<RCVproveedor>()

        try {
            val statement = objConexion.createStatement()
            val resultSet = statement.executeQuery("SELECT * FROM Proveedor")
            while (resultSet.next()) {
                val duiProv = resultSet.getString("Dui_proveedor")
                val nombreProv = resultSet.getString("Nombre")
                val apellidoProv = resultSet.getString("Apellido")
                val telefonoProv = resultSet.getString("Telefono")
                val correoProv = resultSet.getString("Correo_Electronico")
                val direccionProv = resultSet.getString("Direccion")
                val proveedor = RCVproveedor(duiProv, nombreProv, apellidoProv, telefonoProv, correoProv, direccionProv)
                listaProveedores.add(proveedor)
            }
            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            Log.e("obtenerProveedores", "Error al obtener proveedores: ${e.message}")
        } finally {
            objConexion.close()
        }

        return listaProveedores
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREAR_PROVEEDOR && resultCode == Activity.RESULT_OK) {
            cargarProveedores()
        }
    }

    private fun setupNavClickListeners() {
        val navView = findViewById<View>(R.id.include_nav)

        val imgHomenav = navView.findViewById<ImageView>(R.id.imgHomenav)
        val imgRepuestosnav = navView.findViewById<ImageView>(R.id.imgRepuestosnav)
        val imgProveedoresnav = navView.findViewById<ImageView>(R.id.imgProveedoresnav)
        val imgCarrosnav = navView.findViewById<ImageView>(R.id.imgCarrosnav)
        val imgCitasnav = navView.findViewById<ImageView>(R.id.imgCitasnav)

        val clickListener = View.OnClickListener { v ->
            val currentActivity = this::class.java
            val targetActivity = when (v.id) {
                R.id.imgHomenav -> Menu1Activity::class.java
                R.id.imgRepuestosnav -> repuestos_admin::class.java
                R.id.imgProveedoresnav -> proveedores_admin::class.java
                R.id.imgCarrosnav -> carros_admin::class.java
                R.id.imgCitasnav -> citas::class.java
                else -> null
            }
            if (targetActivity != null && currentActivity != targetActivity) {
                val intent = Intent(this, targetActivity)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                startActivity(intent, options.toBundle())
                finish()
            }
        }

        imgHomenav.setOnClickListener(clickListener)
        imgRepuestosnav.setOnClickListener(clickListener)
        imgProveedoresnav.setOnClickListener(clickListener)
        imgCarrosnav.setOnClickListener(clickListener)
        imgCitasnav.setOnClickListener(clickListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Menu1Activity::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.fade_in,
            R.anim.fade_out
        )
        startActivity(intent, options.toBundle())
        finish()
    }
}



