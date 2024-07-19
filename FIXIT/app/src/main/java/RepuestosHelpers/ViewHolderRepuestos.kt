package RepuestosHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderRepuestos(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreRepuesto: TextView = view.findViewById(R.id.txtNombreRepCard)
    val imgRepuesto: ImageView = view.findViewById(R.id.imgRepCard)
    val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
    val txtCategoria: TextView = view.findViewById(R.id.txtCategoria)
    val txtuuid: TextView = view.findViewById(R.id.txtuuid)
    val imgActuRep: Button = view.findViewById(R.id.imgActuRep)
    val imgBorrarRep: Button = view.findViewById(R.id.imgBorrarRep)

    init {
        println("ViewHolder initialized: txtNombreRepuesto = $txtNombreRepuesto, imgRepuesto = $imgRepuesto, txtPrecio = $txtPrecio, txtCategoria = $txtCategoria, txtuuid = $txtuuid, imgActuRep = $imgActuRep, imgBorrarRep = $imgBorrarRep")
    }
}
