package reccyclerviewherlperProveedores

import PtcFixit.fix_it.R
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val txtProveedor = view.findViewById<TextView>(R.id.txtproveedorCard)
    val txtTelefono = view.findViewById<TextView>(R.id.txttelefonoCard)
    val imgBorrar = view.findViewById<ImageView>(R.id.imgBorrar)
    val imgEditar = view.findViewById<ImageView>(R.id.imgEditar)
}