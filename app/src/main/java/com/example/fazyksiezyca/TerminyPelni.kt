package com.example.fazyksiezyca

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_terminy_pelni.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.time.LocalDate
import java.time.Period
import kotlin.math.round

@RequiresApi(Build.VERSION_CODES.O)
class TerminyPelni : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terminy_pelni)
        wylicziprzedstawterminy()
    }

    override fun onResume(){
        super.onResume()
        wylicziprzedstawterminy()
    }

    fun dodaj(v: View){
        var rokpom: Int?=rok.text.toString().toIntOrNull()
        if(rokpom!=null){
            rokpom+=1
            if(rokpom>2020 || rokpom<1900){
                rok.setText("2020")
                Toast.makeText(this,"Rok spoza zakresu!",Toast.LENGTH_LONG).show()
            }
            else{
                rok.setText(rokpom.toString())
            }
        }
        else{
            rok.setText("2020")
            Toast.makeText(this,"Rok spoza zakresu!",Toast.LENGTH_LONG).show()
        }
        wylicziprzedstawterminy()
    }

    fun odejmij(v: View){
        var rokpom: Int?=rok.text.toString().toIntOrNull()
        if(rokpom!=null){
            rokpom-=1
            if(rokpom>2020 || rokpom<1900){
                rok.setText("2020")
                Toast.makeText(this,"Rok spoza zakresu!",Toast.LENGTH_LONG).show()
            }
            else{
                rok.setText(rokpom.toString())
            }
        }
        else{
            rok.setText("2020")
            Toast.makeText(this,"Rok spoza zakresu!",Toast.LENGTH_LONG).show()
        }
        wylicziprzedstawterminy()
    }

    fun zmiana(v:View){
        wylicziprzedstawterminy()
    }

    fun ustawienia(v: View){
        val intent= Intent(this,Ustawienia::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun wylicziprzedstawterminy(){
        if(rok.text.toString().toInt()>1899 && rok.text.toString().toInt()<2021) {
            var parm1: String = "N"
            var parm2: String = "Trig1"
            try {
                val filename = "opcje.txt"
                if (FileExists(filename)) {
                    val file = InputStreamReader(openFileInput(filename))
                    val br = BufferedReader(file)

                    var line = br.readLine()
                    val opcje = line.split(',')
                    if (opcje.size == 2) {
                        parm1 = opcje[0]
                        parm2 = opcje[1]
                    }

                    file.close()

                }
            } catch (e: Exception) {
            }

            var alg: Algorytmy = Algorytmy(parm1, parm2)
            var y = rok.text.toString().toInt()
            var m = 1
            var d = 1
            var wynik: Double
            when (alg.typ) {
                "Simple" -> wynik = alg.simple(y, m, d)
                "Conway" -> wynik = alg.conway(y, m, d)
                "Trig1" -> wynik = alg.trig1(y, m, d)
                "Trig2" -> wynik = alg.trig2(y, m, d)
                else -> wynik = alg.trig1(y, m, d)
            }

            var dzien = round(wynik).toInt()

            var pom = dzien
            if(pom<15){
                pom=15-dzien
            }else{
                pom=dzien-15
            }
            var dateList = ArrayList<String>()
            var datepom = LocalDate.of(rok.text.toString().toInt(), 1, 1)
            if (dzien == 15) {
                dateList.add(datepom.toString())
                var period1 = Period.of(0, 0, 30)
                var i = 0
                while (i < 12) {
                    datepom = datepom.plus(period1)
                    i += 1
                    dateList.add(datepom.toString())
                }
            } else {
                var period=Period.of(0, 0, 30)
                var period1 = Period.of(0, 0, pom)
                datepom = datepom.plus(period1)
                var datemax = LocalDate.of(rok.text.toString().toInt() + 1, 1, 1)
                while (datepom < datemax) {
                    dateList.add(datepom.toString())
                    datepom = datepom.plus(period)
                }
            }

            var listView = findViewById<ListView>(R.id.terminy)
            val listItems = arrayOfNulls<String>(dateList.size)
            for (i in 0 until dateList.size) {
                val termin = dateList[i]
                listItems[i] = termin
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
            listView.adapter = adapter
        }
        else{
            Toast.makeText(this,"Rok spoza zakresu!",Toast.LENGTH_LONG).show()
        }
    }

    private fun FileExists(path:String):Boolean{
        val file=baseContext.getFileStreamPath(path)
        return file.exists()
    }

}
