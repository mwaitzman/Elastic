package net.merayen.elastic.ui.objects.components.dragdrop

import net.merayen.elastic.ui.UIObject
import net.merayen.elastic.ui.objects.top.Top
import net.merayen.elastic.ui.objects.top.mouse.MouseCarryItem
import net.merayen.elastic.ui.util.MouseHandler
import net.merayen.elastic.util.MutablePoint
import kotlin.math.abs

abstract class SourceItem(val source: UIObject) : MouseHandler(source) {
	var tolerance = 0f

	private var active = false
	private var dragging = false
	private var start: MutablePoint? = null

	init {
		val self = this

		setHandler(object : MouseHandler.Handler() {
			override fun onMouseDown(position: MutablePoint?) {
				active = true
				if(tolerance == 0f)
					startDrag()
			}

			override fun onGlobalMouseUp(position: MutablePoint?) {
				active = false
				if (dragging) {
					dragging = false
					mouseCursorManager().removeCarryItem(mouseEvent.id)
					onDrop()
				}
			}

			override fun onGlobalMouseMove(global_position: MutablePoint?) {
				if (!active)
					return

				var start = start
				var globalPosition = global_position!!

				if (start == null)
					self.start = global_position
				else if (!dragging && (abs(globalPosition.x - start.x) > tolerance || abs(globalPosition.y - start.y) > tolerance))
					startDrag()
			}
		})
	}

	abstract fun onGrab(): MouseCarryItem
	abstract fun onDrop()

	private fun startDrag() {
		dragging = true
		mouseCursorManager().setCarryItem(mouseEvent.id, onGrab())
	}

	private fun mouseCursorManager() = (source.search.top as Top).mouseCursorManager
}