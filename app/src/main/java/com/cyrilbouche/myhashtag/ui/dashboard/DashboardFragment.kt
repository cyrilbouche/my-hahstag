package com.cyrilbouche.myhashtag.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyrilbouche.myhashtag.CarDataViewModel
import com.cyrilbouche.myhashtag.Vehicle
import com.cyrilbouche.myhashtag.databinding.FragmentDashboardBinding
import com.google.android.gms.maps.MapView

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private lateinit var carDataViewModel: CarDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)
        //setContentView(R.layout.activity_maps)
        carDataViewModel = ViewModelProvider(requireActivity())[CarDataViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        //mapView = binding.mapView
        //mapView.onCreate(savedInstanceState)
        //mapView.getMapAsync(this)

         val root: View = binding.root

        var vehicle: Vehicle? = carDataViewModel.data.value

        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        if (vehicle != null) {
            val maintenanceText = binding.maintenanceTextView
            val maintenanceDistance = vehicle.tripMeter2
            val maintenanceTime = vehicle.daysToService
            val maintenanceString = "Next maintenance in $maintenanceTime days"

            maintenanceText.text = maintenanceString
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}