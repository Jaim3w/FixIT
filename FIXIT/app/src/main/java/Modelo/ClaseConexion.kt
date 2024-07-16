package Modelo


import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion():Connection?{
        try{
            val url="jdbc:oracle:thin:@192.168.1.6:1521:xe"
            val usuario= "fito_DEVE"
            val contrasena= "fito17"
            val conexion=DriverManager.getConnection(url, usuario , contrasena)
            return conexion
        }catch (e:Exception){
            println("El error es este $e")
            return null
        }
    }
}