package RepuestosHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderRepuestos(view: View): RecyclerView.ViewHolder(view) {
    val imgRepuesto: ImageView = view.findViewById(R.id.imgRepCard)
    val txtNombreRepuesto: TextView = view.findViewById(R.id.txtNombreRepCard)
    val txtCategoria: TextView = view.findViewById(R.id.txtCategoria)
    val txtPrecio: TextView = view.findViewById(R.id.txtPrecio)
    val txtuuid: TextView = view.findViewById(R.id.txtuuid)
    val imgActuRep: ImageView = view.findViewById(R.id.imgActuRep)
    val imgBorrarRep: ImageView = view.findViewById(R.id.imgBorrarRep)

}