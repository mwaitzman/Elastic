package net.merayen.elastic.ui.objects.top.views.nodeview

import net.merayen.elastic.system.intercom.backend.ImportFileIntoNodeGroupMessage
import net.merayen.elastic.ui.Draw
import net.merayen.elastic.ui.UIObject
import net.merayen.elastic.ui.event.FileDropEvent
import net.merayen.elastic.ui.event.UIEvent
import net.merayen.elastic.util.Pacer

class NodeViewContainer(val nodeView: NodeView) : UIObject() {
	var zoomTranslateXTarget = 0f
	var zoomTranslateYTarget = 0f
	var zoomScaleXTarget = 1f
	var zoomScaleYTarget = 1f

	private val pacer = Pacer()

	override fun onDraw(draw: Draw) {
		draw.empty(-10000000f, -10000000f, 10000000000f, 10000000000f)
	}

	override fun onEvent(event: UIEvent) {
		if (event is FileDropEvent) {
			val nodeId = nodeView.currentNodeId

			if (nodeId != null) {
				val position = getRelativeFromAbsolute(event.x.toFloat(), event.y.toFloat())
				sendMessage(ImportFileIntoNodeGroupMessage(event.files, nodeId, position.x, position.y))
			}
		}
	}

	override fun onUpdate() {
		pacer.update()

		val diff = pacer.getDiff(20f)

		translation.x += (zoomTranslateXTarget - translation.x) * diff
		translation.y += (zoomTranslateYTarget - translation.y) * diff
		translation.scaleX += (zoomScaleXTarget - translation.scaleX) * diff
		translation.scaleY += (zoomScaleYTarget - translation.scaleY) * diff
	}
}
