package com.example.myapplication

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

data class Ders_Odev (var id:Int = 0, var key:String = "",var value:String = "")

class DBHelper(val context: Context) : SQLiteOpenHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION) {
    private val TABLE_NAME="Ders_Odev"
    private val ID = "id"
    private val KEY = "column_key"
    private val VALUE = "column_value"
    companion object {
        private val DATABASE_NAME = "DATABASE"//database adı
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY VARCHAR(256) UNIQUE, $VALUE  VARCHAR(256))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertData(data:Ders_Odev){
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY , data.key)
        contentValues.put(VALUE, data.value)
        val result = sqliteDB.insert(TABLE_NAME,null,contentValues)

        Toast.makeText(context,if(result != -1L) "Veritabanı bağlantısı başarılı" else "Veritabanı bağlantısı başarısız", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    fun readData():Ders_Odev{
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query,null)
        val data = Ders_Odev()
        if(result.moveToFirst()){
            data.id = result.getString(result.getColumnIndex(ID)).toInt()
            data.key = result.getString(result.getColumnIndex(KEY))
            data.value = result.getString(result.getColumnIndex(VALUE))
        }
        result.close()
        sqliteDB.close()
        return data
    }

    @SuppressLint("Range")
    fun readDataByKey(value: String):Ders_Odev{
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $KEY = ?"
        val result = sqliteDB.rawQuery(query, arrayOf(value))
        val data = Ders_Odev()
        if(result.moveToFirst()){
            data.id = result.getString(result.getColumnIndex(ID)).toInt()
            data.key = result.getString(result.getColumnIndex(KEY))
            data.value = result.getString(result.getColumnIndex(VALUE))
        }
        result.close()
        sqliteDB.close()
        return data
    }


}

class MainActivity : AppCompatActivity() {

    val db by lazy {DBHelper(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            showData(db.readDataByKey(binding.editTextView.text.toString()))
        }
        db.insertData(Ders_Odev(key = "bil359", value = "Hello World from database"))

    }

    fun showData(data:Ders_Odev){
        val text = getString(R.string.result_string, data.value)
        binding.textView.text = text
    }

}