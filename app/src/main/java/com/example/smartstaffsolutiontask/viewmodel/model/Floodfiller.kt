package com.example.smartstaffsolutiontask.viewmodel.model

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Parcel
import android.util.Base64
import android.util.Base64.DEFAULT
import androidx.core.content.edit
import com.example.smartstaffsolutiontask.algorithms.AlgorithmsDescription

class Floodfiller(private val context: Context): FloodfillerInterface {

    companion object {
        private const val NAME = "FLOODFILLERS"
        private const val FILLERS_ARG = "FLODFILLERS"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    override fun saveFloodFillers(list: List<AlgorithmsDescription>) {
        val stringParcelSet = list.map {
            val parcel = Parcel.obtain()
            parcel.setDataPosition(0)
            it.writeToParcel(parcel)
            val string = Base64.encodeToString(parcel.marshall(), DEFAULT)
            parcel.recycle()
            string
        }.toSet()

        sharedPreferences.edit {
            this.putStringSet(FILLERS_ARG, stringParcelSet)
        }
    }

    override fun getFloodFillers(image: Bitmap): List<AlgorithmsDescription> {
        return sharedPreferences.getStringSet(FILLERS_ARG, emptySet())!!
                .map { Base64.decode(it, DEFAULT) }
                .map {
                    val parcel = Parcel.obtain()
                    parcel.setDataPosition(0)
                    parcel.unmarshall(it, 0, it.size)
                    parcel.setDataPosition(0)
                    val floodfiller = AlgorithmsDescription.create(image, parcel)
                    parcel.recycle()
                    floodfiller
                }.toMutableList()
    }
}