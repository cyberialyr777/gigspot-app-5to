import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties
import java.sql.PreparedStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


/*
class DBconnection(): Connection{
    fun conectionToDataBase(){
        val url = "jdbc:mysql://localhost:3306/pruebaparalaravel"
        val username = "root"
        val password = ""
        var connection: Connection? = null

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")

            val connectionProps = Properties()
            connectionProps["user"] = username
            connectionProps["password"] = password

            val conn: Connection = DriverManager.getConnection(url,connectionProps)
        }catch (e: SQLException){
            e.printStackTrace()
        }catch (e: Exception){
            e.printStackTrace()
        }

        return connection

    }

    fun InsertarPersona(){

    }
}
*/

object DatabaseOperaton{
    private const val url = "jdbc:mysql://localhost:3306/pruebaparalaravel"
    private const val username = "root"
    private const val password = ""

    fun getConnection(): Connection?{
        var connection: Connection? = null

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            val conn: Connection = DriverManager.getConnection(url, username, password)
        }catch (e: SQLException){
            e.printStackTrace()
        }catch (e: Exception){
            e.printStackTrace()
        }
        return connection
    }

    fun insert(connection: Connection, nombre: String, apellido: String){
        val query = "INSERT INTO usuarios (nombre,apellido) VALUES (?,?)"
        try {
            val prepareStatement = connection.prepareStatement(query)
            prepareStatement.setString(1,nombre)
            prepareStatement.setString(1,apellido)
            prepareStatement.executeUpdate()

            connection.close()
        }catch (e: SQLException){
            e.printStackTrace()
        }
    }
}
