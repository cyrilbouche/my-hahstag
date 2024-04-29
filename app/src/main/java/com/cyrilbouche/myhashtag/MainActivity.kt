package com.cyrilbouche.myhashtag

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cyrilbouche.myhashtag.databinding.ActivityMainBinding
import com.cyrilbouche.myhashtag.ui.climate.ClimateViewModel
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // Déclarer le ViewModel partagé
    private lateinit var climateViewModel: ClimateViewModel
    private val carDataViewModel: CarDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Getting the car data

        val carDataObj = getCarData()
        val vehicleStatusObj = carDataObj.getJSONObject("vehicleStatus")
        val vehicle1 = createVehicle(vehicleStatusObj)

        carDataViewModel.setCarData(vehicle1)

         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_climate, R.id.navigation_charge
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    // Charger les donénes du fichier JSON
    fun getCarData() : JSONObject {
        // Charger le fichier JSON
        var jsonStr = loadJSONFromAsset(this, "GetCar.json")
        var carDataObj = JSONObject()

        // Remplacer les caractères de retour chariot et de nouvelle ligne
        jsonStr = jsonStr?.replace("\r\n", "")
        jsonStr = jsonStr?.replace(" ", "")

        // Traitement des données JSON
        jsonStr?.let {
            // Convertir la chaîne JSON en objet JSON
            //val jsonObj = JSONObject(it)
            try {
                // Convertir la chaîne JSON en objet JSON
                val jsonObj = JSONObject(jsonStr)

                // Accéder à l'objet "data"
                carDataObj = jsonObj.getJSONObject("data")

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return carDataObj
    }

    // Fonction pour lire le fichier JSON depuis le dossier assets
    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        var json: String? = null
        try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return json
    }

    // Create a vehicle object from JSON
    fun createVehicle(carData: JSONObject): Vehicle {


        val additionalStatus = carData.getJSONObject("additionalVehicleStatus")
        val maintenanceStatus = additionalStatus.getJSONObject("maintenanceStatus")
        val drivingSafetyStatus = additionalStatus.getJSONObject("drivingSafetyStatus")
        val climateStatus = additionalStatus.getJSONObject("climateStatus")
        val elecStatus = additionalStatus.getJSONObject("electricVehicleStatus")
        val runningStatus = additionalStatus.getJSONObject("runningStatus")

        val nickname = "Smart de test"
        val distanceToEmptyOnBatteryOnly = elecStatus.getInt("distanceToEmptyOnBatteryOnly")
        val distanceToEmptyOnBattery100Soc = elecStatus.getInt("distanceToEmptyOnBattery100Soc")
        val chargeLevel = elecStatus.getInt("chargeLevel")
        val timeToFullyCharge = elecStatus.getInt("timeToFullyCharged")
        val odoMeterString = maintenanceStatus.optString("odoMeter")
        val odoMeter = odoMeterString?.toDoubleOrNull() ?: 0.0

        //val odoMeterString = maintenanceStatus.getString("odoMeter")
        //val odoMeter = odoMeterString.toDoubleOrNull() ?: 0.0
        val tripMeter1String = runningStatus.getString("tripMeter1")
        val tripMeter1 = tripMeter1String.toDoubleOrNull() ?: 0.0
        val tripMeter2String = runningStatus.getString("tripMeter2")
        val tripMeter2 = tripMeter2String.toDoubleOrNull() ?: 0.0
        val interiorTempString = climateStatus.getString("interiorTemp")
        val interiorTemp = interiorTempString.toDoubleOrNull() ?: 0.0
        val exteriorTempString = climateStatus.getString("exteriorTemp")
        val exteriorTemp = exteriorTempString.toDoubleOrNull() ?: 0.0
        val preClimateActive = climateStatus.getString("preClimateActive")
        val steerWhlHeatingSts = climateStatus.getInt("steerWhlHeatingSts")
        val trunkOpenStatus = drivingSafetyStatus.getInt("trunkOpenStatus")
        val engineHoodOpenStatus = drivingSafetyStatus.getInt("engineHoodOpenStatus")
        val drvHeatSts = climateStatus.getInt("drvHeatSts")
        val passHeatingSts = climateStatus.getInt("passHeatingSts")
        val avgSpeed = runningStatus.getInt("avgSpeed")
        val chargeLidAcStatus = elecStatus.getInt("chargeLidAcStatus")
        val chargeUActString = elecStatus.getString("chargeUAct")
        val chargeUAct = chargeUActString.toDoubleOrNull() ?: 0.0
        val chargeIActString = elecStatus.getString("chargeIAct")
        val chargeIAct = chargeIActString.toDoubleOrNull() ?: 0.0
        val daysToService = maintenanceStatus.getInt("daysToService")

        val vehicle = Vehicle(nickname,distanceToEmptyOnBatteryOnly,distanceToEmptyOnBattery100Soc,chargeLevel,timeToFullyCharge,
            odoMeter,
            tripMeter1,
            tripMeter2,
            interiorTemp,
            exteriorTemp,
            preClimateActive,
            steerWhlHeatingSts,
            trunkOpenStatus,
            engineHoodOpenStatus,
            drvHeatSts,
            passHeatingSts,
            avgSpeed,
            chargeLidAcStatus,
            chargeUAct,
            chargeIAct,
            daysToService)

        return vehicle
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                // Action à effectuer lorsque l'élément de menu est sélectionné
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}