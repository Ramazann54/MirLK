package me.apps.personal_account_npo_mir.view.main.instruments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import me.apps.personalaccountnpomir.R

class TariffDonutView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 22f
        strokeCap = Paint.Cap.BUTT
    }

    private val rect = RectF()

    private var tariff1 = 0f
    private var tariff2 = 0f
    private var tariff3 = 0f
    private var tariff4 = 0f

    private val colors by lazy {
        listOf(
            ContextCompat.getColor(context, R.color.tariff_1),
            ContextCompat.getColor(context, R.color.tariff_2),
            ContextCompat.getColor(context, R.color.tariff_3),
            ContextCompat.getColor(context, R.color.tariff_4)
        )
    }

    fun setTariffs(t1: String, t2: String, t3: String, t4: String) {
        tariff1 = t1.toFloatSafe()
        tariff2 = t2.toFloatSafe()
        tariff3 = t3.toFloatSafe()
        tariff4 = t4.toFloatSafe()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = width.coerceAtMost(height).toFloat()
        val padding = 28f

        rect.set(
            padding,
            padding,
            size - padding,
            size - padding
        )

        val values = listOf(tariff1, tariff2, tariff3, tariff4)
        val sum = values.sum()

        if (sum <= 0f) {
            paint.color = colors[0]
            canvas.drawArc(rect, 0f, 360f, false, paint)
            return
        }

        var startAngle = -90f

        values.forEachIndexed { index, value ->
            if (value > 0f) {
                val sweepAngle = value / sum * 360f
                paint.color = colors[index]
                canvas.drawArc(rect, startAngle, sweepAngle, false, paint)
                startAngle += sweepAngle
            }
        }
    }

    private fun String.toFloatSafe(): Float {
        return this
            .replace(",", ".")
            .trim()
            .toFloatOrNull() ?: 0f
    }
}