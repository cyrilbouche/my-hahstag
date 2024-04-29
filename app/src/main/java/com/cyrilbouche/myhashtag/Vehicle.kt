package com.cyrilbouche.myhashtag

import java.text.DecimalFormat
import kotlin.math.sqrt

class Vehicle(
    val nickname: String ="Smart de test",
    val distanceToEmptyOnBatteryOnly: Int,
    val distanceToEmptyOnBattery100Soc: Int,
    val chargeLevel: Int,
    val timeToFullyCharge: Int,
    val odoMeter: Double,
    val tripMeter1: Double,
    val tripMeter2: Double,
    val interiorTemp: Double,
    val exteriorTemp: Double,
    val preClimateActive: String,
    val steerWhlHeatingSts: Int,
    val trunkOpenStatus: Int,
    val engineHoodOpenStatus: Int,
    val drvHeatSts: Int,
    val passHeatingSts: Int,
    val avgSpeed: Int,
    val chargeLidAcStatus: Int,
    val chargeUAct: Double,
    val chargeIAct: Double,
    val daysToService: Int
) {
    fun getChargingPower() : String {
        val calcChgPwr = chargeUAct * chargeIAct * sqrt(3.0) / 1000
        val df = DecimalFormat("#.##")
        df.maximumFractionDigits = 2
        val nombreArrondi = df.format(calcChgPwr)
        val chargePowerString = nombreArrondi.toString() + " kW"
        return chargePowerString
    }

    fun getRemainingTimeForFull() : String {
        val remainingTimeString = timeToFullyCharge.toString() + " min"
        return remainingTimeString
    }

    fun getExtTemp(unit: String) : String{
        val exteriorTempString = if (unit == "F"){
            val farenheit = exteriorTemp * (9/5) + 32
            "Exterior : " + farenheit.toString() + "°F"
        } else {
            "Exterior : " + exteriorTemp.toString() + "°C"
        }
        return exteriorTempString
    }

    fun getIntTemp(unit: String) : String{
        val interiorTempString = if (unit == "F"){
            val farenheit = interiorTemp * (9/5) + 32
            "Interior : " + farenheit.toString() + "°F"
        } else {
            "Interior : " + interiorTemp.toString() + "°C"
        }
        return interiorTempString
    }

}