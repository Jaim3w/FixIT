package PtcFixit.fix_it

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RecuperarContra : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recuperar_contra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNum1 = findViewById<EditText>(R.id.txtPrimerNum)
        val txtNum2 = findViewById<EditText>(R.id.txtSegundoNum)
        val txtNum3 = findViewById<EditText>(R.id.txtTercerNum)
        val txtNum4 = findViewById<EditText>(R.id.txtCuartoNum)
        val btnContinuar = findViewById<Button>(R.id.btnGurdadContra)

        txtNum1.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                txtNum2.requestFocus();
                println("esto es lo que escruibe $v")
                println("esto es lo que escruibe $keyCode")
                println("esto es lo que escruibe ${txtNum1.text.toString()}")
                return@OnKeyListener true
            }
            false
        })
        txtNum2.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                txtNum3.requestFocus();
                println("esto es lo que escruibe $v")
                println("esto es lo que escruibe $keyCode")
                println("esto es lo que escruibe ${txtNum2.text.toString()}")
                return@OnKeyListener true
            }
            false
        })
        txtNum3.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP) {
                txtNum4.requestFocus();
                println("esto es lo que escruibe $v")
                println("esto es lo que escruibe $keyCode")
                println("esto es lo que escruibe ${txtNum3.text.toString()}")
                return@OnKeyListener true
            }
            false
        })
        val todosNum = "$txtNum1  $txtNum2  $txtNum3  $txtNum4"
        btnContinuar.setOnClickListener {
            val intent = Intent(this, RecuperarContra3::class.java)
            startActivity(intent)
        }
    }
}