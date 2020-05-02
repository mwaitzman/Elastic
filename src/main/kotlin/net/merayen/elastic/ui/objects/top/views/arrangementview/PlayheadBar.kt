package net.merayen.elastic.ui.objects.top.views.arrangementview

import net.merayen.elastic.ui.Draw
import net.merayen.elastic.ui.FlexibleDimension
import net.merayen.elastic.ui.UIObject

class PlayheadBar : UIObject(), FlexibleDimension {
	override var layoutWidth = 100f
	override var layoutHeight = 20f

	var beatWidth = 10f
	var division = 4

	override fun onDraw(draw: Draw) { // TODO only draw what is visible in the clip
		draw.setColor(0.2f, 0.2f, 0.2f)
		draw.fillRect(0f, 0f, layoutWidth, layoutHeight)
	}
}