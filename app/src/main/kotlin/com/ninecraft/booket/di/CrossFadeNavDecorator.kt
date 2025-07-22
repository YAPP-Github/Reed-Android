package com.ninecraft.booket.di

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import com.slack.circuit.backstack.NavArgument
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.foundation.animation.AnimatedNavDecorator
import com.slack.circuit.foundation.animation.AnimatedNavEvent
import com.slack.circuit.foundation.animation.AnimatedNavState

data class CrossFadeNavDecoratorFactory(val durationMillis: Int = 300) :
    AnimatedNavDecorator.Factory {
    override fun <T : NavArgument> create(): AnimatedNavDecorator<T, *> =
        CrossFadeNavDecorator(durationMillis)
}

class CrossFadeNavDecorator<T : NavArgument>(private val durationMillis: Int) :
    AnimatedNavDecorator<T, NavigatorDefaults.DefaultDecorator.DefaultAnimatedState<T>> by NavigatorDefaults.DefaultDecorator<T>() {

    override fun AnimatedContentTransitionScope<AnimatedNavState>.transitionSpec(
        animatedNavEvent: AnimatedNavEvent,
    ): ContentTransform {
        return fadeIn(tween(durationMillis)) togetherWith fadeOut(tween(durationMillis))
    }
}
