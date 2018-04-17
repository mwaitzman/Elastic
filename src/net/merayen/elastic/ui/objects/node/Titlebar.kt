package net.merayen.elastic.ui.objects.node

import net.merayen.elastic.ui.Draw
import net.merayen.elastic.ui.UIObject
import net.merayen.elastic.ui.event.MouseEvent
import net.merayen.elastic.ui.util.Movable

class Titlebar internal constructor() : UIObject() {
	var layoutWidth = 0f
	var title = ""

	var isDragging: Boolean = false
		private set // True when user is dragging the node

	private var moveable: Movable? = null

	override fun onInit() {
		moveable = Movable(this.parent, this, MouseEvent.Button.LEFT)
		moveable!!.setHandler(object : Movable.IMoveable {

			override fun onGrab() {
				isDragging = true
			}

			override fun onMove() {
				(parent as UINode).sendParameter("ui.java.translation.x", parent!!.translation.x)
				(parent as UINode).sendParameter("ui.java.translation.y", parent!!.translation.y)
			}

			override fun onDrop() {
				isDragging = false
			}
		})

		add(TitleBarContextMenu(this))
	}

	override fun onDraw(draw: Draw) {
		draw.setColor(30, 30, 30)
		draw.fillRect(2f, 2f, layoutWidth - 4f, 10f)

		draw.setColor(200, 200, 200)
		draw.setFont("Verdana", 8f)
		draw.text(title, 5f, 9f)

		super.onDraw(draw)
	}

	override fun onEvent(event: net.merayen.elastic.ui.event.UIEvent) {
		moveable!!.handle(event)
	}
}