package com.example.fazyksiezyca

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ustawienia.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.time.LocalDate
import java.time.Period
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ustawElementy()
    }

    fun ustawienia(v: View) {
        val intent = Intent(this, Ustawienia::class.java)
        startActivity(intent)
    }

    fun pelnie(v: View) {
        val intent = Intent(this, TerminyPelni::class.java)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        ustawElementy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun ustawElementy() {
        var parm1:String="N"
        var parm2:String="Trig1"
        try{
            val filename="opcje.txt"
            if (FileExists(filename)){
                val file= InputStreamReader(openFileInput(filename))
                val br= BufferedReader(file)

                var line=br.readLine()
                val opcje=line.split(',')
                if(opcje.size==2){
                    parm1=opcje[0]
                    parm2=opcje[1]
                }

                file.close()

            }
            else{
                Toast.makeText(this, "Nie znaleziono pliku", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, "Korzystasz z domyślnych ustawień, ale możesz je zmienić", Toast.LENGTH_LONG).show()
        }

        var alg: Algorytmy = Algorytmy(parm1,parm2)
        var data = LocalDate.now()
        val y = data.year
        val m = data.monthValue
        val d = data.dayOfMonth

        //wyliczenie aktualnej fazy
        var wynik: Double
        when (alg.typ) {
            "Simple" -> wynik = alg.simple(y, m, d)
            "Conway" -> wynik = alg.conway(y, m, d)
            "Trig1" -> wynik = alg.trig1(y, m, d)
            "Trig2" -> wynik = alg.trig2(y, m, d)
            else -> wynik = alg.trig1(y, m, d)
        }
        var dzien = round(wynik)
        wynik = (wynik / 30) * 100
        wynik = round(wynik)
        procenty.setText("Dzisiaj: $wynik%")

        //wybór obrazka
        var obrazek: String
        when (alg.polkula) {
            "N" -> obrazek = "n"
            "S" -> obrazek = "s"
            else -> obrazek = "n"
        }

        if (obrazek.equals("n")) {
            if (wynik < 8.0) {
                obrazek = obrazek + "01p"
            } else if (wynik < 18.0) {
                obrazek = obrazek + "10p"
            } else if (wynik < 25.0) {
                obrazek = obrazek + "21pm"
            } else if (wynik < 34.0) {
                obrazek = obrazek + "29pm"
            } else if (wynik < 44.0) {
                obrazek = obrazek + "38pm"
            } else if (wynik < 53.0) {
                obrazek = obrazek + "48p"
            } else if (wynik < 61.0) {
                obrazek = obrazek + "57p"
            } else if (wynik < 70.0) {
                obrazek = obrazek + "66pm"
            } else if (wynik < 79.0) {
                obrazek = obrazek + "75pm"
            } else if (wynik < 90.0) {
                obrazek = obrazek + "83pm"
            } else if (wynik < 98.0) {
                obrazek = obrazek + "95pm"
            } else {
                obrazek = obrazek + "99p"
            }
        } else {
            if (wynik < 2.0) {
                obrazek = obrazek + "01p"
            } else if (wynik < 10.0) {
                obrazek = obrazek + "03p"
            } else if (wynik < 19.0) {
                obrazek = obrazek + "15p"
            } else if (wynik < 30.0) {
                obrazek = obrazek + "25p"
            } else if (wynik < 41.0) {
                obrazek = obrazek + "36p"
            } else if (wynik < 52.0) {
                obrazek = obrazek + "47p"
            } else if (wynik < 62.0) {
                obrazek = obrazek + "58p"
            } else if (wynik < 71.0) {
                obrazek = obrazek + "69p"
            } else if (wynik < 81.0) {
                obrazek = obrazek + "78p"
            } else if (wynik < 92.0) {
                obrazek = obrazek + "86p"
            } else if (wynik < 98.0) {
                obrazek = obrazek + "97p"
            } else {
                obrazek = obrazek + "99pm"
            }

        }
        val pom = this.getResources().getIdentifier(obrazek, "drawable", this.getPackageName())
        zdjecie.setImageDrawable(getDrawable(pom))

        //ustawienie napisów o dacie nowiu i pełni
        if (dzien > 0) {
            var datadzis = LocalDate.now()
            var period1 = Period.of(0, 0, dzien.toInt())
            val datapoprzedniegonowiu = datadzis.minus(period1)
            if(datapoprzedniegonowiu==datadzis){
                now.setText("Dziś jest nów")
            }else {
                now.setText("Poprzedni nów: $datapoprzedniegonowiu")
            }

            var period2 = Period.of(0, 0, 15)
            val datanastepnejpelni = datapoprzedniegonowiu.plus(period2)
            if(datanastepnejpelni<datadzis){
                pelnia.setText("Poprzednia pełnia: $datanastepnejpelni")
            }else if(datanastepnejpelni==datadzis){
                pelnia.setText("Dziś jest pełnia")
            }
            else {
                pelnia.setText("Następna pełnia: $datanastepnejpelni")
            }
        }

    }

    private fun FileExists(path:String):Boolean{
        val file=baseContext.getFileStreamPath(path)
        return file.exists()
    }
}
