package com.cyrilbouche.myhashtag.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyrilbouche.myhashtag.CarDataViewModel
import com.cyrilbouche.myhashtag.R
import com.cyrilbouche.myhashtag.Vehicle
import com.cyrilbouche.myhashtag.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var carDataViewModel: CarDataViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        carDataViewModel = ViewModelProvider(requireActivity())[CarDataViewModel::class.java]


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var vehicle: Vehicle? = carDataViewModel.data.value

        val activeCarView = binding.activeCar


        val batteryLevel = binding.batteryLevel


        try {
            if (vehicle != null) {
                activeCarView.text = vehicle.nickname

                /*homeViewModel.data = arrayOf(
                    arrayOf("ic_directions_car_24", vehicle.nickname.toString()),
                    //arrayOf("Range", vehicle1.distanceToEmptyOnBatteryOnly.toString()),
                    arrayOf("ic_battery_charging_full_24", vehicle.chargeLevel.toString()+"% remaining","- estimated range "+vehicle.distanceToEmptyOnBatteryOnly.toString()+"km"),
                    arrayOf("ic_directions_car_24","timeToFullyCharge",vehicle.timeToFullyCharge.toString()),
                    arrayOf("ic_directions_car_24","odoMeter",vehicle.odoMeter.toString()),
                    arrayOf("ic_directions_car_24","tripMeter1",vehicle.tripMeter1.toString()),
                    arrayOf("ic_directions_car_24","tripMeter2",vehicle.tripMeter2.toString()),
                    arrayOf("ic_directions_car_24","interiorTemp",vehicle.interiorTemp.toString()),
                    arrayOf("ic_directions_car_24","exteriorTemp",vehicle.exteriorTemp.toString()),
                    arrayOf("ic_directions_car_24","preClimateActive",vehicle.preClimateActive),
                    arrayOf("ic_directions_car_24","steerWhlHeatingSts",vehicle.steerWhlHeatingSts.toString()),
                    arrayOf("ic_directions_car_24","trunkOpenStatus",vehicle.trunkOpenStatus.toString()),
                    arrayOf("ic_directions_car_24","engineHoodOpenStatus",vehicle.engineHoodOpenStatus.toString()),
                    arrayOf("ic_directions_car_24","drvHeatSts",vehicle.drvHeatSts.toString()),
                    arrayOf("ic_directions_car_24","passHeatingSts",vehicle.passHeatingSts.toString()),
                    arrayOf("ic_directions_car_24","avgSpeed",vehicle.avgSpeed.toString()),
                    arrayOf("ic_directions_car_24","chargeLidAcStatus",vehicle.chargeLidAcStatus.toString()),
                    arrayOf("ic_directions_car_24","chargeUAct",vehicle.chargeUAct.toString()),
                    arrayOf("ic_directions_car_24","chargeIAct",vehicle.chargeIAct.toString()),
                    arrayOf("ic_directions_car_24","daysToService",vehicle.daysToService.toString())
                )*/

                /// BATTERY ///

                val batteryLevelString = vehicle.chargeLevel.toString() + "%"
                batteryLevel.text = batteryLevelString

                val iconBatteryLevel = binding.iconBatteryLevel
                when(vehicle.chargeLevel) {
                    0 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_0_bar_24)
                    in 1..14 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_1_bar_24)
                    in 15..29 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_2_bar_24)
                    in 30..44 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_3_bar_24)
                    in 45..59 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_4_bar_24)
                    in 60..74 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_5_bar_24)
                    in 75..89 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_6_bar_24)
                    in 90..100 -> iconBatteryLevel.setImageResource(R.drawable.ic_battery_full_24)
                }

                val chargePower = binding.chargePower
                chargePower.text = vehicle.getChargingPower()

                val remainingTime = binding.remaining100SOC
                remainingTime.text = vehicle.getRemainingTimeForFull()


                /// CLIMATE ///

                val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation)
                val iconFan = binding.iconFan
                iconFan.startAnimation(rotateAnimation)

                val exteriorTemp = binding.exteriorTemperature
                val interiorTemp = binding.interiorTemperature
                val targetTemp = binding.targetTemperature
                //val targetTempString = vehicle.
                exteriorTemp.text = vehicle.getExtTemp("C")
                interiorTemp.text = vehicle.getIntTemp("C")

                //// LOCK ///

                val sliderVerrouillage = binding.sliderVerrouillage
                sliderVerrouillage.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {
                        // Ici, vous pouvez exécuter une action lorsque l'utilisateur arrête de glisser le slider
                        //vibrator.vibrate(100)
                        if (seekBar != null) {
                            if (seekBar.progress < 50){
                                unlockCar()
                            } else {
                                lockCar()
                            }
                        }
                    }
                })

                val longPressButton = binding.longPressButton
                longPressButton.setOnLongClickListener {
                    if (longPressButton.text == "LOCK MY CAR") {
                        lockCar()
                    } else {
                        unlockCar()
                    }

                    true // Retourne true pour indiquer que l'événement a été traité
                }
                longPressButton.setOnClickListener {
                    Toast.makeText(requireContext(), "Press longer for action", Toast.LENGTH_SHORT).show()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        //val tableFavoriteData : TableLayout = binding.favoriteData

        /*val tabData = homeViewModel.data
        if (tabData != null) {
            for (row in tabData) {
                val tableRow = TableRow(requireContext())
                tableRow.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )

                for (text in row) {
                    if (text.length > 1){
                        // si on traite une icône
                        if (text.substring(0,2) == "ic") {
                            val favoriteDataView = ImageView(requireContext())
                            when(text) {
                                "ic_directions_car_24" -> {
                                    favoriteDataView.setImageResource(R.drawable.ic_directions_car_24)
                                    favoriteDataView.setPadding(10, 10, 10, 10)
                                }
                                "ic_battery_charging_full_24" -> {
                                    favoriteDataView.setImageResource(R.drawable.ic_battery_charging_full_24)
                                    favoriteDataView.setPadding(10, 10, 10, 10)
                                }
                            }

                            tableRow.addView(favoriteDataView)
                        }
                        else{
                            val favoriteDataView = TextView(requireContext())
                            favoriteDataView.text = text
                            favoriteDataView.setPadding(10, 10, 10, 10)
                            favoriteDataView.setTextSize(18F)
                            tableRow.addView(favoriteDataView)
                        }
                    }

                }

                tableFavoriteData.addView(tableRow)
            }
        }*/





        // Obtenez une référence à votre ScrollView
        val scrollView: ScrollView = binding.mainScrollView



// Ajoutez le LinearLayout à la ScrollView
       // scrollView.addView(linearLayout)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Méthode à appeler lorsque le bouton est enfoncé longtemps
    fun onLongPressButtonLongClick(view: View) {
        // Code à exécuter lorsqu'un appui long est détecté sur le bouton
        Toast.makeText(requireContext(), "Appui long détecté", Toast.LENGTH_SHORT).show()
    }

    fun unlockCar(){
        val longPressButton = binding.longPressButton
        val sliderVerrouillage = binding.sliderVerrouillage
        longPressButton.text = "LOCK MY CAR"

        sliderVerrouillage.progress = 0
        sliderVerrouillage.thumb = resources.getDrawable(R.drawable.ic_lock_open_45)

        Toast.makeText(requireContext(), "UNLOCKED", Toast.LENGTH_SHORT).show()
    }

    fun lockCar(){
        val longPressButton = binding.longPressButton
        val sliderVerrouillage = binding.sliderVerrouillage
        longPressButton.text = "UNLOCK MY CAR"

        sliderVerrouillage.progress = 100
        sliderVerrouillage.thumb = resources.getDrawable(R.drawable.ic_lock_45)

        Toast.makeText(requireContext(), "LOCKED", Toast.LENGTH_SHORT).show()
    }

}