package CitasHelpers

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolderCitas(view: View): RecyclerView.ViewHolder(view) {
    val lblcliente: TextView = view.findViewById(R.id.lblCliente)
    val lblFecha: TextView = view.findViewById(R.id.lblFechaCita)
    val lblHora: TextView = view.findViewById(R.id.lblHoraCita)
    val imgEditar: ImageView = view.findViewById(R.id.imgeditar)
    val imgEliminar: ImageView = view.findViewById(R.id.imgBorrar)
}