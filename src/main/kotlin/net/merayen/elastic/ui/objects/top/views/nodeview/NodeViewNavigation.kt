package net.merayen.elastic.ui.objects.top.views.nodeview


import net.merayen.elastic.ui.Draw
import net.merayen.elastic.ui.UIObject
import net.merayen.elastic.ui.objects.node.UINode
import net.merayen.elastic.ui.objects.node.UIPort
import net.merayen.elastic.ui.objects.node.UIPortTemporary
import net.merayen.elastic.ui.util.ArrowNavigation

/**
 * Add-on for NodeView, giving feature for navigating the nodes by using arrow keys.
 * Navigation moves over UINode and UIPort in NodeView.
 *
 *                                       |-------------|
 *                                       | Node B      |
 *                                       |             |
 *                                       |             |
 *    ---------------                    |             |      |-------------|
 *    | Node A      |(1)_____ (2)_____(3)|             |   (4)| Node C      |
 *    |             | |     \  |         |-------------|    | |             |
 *    |             |(1)__…  \(2)__________________________(4)|             |
 *    |-------------|                                         |-------------|
 */
class NodeViewNavigation(private val nodeView: NodeView) : UIObject() {
	class Line(val portA: UIPort, val portB: UIPort)

	val current
		get() = pointMap[arrowNavigation.current]

	private val arrowNavigation = ArrowNavigation()

	private val pointMap = HashMap<ArrowNavigation.Point, Any>()

	private var dirty = true

	private var nodeViewRevision = -1
	private var uiNetRevision = -1

	override fun onUpdate() {
		if (nodeView.revision == nodeViewRevision && nodeView.uiNet.revision == uiNetRevision)
			return

		rebuild()

		nodeViewRevision = nodeView.revision
		uiNetRevision = nodeView.uiNet.revision
	}

	override fun onDraw(draw: Draw) {
		val obj = current ?: return

		draw.disableOutline()

		when (obj) {
			is UINode -> {
				val pos = getRelativePosition(obj) ?: return
				draw.setColor(1f, 1f, 0.5f)
				draw.setStroke(2f)
				draw.rect(pos.x, pos.y, 50f, 50f)
			}
			is UIPort -> {
				val pos = getRelativePosition(obj) ?: return
				draw.setColor(1f, 1f, 0.5f)
				draw.setStroke(2f)
				draw.rect(pos.x - 5, pos.y - 5, 10f, 10f)
			}
			is Line -> {
				val posA = getRelativePosition(obj.portA) ?: return
				val posB = getRelativePosition(obj.portB) ?: return
				draw.setColor(1f, 1f, 0.5f)
				draw.setStroke(5f)
				draw.line(posA.x, posA.y, posB.x, posB.y)
			}
			else -> throw RuntimeException("Should not happen")
		}
	}

	/**
	 * Every time something has been changed, remember to call this method!
	 */
	fun rebuild() {
		pointMap.clear()
		arrowNavigation.clear()

		// Add nodes
		for (uinode in nodeView.nodes.values) {
			val nodePoint = arrowNavigation.newPoint()
			pointMap[nodePoint] = uinode

			// Add its ports and tie them to the node, so arrow keys can jump from node to ports
			for (uiport in uinode.getPorts()) {
				val uiportPoint = arrowNavigation.newPoint()
				pointMap[uiportPoint] = uiport

				if (uiport.output) { // Right side on the node
					val nodePointRight = nodePoint.right

					uiportPoint.left = nodePoint // Goes from output-port back to node

					if (nodePointRight == null) { // No port added yet. We add the first one
						nodePoint.right = uiportPoint
					} else { // There are already a port, attach it below the previous port
						val lastNodePointRight = nodePointRight.findLast(ArrowNavigation.Direction.DOWN)
						lastNodePointRight.down = uiportPoint // Goes from output port to port below
						uiportPoint.up = lastNodePointRight // Goes from this output port to previous one, above
					}
				} else { // Left side on the node
					val nodePointLeft = nodePoint.left

					uiportPoint.right = nodePoint // Goes from input-port back to node

					if (nodePointLeft == null) {
						nodePoint.left = uiportPoint
					} else { // There are already a port, attach it below the previous port
						val lastNodePointLeft = nodePointLeft.findLast(ArrowNavigation.Direction.DOWN)
						lastNodePointLeft.down = uiportPoint
						uiportPoint.up = lastNodePointLeft
					}
				}
			}
		}

		// Then connect all the uiports, creating lines between them that user can follow using arrow keys
		val linesCreated = ArrayList<Line>()
		for (node in nodeView.nodes.values) {
			val nodePoint = pointMap.entries.first { it.value === node }.key

			for (uiportA in node.getPorts()) {
				if (uiportA is UIPortTemporary)
					continue

				val uiportPointA = pointMap.entries.first { it.value === uiportA }.key

				for (uiportB in nodeView.uiNet.getAllConnectedPorts(uiportA)) {
					if (uiportB is UIPortTemporary)
						continue

					if (linesCreated.any { (it.portA === uiportA && it.portB === uiportB) || (it.portB === uiportA && it.portA === uiportB) })
						continue // Already set up

					val uiportPointB = pointMap.entries.first { it.value === uiportB }.key

					val linePoint = arrowNavigation.newPoint()
					val line = Line(uiportA, uiportB)
					linesCreated.add(line)
					pointMap[linePoint] = line

					if (uiportA.output) {
						linePoint.left = uiportPointA
						linePoint.right = uiportPointB

						// Jumping between lines
						val uiportPointARight = uiportPointA.right
						if (uiportPointARight == null) {
							uiportPointA.right = linePoint
							uiportPointB.left = linePoint
						} else {
							val lastLinePoint = uiportPointARight.findLast(ArrowNavigation.Direction.DOWN)
							lastLinePoint.down = linePoint
							linePoint.up = lastLinePoint
						}
					} else {
						linePoint.right = uiportPointA
						linePoint.left = uiportPointB
						uiportPointA.left = linePoint
						uiportPointB.right = linePoint

						// Jumping between lines
						val uiportPointBLeft = uiportPointB.left
						if (uiportPointBLeft == null) {
							uiportPointA.left = linePoint
							uiportPointB.right = linePoint
						} else {
							val lastLinePoint = uiportPointBLeft.findLast(ArrowNavigation.Direction.DOWN)
							lastLinePoint.down = linePoint
							linePoint.up = lastLinePoint
						}
					}
				}
			}
		}

		System.currentTimeMillis()
	}

	fun move(direction: ArrowNavigation.Direction) {
		val last = arrowNavigation.current
		arrowNavigation.move(direction)
		println("Moved from ${pointMap[last]} to ${pointMap[arrowNavigation.current]}")
	}
}