package CitasHelpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ViewModelCita : ViewModel() {

    private val citas1 = MutableLiveData<MutableList<tbCita>>().apply { value = mutableListOf() }
    val citass: LiveData<MutableList<tbCita>> get() = citas1

    fun agregarCita(cita: tbCita){
        val listaActual = citas1.value?.toMutableList() ?: mutableListOf()
        listaActual.add(cita)
        citas1.value = listaActual
    }

    fun actualizarCita(cita: tbCita){
        val listaActual = citas1.value?.toMutableList() ?: mutableListOf()
        val index = listaActual.indexOfFirst { it.uuid == cita.uuid }
        if (index != -1) {
            listaActual[index] = cita
        }
        citas1.value = listaActual
    }

    fun eliminarCita(cita: tbCita){
        val listaActual = citas1.value?.toMutableList() ?: mutableListOf()
        listaActual.remove(cita)
    }


}