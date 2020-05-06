package com.example.fazyksiezyca

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ustawienia.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception

class Ustawienia : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ustawienia)
        pokazywanieustawien()
    }

    override fun onResume() {
        pokazywanieustawien()
        super.onResume()
    }
    private fun FileExists(path:String):Boolean{
        val file=baseContext.getFileStreamPath(path)
        return file.exists()
    }

    fun pokazywanieustawien(){
        //ustawienie "wyboru" dotychczas zapisanego w pliku
        try{
            val filename="opcje.txt"
            if (FileExists(filename)){
                val file= InputStreamReader(openFileInput(filename))
                val br= BufferedReader(file)

                var line=br.readLine()
                val opcje=line.split(',')
                if(opcje.size==2){
                    if(polkulaN.text.toString().equals(opcje[0])){
                        radioGroup2.check(polkulaN.id)
                    }
                    else{
                        radioGroup2.check(polkulaS.id)
                    }

                    if(simple.text.toString().equals(opcje[1])){
                        radioGroup.check(simple.id)
                    } else if(conway.text.toString().equals(opcje[1])){
                        radioGroup.check(conway.id)
                    }else if(trig1.text.toString().equals(opcje[1])){
                        radioGroup.check(trig1.id)
                    }else{
                        radioGroup.check(trig2.id)
                    }
                }
                else{
                    radioGroup.check(trig1.id)
                    radioGroup2.check(polkulaN.id)
                }
                file.close()

            }
            else{
                Toast.makeText(this, "Nie znaleziono pliku",Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, "Korzystasz z domyślnych ustawień, ale możesz je zmienić",Toast.LENGTH_LONG).show()
        }
    }

    fun zapisz(v: View){
        //sprawdzanie co wybrał użytkownik
        val filename="opcje.txt"
        val file= OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE))
        val radio1: RadioButton = findViewById(radioGroup.checkedRadioButtonId)
        val radio2: RadioButton = findViewById(radioGroup2.checkedRadioButtonId)
        val komunikat:String=radio2.text.toString()+","+radio1.text.toString()

        file.write(komunikat)

        file.flush()
        file.close()
        Toast.makeText(this, "Ustawienia zostały zmienione", Toast.LENGTH_LONG).show()

        //przejście do ekranu startowego
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
