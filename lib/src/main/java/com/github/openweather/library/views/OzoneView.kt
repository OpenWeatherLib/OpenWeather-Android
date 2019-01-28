package com.github.openweather.library.views

import android.annotation.SuppressLint
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import com.github.openweather.library.extensions.DDMMYYYY
import com.github.openweather.library.extensions.doubleFormat
import com.github.openweather.library.extensions.hhmmss
import com.github.openweather.library.services.openweather.OpenWeatherService
import guepardoapps.lib.openweather.R
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult", "SetTextI18n")
class OzoneView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var valueTextView: TextView? = null
    private var coordinatesTextView: TextView? = null
    private var datetimeTextView: TextView? = null

    private var subscriptions: Array<Disposable?> = arrayOf()

    init {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.lib_ozone, this, true)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        valueTextView = findViewById(R.id.lib_ozone_value)
        coordinatesTextView = findViewById(R.id.lib_ozone_coordinates)
        datetimeTextView = findViewById(R.id.lib_ozone_datetime)

        subscriptions = subscriptions.plus(OpenWeatherService.instance.ozonePublishSubject
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { response ->
                            val ozone = response?.value
                            if (ozone != null) {
                                valueTextView!!.text = ozone.data.doubleFormat(2)
                                coordinatesTextView!!.text = "Lat: ${ozone.coordinates.latitude.doubleFormat(2)}, Lon:${ozone.coordinates.longitude.doubleFormat(2)}"
                                datetimeTextView!!.text = "${ozone.dateTime.DDMMYYYY()} - ${ozone.dateTime.hhmmss()}"
                            }
                        },
                        { }))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        subscriptions.forEach { x -> x?.dispose() }
    }
}