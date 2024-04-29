package com.cyrilbouche.myhashtag.ui.charge

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import com.cyrilbouche.myhashtag.CarDataViewModel
import com.cyrilbouche.myhashtag.PulseAnimation
import com.cyrilbouche.myhashtag.Vehicle
import com.cyrilbouche.myhashtag.databinding.FragmentChargeBinding

class ChargeFragment : Fragment() {

    /*companion object {
        fun newInstance() = ChargeFragment()
    }

    private lateinit var viewModel: ChargeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charge, container, false)
    }*/

    private var _binding: FragmentChargeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var carDataViewModel: CarDataViewModel

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val chargeViewModel =
            ViewModelProvider(this).get(ChargeViewModel::class.java)

        carDataViewModel = ViewModelProvider(requireActivity()).get(CarDataViewModel::class.java)

        _binding = FragmentChargeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textDashboard
        chargeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        val vehicle: Vehicle? = carDataViewModel.data.value

        val batteryLevel: TextView = binding.batteryLevel
        val batteryLevelString = vehicle?.chargeLevel.toString() + "%"
        batteryLevel.text = batteryLevelString

        val progressBar = binding.batteryChargeProgressBar
        if (vehicle != null) {
            progressBar.progress = vehicle.chargeLevel
        } else {
            progressBar.progress = 0
        }

        val chargingIcon = binding.chargingIcon
        val pulseAnimation = PulseAnimation(chargingIcon)
        if (vehicle?.chargeUAct != null) {
            if (vehicle.chargeUAct > 0) {
                pulseAnimation.start()
            } else {
                pulseAnimation.stop()
            }
        }

        /// Charge limit ///

        val chargeLimit = binding.chargeLimit
        val maxChargeSeekbar = binding.maxChargeSeekBar
        maxChargeSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val chargeLimitString = "Charge limit : " + maxChargeSeekbar.progress.toString() + "%"
                chargeLimit.text = chargeLimitString
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChargeViewModel::class.java)
        // TODO: Use the ViewModel
    }*/

}