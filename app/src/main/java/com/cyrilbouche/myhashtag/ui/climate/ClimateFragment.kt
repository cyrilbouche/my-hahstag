package com.cyrilbouche.myhashtag.ui.climate

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyrilbouche.myhashtag.R
import com.cyrilbouche.myhashtag.databinding.FragmentClimateBinding

class ClimateFragment : Fragment() {

    private var _binding: FragmentClimateBinding? = null
    //private var activeButtonId: Int? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var climateViewModel: ClimateViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        climateViewModel = ViewModelProvider(requireActivity()).get(ClimateViewModel::class.java)

        _binding = FragmentClimateBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        // A supprimer et remplacer par lecture du json
        val interiorTemperature = 23

        val gauge = binding.temperatureSeekBar


        // Observer pour observer les changements dans activePreset
        climateViewModel.activePreset.observe(viewLifecycleOwner) { activePreset ->
            // Mettre à jour l'icône Eco en fonction de la valeur de activePreset
            val ecoButton = binding.button4.id
            if (activePreset == ecoButton) {
                displayEcoIcon("o")
            } else {
                displayEcoIcon("n")
            }

            // Mettre à jour l'icône AC en fonction des valeurs actuelles
            updateAcIcon(binding.temperatureSeekBar.progress, interiorTemperature)
            // Mettre à jour l'apparence des boutons en fonction de activePreset
            updateButtonAppearance()
        }


        val textView: TextView = binding.temperatureTextView
        textView.text = "$interiorTemperature °C"

        // Définir un écouteur de changement de progression pour votre SeekBar
        gauge.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(gauge: SeekBar?, progress: Int, fromUser: Boolean) {
                // La valeur actuelle de la SeekBar est dans le paramètre 'progress'
                // Vous pouvez faire ce que vous voulez avec cette valeur ici
                val value = progress
                // Par exemple, affichez la valeur dans un TextView
                textView.text = "$value °C"

                updateAcIcon(value, interiorTemperature)
            }

            override fun onStartTrackingTouch(gauge: SeekBar?) {
                // Cette méthode est appelée lorsque l'utilisateur commence à déplacer la barre de progression
                climateViewModel.updateActivePreset(0)
                updateButtonAppearance()
                displayEcoIcon("n")
            }

            override fun onStopTrackingTouch(gauge: SeekBar?) {
                // Cette méthode est appelée lorsque l'utilisateur arrête de déplacer la barre de progression
            }
        })

        val ecoButton: Button = binding.button4
        val comfortButton: Button = binding.button2
        val comfortPlusButton: Button = binding.button3

        ecoButton.setOnClickListener {
            updateActiveButton(ecoButton.id)
            updateTemperature(19)
            displayEcoIcon("o")
        }

        comfortButton.setOnClickListener {
            updateActiveButton(comfortButton.id)
            updateTemperature(21)
            displayEcoIcon("n")
        }

        comfortPlusButton.setOnClickListener {
            updateActiveButton(comfortPlusButton.id)
            updateTemperature(23)
            displayEcoIcon("n")
        }

        val rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotation_animation)
        val iconFan = binding.iconFan
        iconFan.startAnimation(rotateAnimation)


        val climateSwitch = binding.preClimate
        climateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            val iconsBar = binding.iconsBar
            val thermostat = binding.thermostat
            val presets = binding.presets
            if (isChecked) {
                // Le switch est activé
                // Mettez votre code ici pour gérer l'action lorsque le switch est activé
                iconsBar.visibility = View.VISIBLE
                thermostat.visibility = View.VISIBLE
                presets.visibility = View.VISIBLE
            } else {
                // Disable icons bar and temperature controls
                iconsBar.visibility = View.GONE
                thermostat.visibility = View.GONE
                presets.visibility = View.GONE

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateActiveButton(buttonId: Int) {
        climateViewModel.updateActivePreset(buttonId)
        updateButtonAppearance()
    }

    private fun updateButtonAppearance() {
        val ecoButton: Button = view?.findViewById(R.id.button4) ?: return
        val comfortButton: Button = view?.findViewById(R.id.button2) ?: return
        val comfortPlusButton: Button = view?.findViewById(R.id.button3) ?: return
        val activeButtonId = climateViewModel.activePreset.value

        ecoButton.setTextColor(getButtonTextColor(ecoButton.id))
        comfortButton.setTextColor(getButtonTextColor(comfortButton.id))
        comfortPlusButton.setTextColor(getButtonTextColor(comfortPlusButton.id))
        updateButtonBackground(ecoButton, activeButtonId == ecoButton.id)
        updateButtonBackground(comfortButton, activeButtonId == comfortButton.id)
        updateButtonBackground(comfortPlusButton, activeButtonId == comfortPlusButton.id)
    }

    private fun updateButtonBackground(button: Button, isActive: Boolean) {
        val colorId = if (isActive) R.color.yellowish else R.color.black
        button.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), colorId))
    }


    private fun getButtonTextColor(buttonId: Int): Int {
        return if (buttonId == climateViewModel.activePreset.value) {
            // Couleur pour le bouton actif
            ContextCompat.getColor(requireContext(), R.color.black)

        } else {
            // Couleur pour les autres boutons
            ContextCompat.getColor(requireContext(), R.color.white)
        }
    }

    /*private fun getActiveButton() : Button? {
        val ecoButton: Button = binding.button4
        val comfortButton: Button = binding.button2
        val comfortPlusButton: Button = binding.button3
        var activeButton : Button? = null

        when (activeButtonId){
            ecoButton.id -> activeButton = ecoButton
            comfortButton.id -> activeButton = comfortButton
            comfortPlusButton.id -> activeButton = comfortPlusButton
        }

        return activeButton
    }*/

    private fun updateTemperature(temperature: Int){
        val temperatureTextView = binding.temperatureTextView
        val temperatureSeekBar = binding.temperatureSeekBar

        temperatureTextView.setText("$temperature °C")
        temperatureSeekBar.progress = temperature
    }

    private fun updateAcIcon(targetTemperature: Int, interiorTemperature: Int){
        val iconAC = binding.iconAc
        if (targetTemperature >= interiorTemperature){
            iconAC.setImageResource(R.drawable.ic_ac_heat_24)
        } else {
            iconAC.setImageResource(R.drawable.ic_ac_unit_24)
        }
    }

    private fun displayEcoIcon(display: String){
        val iconEco = binding.iconEco
        if (display == "o"){
            iconEco.visibility = View.VISIBLE
        } else {
            iconEco.visibility = View.GONE
        }
    }
}