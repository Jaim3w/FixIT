package PtcFixit.fix_it

import android.media.MediaCas
import android.se.omapi.Session
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage



    suspend fun EnviarRecuperacion(receptor:String,asunto:String,mensaje:String) = withContext(Dispatchers.IO) {

        val props=Properties().apply {
            put("mail.smtp.host","smtp.gmail.com")
            put("mail.smtp.socketFactory.port","465")
            put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory")
            put("mail.smtp.auth","true")
            put("mail.smtp.port","465")
        }

        val session=javax.mail.Session.getInstance(props,object :javax.mail.Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("asistenciafixit@gmail.com","ynsk wpub yylq ujkh")
            }
        })

        try{
            val message=MimeMessage(session).apply {
                setFrom(InternetAddress("asistenciafixit@gmail.com"))
                addRecipient(Message.RecipientType.TO,InternetAddress(receptor))
                subject= asunto
                setText(mensaje)
            }
            Transport.send(message)
            println("Correo enviado exitosamente")
        }catch (e:MessagingException){
            e.printStackTrace()
            println("correo no enviado,error: ${e.message}")
        }
    }
