package com.cyrilbouche.myhashtag
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView

class PulseAnimation(private val icon: ImageView) {

    private val duration = 1000L // Durée de l'animation de pulsation
    private lateinit var animationSet: AnimationSet

    fun start() {
        // Créer une animation de mise à l'échelle pour agrandir et rétrécir l'icône
        val scaleAnimation = ScaleAnimation(
            1f, 1.2f, 1f, 1.2f, // Facteur de mise à l'échelle pour agrandir l'icône
            Animation.RELATIVE_TO_SELF, 0.5f, // Point de pivot X pour l'agrandissement (centre de l'icône)
            Animation.RELATIVE_TO_SELF, 0.5f // Point de pivot Y pour l'agrandissement (centre de l'icône)
        )
        scaleAnimation.repeatCount = Animation.INFINITE // Répéter l'animation indéfiniment
        scaleAnimation.repeatMode = Animation.REVERSE // Inverser l'animation lorsqu'elle se répète
        scaleAnimation.duration = duration

        // Créer une animation d'opacité pour ajuster l'opacité de l'icône
        val alphaAnimation = AlphaAnimation(1f, 0.5f)
        alphaAnimation.repeatCount = Animation.INFINITE
        alphaAnimation.repeatMode = Animation.REVERSE
        alphaAnimation.duration = duration

        // Combinez les deux animations
        animationSet = AnimationSet(true)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(alphaAnimation)

        // Appliquer l'animation à l'icône
        icon.startAnimation(animationSet)
    }

    fun stop() {
        // Arrêter l'animation en retirant toutes les animations
        animationSet.cancel()
        icon.clearAnimation()
    }
}
