import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties
import java.sql.PreparedStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast

class DatabaseOperaton{
    private val url = "jdbc:mysql://127.0.0.1:3306/pruebaparalaravel"
    private val username = "root"
    private val password = ""

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
            prepareStatement.setString(2,apellido)
            prepareStatement.executeUpdate()
            connection.close()
        }catch (e: SQLException){
            e.printStackTrace()
        }
    }

    fun select(connection: Connection){

    }
}
