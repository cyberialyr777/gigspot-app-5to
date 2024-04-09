package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class mySQliteHelper(context: Context): SQLiteOpenHelper(context,"sessions",null,1) {

    companion object {
        private const val TABLE_SESIONES = "sesiones"
        private const val COLUMN_ID = "_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val ordenCreacion = "CREATE TABLE sesiones"+
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT, pass TEXT)"

        db!!.execSQL(ordenCreacion)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val ordenBorrado = "DROP TABLE IF EXISTS sesiones"
        db!!.execSQL(ordenBorrado)
        onCreate(db)
    }

    fun anyadirDatos(email:String, pass:String){
        val datos = ContentValues()
        datos.put("email", email)
        datos.put("pass",pass)

        val db = this.writableDatabase
        db.insert("sesiones", null, datos)
        db.close()
    }

    fun deleteSesion(id: Int): Boolean {
        val db = this.writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(id.toString())

        val resultado = db.delete(TABLE_SESIONES, whereClause, whereArgs)
        db.close()

        // Devuelve true si se eliminó al menos una fila, de lo contrario, devuelve false
        return resultado != 0
    }

    fun gettext(): Cursor? {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM sesiones", null)
        return cursor
    }
}