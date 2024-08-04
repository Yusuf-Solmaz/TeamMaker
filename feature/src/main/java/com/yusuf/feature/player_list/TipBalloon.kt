package com.yusuf.feature.player_list

import android.content.Context
import android.view.View
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation

fun showTooltipBalloon(context: Context, anchorView: View, text: String, onDismiss: () -> Unit) {
    val balloon = Balloon.Builder(context)
        .setArrowSize(10)
        .setWidthRatio(1.0f)
        .setHeight(65)
        .setTextSize(15f)
        .setCornerRadius(4f)
        .setAlpha(0.9f)
        .setText(text)
        .setDismissWhenClicked(true)
        .setDismissWhenTouchOutside(true)
        .setBalloonAnimation(BalloonAnimation.ELASTIC)
        .build()

    balloon.setOnBalloonDismissListener {
        onDismiss()
    }

    balloon.showAlignTop(anchorView)
}
