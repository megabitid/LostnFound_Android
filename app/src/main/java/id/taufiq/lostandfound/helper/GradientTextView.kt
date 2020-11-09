package id.taufiq.lostandfound.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import id.taufiq.lostandfound.R

/**
 * Created By Taufiq on 11/9/2020.
 *
 */
class GradientTextView : androidx.appcompat.widget.AppCompatTextView {

    var startColor = 0
    var endColor = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context, attributeSet, defStyleAttr
    )

    fun setColorGradient(startColor: Int, endColor: Int) {
        this.startColor = startColor
        this.endColor = endColor
    }


    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (changed) {
            val startColor = ContextCompat.getColor(context, R.color.orange_1)
            val endColor = ContextCompat.getColor(context, R.color.orange_2)
            paint.shader = LinearGradient(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                endColor,
                startColor,
                Shader.TileMode.CLAMP
            )
        }
    }
}