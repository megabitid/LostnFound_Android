package id.taufiq.lostandfound.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.taufiq.lostandfound.R

/**
 * Created By Taufiq on 11/9/2020.
 *
 */
@SuppressLint("AppCompatCustomView")
class GradientTextView : TextView {


    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context, attributeSet, defStyleAttr
    )



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