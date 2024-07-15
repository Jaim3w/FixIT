package PtcFixit.fix_it

import CitasHelpers.tbCita
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

    private lateinit var adapter: Adaptador

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Asegúrate de tener implementado este método si no es propio del SDK de Android
        setContentView(R.layout.activity_proveedores_admin)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAgregarProveedor = findViewById<Button>(R.id.btnAgregarProveedor)

        btnAgregarProveedor.setOnClickListener {
            val intent = Intent(this, crear_proveedores::class.java)
            startActivity(intent)

            // Llamar a obtenerDatos() después de agregar un proveedor
            obtenerDatos()
        }

        // Aquí debes inicializar y configurar tu RecyclerView y Adaptador
        // recyclerView = findViewById(R.id.rcvProveedores)
        // recyclerView.layoutManager = LinearLayoutManager(this)
        // adapter = Adaptador(emptyList()) // Inicializa el adaptador con una lista vacía inicialmente
        // recyclerView.adapter = adapter

        // Debes llamar a cargarProveedores() para cargar los datos iniciales en el RecyclerView
        // cargarProveedores()
    }

    private fun obtenerDatos(): List<RCVproveedor> {
        val objConexion = ClaseConexion().cadenaConexion()
        val statement = objConexion?.createStatement()
        val resultSet = statement?.executeQuery("select * from Proveedor")!!

        val listadoProv = mutableListOf<RCVproveedor>()

        while (resultSet.next()) {
            val dui = resultSet.getString("Dui_proveedor")
            val nombre = resultSet.getString("Nombre")
            val apellido = resultSet.getString("Apellido")
            val telefono = resultSet.getString("Telefono")
            val correo = resultSet.getString("Correo_Electronico")
            val direccion = resultSet.getString("Direccion")
            val proveedor = RCVproveedor(dui, nombre, apellido, telefono, correo, direccion)
            listadoProv.add(proveedor)
        }

        // Actualiza el RecyclerView con los nuevos datos obtenidos
        adapter.actualizarRecyclerView(listadoProv)

        return listadoProv
    }


    //-----------------------------

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




