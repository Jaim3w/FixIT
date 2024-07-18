package RepuestosHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderRepuestos(view: View): RecyclerView.ViewHolder(view) {
    val txtNombreRepuesto: TextView = view.findViewById(R.id.txtNombreRepCard)
    val imgRep: ImageView = view.findViewById(R.id.imgRepCard)
    val txtCategoria: TextView = view.findViewById(R.id.txtCategoria)
    val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
    val imgActuRep: ImageView = view.findViewById(R.id.imgActuRep)
    val imgBorrarRep: ImageView = view.findViewById(R.id.imgBorrarRep)
}