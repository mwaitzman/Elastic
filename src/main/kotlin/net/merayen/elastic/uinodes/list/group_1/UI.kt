package net.merayen.elastic.uinodes.list.group_1

import net.merayen.elastic.backend.nodes.BaseNodeProperties
import net.merayen.elastic.system.intercom.NodeDataMessage
import net.merayen.elastic.ui.objects.node.UINode
import net.merayen.elastic.ui.objects.node.UIPort

class UI : UINode() {
	override fun onCreatePort(port: UIPort) {}
	override fun onRemovePort(port: UIPort) {}
	override fun onMessage(message: BaseNodeProperties) {}
	override fun onData(message: NodeDataMessage) {}
	override fun onParameter(instance: BaseNodeProperties) {}
}
