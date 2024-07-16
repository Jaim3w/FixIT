package Modelo


import java.sql.Connection
import java.sql.DriverManager

class  ClaseConexion {
    fun cadenaConexion():Connection?{
        try{
            
            val ip="jdbc:oracle:thin:@172.18.224.1:1521:xe"
            val usuario= "lacobraa"
            val contrasena= "qatar24"
            val conexion=DriverManager.getConnection(ip, usuario , contrasena)
            return conexion
        }catch (e:Exception){
            println("El error es este $e")
            return null
        }
    }
}