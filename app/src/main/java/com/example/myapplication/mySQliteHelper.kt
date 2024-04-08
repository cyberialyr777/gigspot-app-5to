package com.example.myapplication

import android.content.ContentValues
import android.content.Context
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

    fun deleteRecordById(id: Int) {
        val deleteQuery = "DELETE FROM $TABLE_SESIONES WHERE $COLUMN_ID = ?"

        val db = this.writableDatabase
        db.execSQL(deleteQuery, arrayOf(id.toString()))
        db.close()
    }
}