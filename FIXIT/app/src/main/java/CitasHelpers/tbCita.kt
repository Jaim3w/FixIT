package CitasHelpers

data class tbCita(
    val uuid: String,
    val cliente: String,
    val empleado: String ,
    var fecha: String,
    var hora: String,
    var descripcion: String
)
