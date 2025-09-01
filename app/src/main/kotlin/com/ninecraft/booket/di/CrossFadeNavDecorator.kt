package com.ninecraft.booket.di

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import com.ninecraft.booket.feature.screens.LoginScreen
import com.ninecraft.booket.feature.screens.SplashScreen
import com.ninecraft.booket.feature.screens.TermsAgreementScreen
import com.slack.circuit.backstack.NavArgument
import com.slack.circuit.foundation.NavigatorDefaults
import com.slack.circuit.foundation.animation.AnimatedNavDecorator
import com.slack.circuit.foundation.animation.AnimatedNavEvent
import com.slack.circuit.foundation.animation.AnimatedNavState

data class CrossFadeNavDecoratorFactory(val durationMillis: Int = 500) :
    AnimatedNavDecorator.Factory {
    override fun <T : NavArgument> create(): AnimatedNavDecorator<T, *> =
        CrossFadeNavDecorator(durationMillis)
}

class CrossFadeNavDecorator<T : NavArgument>(private val durationMillis: Int) :
    AnimatedNavDecorator<T, NavigatorDefaults.DefaultDecorator.DefaultAnimatedState<T>> by NavigatorDefaults.DefaultDecorator<T>() {

    override fun AnimatedContentTransitionScope<AnimatedNavState>.transitionSpec(
        animatedNavEvent: AnimatedNavEvent,
    ): ContentTransform {
        val shouldUseFade = shouldUseFadeAnimation(initialState, targetState)

        return if (shouldUseFade) {
            fadeIn(tween(durationMillis)) togetherWith fadeOut(tween(durationMillis))
        } else {
            // Circuit 기본 애니메이션 사용
            with(NavigatorDefaults.DefaultDecorator<T>()) {
                transitionSpec(animatedNavEvent)
            }
        }
    }

    private fun shouldUseFadeAnimation(
        initialState: AnimatedNavState,
        targetState: AnimatedNavState,
    ): Boolean {
        val fadeScreens = setOf(
            SplashScreen::class,
            LoginScreen::class,
            TermsAgreementScreen::class,
        )

        val initialScreenClass = initialState.top.screen::class
        val targetScreenClass = targetState.top.screen::class

        // 앱 시작시 SplashScreen이 첫 화면인 경우 처리
        val isAppLaunchToSplash = targetScreenClass == SplashScreen::class

        return isAppLaunchToSplash || initialScreenClass in fadeScreens || targetScreenClass in fadeScreens
    }
}
