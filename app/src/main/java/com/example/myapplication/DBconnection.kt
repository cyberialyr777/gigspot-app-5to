import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties
import java.sql.PreparedStatement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.Gravity
import android.widget.Toast
import java.sql.Driver
import kotlin.math.log

class DatabaseOperaton{
    // private val ip = "196.168.0.25:1433"
    private val db = "jdbc:postgresql://192.168.43.5:3306/pruebaparalaravel"
    private val username = "root"
    private val password = ""

    data class PersonaModel(
        val id: Int,
        val userName: String,
        val email: String,
        val password: String,
        val name: String,
        val lastName: String,
        val age: Int
    )

    fun conexion(): MutableList<PersonaModel> {
        val connection = DriverManager
            .getConnection(db,username,password)
        println(connection.isValid(0))

        val query = connection.prepareStatement("SELECT * FROM personas")
        val resul = query.executeQuery()
        val personas = mutableListOf<PersonaModel>()

        while(resul.next()){
            val id = resul.getInt("id")
            val userName = resul.getString("userName")
            val email = resul.getString("email")
            val password = resul.getString("password")
            val name = resul.getString("name")
            val lastName = resul.getString("lastName")
            val age = resul.getInt("age")

            personas.add(PersonaModel(id,userName,email,password,name,lastName,age))
        }

        println(personas)

        return personas
    }
}
