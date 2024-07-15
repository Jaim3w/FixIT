package PtcFixit.fix_it

import Modelo.ClaseConexion
import Modelo.listadoCarros
import Modelo.listadoCita
import RecyclerViewHelpersMain.AdaptadorCarros
import RecyclerViewHelpersMain.AdaptadorCitas
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException

class Menu1Activity : AppCompatActivity() {
    lateinit var rcvCitas: RecyclerView
    lateinit var adaptadorCitas: AdaptadorCitas
    lateinit var rcvCarrosDespachados: RecyclerView
    lateinit var adaptadorCarros: AdaptadorCarros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupNavClickListeners()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@Menu1Activity, Menu1Activity::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@Menu1Activity,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                startActivity(intent, options.toBundle())
                finish()
            }
        })

        rcvCarrosDespachados = findViewById(R.id.rcvCarrosDespachados)
        rcvCarrosDespachados.layoutManager = LinearLayoutManager(this)
        adaptadorCarros = AdaptadorCarros(emptyList(), this)
        rcvCarrosDespachados.adapter = adaptadorCarros

        rcvCitas = findViewById(R.id.rcvCitas)
        rcvCitas.layoutManager = LinearLayoutManager(this)
        adaptadorCitas = AdaptadorCitas(emptyList(), this)
        rcvCitas.adapter = adaptadorCitas

        mostrarCitas()
        mostrarCarros()
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

    private fun mostrarCitas() {
        CoroutineScope(Dispatchers.IO).launch {
            val nuevasCitas = obtenerCitas()

            withContext(Dispatchers.Main) {
                adaptadorCitas.actualizarCitas(nuevasCitas)
            }
        }
    }

    private fun mostrarCarros() {
        CoroutineScope(Dispatchers.IO).launch {
            val nuevosCarros = obtenerCarros()

            withContext(Dispatchers.Main) {
                adaptadorCarros.actualizarCarros(nuevosCarros)
            }
        }
    }

    private fun obtenerCitas(): List<listadoCita> {
        val objConexion = ClaseConexion().cadenaConexion()
        val listadoCitas = mutableListOf<listadoCita>()

        try {
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery(
                "SELECT c.UUID_cita, c.Dui_cliente, cli.Nombre AS Nombre_cliente, c.Dui_Empleado, c.Fecha_cita, c.Descripcion\n" +
                        "FROM Cita c\n" +
                        "INNER JOIN Cliente cli ON c.Dui_cliente = cli.Dui_cliente\n" +
                        "ORDER BY c.Fecha_cita ASC"
            )

            while (resultSet?.next() == true) {
                val uuidCita = resultSet.getString("UUID_cita")
                val nombreCliente = resultSet.getString("Nombre_cliente")
                val duiEmpleado = resultSet.getString("Dui_Empleado")
                val fechaCita = resultSet.getString("Fecha_cita")
                val descripcion = resultSet.getString("Descripcion")

                val cita = listadoCita(
                    uuidCita,
                    nombreCliente,
                    duiEmpleado,
                    fechaCita,
                    descripcion
                )

                listadoCitas.add(cita)
            }

            resultSet?.close()
            statement?.close()
        } catch (e: SQLException) {
            Log.e("Menu1Activity", "Error al obtener citas: ${e.message}")
        } finally {
            objConexion?.close()
        }
        return listadoCitas
    }

    private fun obtenerCarros(): List<listadoCarros> {
        val objConexion = ClaseConexion().cadenaConexion()
        val listadoCarros = mutableListOf<listadoCarros>()

        try {
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Carro")

            while (resultSet?.next() == true) {
                val placaCarro = resultSet.getString("Placa_carro")
                val duiCliente = resultSet.getString("Dui_cliente")
                val uuidModelo = resultSet.getString("UUID_modelo")
                val color = resultSet.getString("Color")
                val año = resultSet.getString("Año")
                val imagenCarro = resultSet.getString("ImagenCarro")
                val fechaRegistro = resultSet.getString("FechaRegistro")
                val descripcion = resultSet.getString("Descripcion")

                val carro = listadoCarros(
                    placaCarro,
                    duiCliente,
                    uuidModelo,
                    color,
                    año,
                    imagenCarro,
                    fechaRegistro,
                    descripcion
                )

                listadoCarros.add(carro)
            }

            resultSet?.close()
            statement?.close()
        } catch (e: SQLException) {
            Log.e("Menu1Activity", "Error al obtener carros: ${e.message}")
        } finally {
            objConexion?.close()
        }
        return listadoCarros
    }
}
