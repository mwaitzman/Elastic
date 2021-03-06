package net.merayen.elastic.ui.objects.components

import net.merayen.elastic.ui.Draw
import net.merayen.elastic.ui.UIObject

class Label(var text: String = "", var eventTransparent: Boolean = true) : UIObject() {
	var fontSize = 10f
	var font_name = "Geneva"
	var align: Align? = null
	var labelWidth: Float = 0f
		private set

	enum class Align {
		LEFT, CENTER, RIGHT
	}

	override fun onDraw(draw: Draw) {
		if((absoluteTranslation?.scaleX ?: 0f) > fontSize / 7f) // Hide text if it is too small
			return

		if(eventTransparent)
			draw.disableOutline()

		draw.setFont(font_name, fontSize)
		labelWidth = draw.getTextWidth(text)

		var x_offset = 0f
		if (align == Align.CENTER)
			x_offset = -labelWidth / 2f
		else if (align == Align.RIGHT)
			x_offset = -labelWidth

		draw.setColor(50, 50, 50)
		draw.text(text, x_offset - fontSize / 10f, fontSize - fontSize / 10f)
		draw.setColor(200, 200, 200)
		draw.text(text, x_offset, fontSize)
	}

	override fun getWidth() = labelWidth
	override fun getHeight() = fontSize
}
