package PtcFixit.fix_it

import android.os.Bundle
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
        val txtNum1=findViewById<EditText>(R.id.txtPrimerNum)
        val txtNum2=findViewById<EditText>(R.id.txtSegundoNum)
        val txtNum3=findViewById<EditText>(R.id.txtTercerNum)
        val txtNum4=findViewById<EditText>(R.id.txtCuartoNum)
        val btnContinuar=findViewById<Button>(R.id.btnContinuar)


    }
}