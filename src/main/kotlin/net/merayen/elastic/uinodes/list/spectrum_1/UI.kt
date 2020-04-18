package net.merayen.elastic.uinodes.list.spectrum_1

import net.merayen.elastic.backend.nodes.BaseNodeProperties
import net.merayen.elastic.system.intercom.NodeDataMessage
import net.merayen.elastic.ui.Draw
import net.merayen.elastic.ui.objects.node.UINode
import net.merayen.elastic.ui.objects.node.UIPort

class UI : UINode() {

	override fun onInit() {
		super.onInit()

	}

	override fun onCreatePort(port: UIPort) { }
	override fun onRemovePort(port: UIPort) { }
	override fun onProperties(properties: BaseNodeProperties) { }
	override fun onData(message: NodeDataMessage) { }
}