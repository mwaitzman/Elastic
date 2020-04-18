package net.merayen.elastic.backend.logicnodes.list.spectrum_1

import net.merayen.elastic.backend.nodes.BaseLogicNode
import net.merayen.elastic.backend.nodes.BaseNodeProperties
import net.merayen.elastic.system.intercom.NodeDataMessage
import net.merayen.elastic.system.intercom.OutputFrameData

class LogicNode : BaseLogicNode() {
	override fun onInit() {
		createInputPort("in")
	}

	override fun onConnect(port: String?) { }
	override fun onDisconnect(port: String?) { }
	override fun onData(data: NodeDataMessage?) { }
	override fun onRemove() { }

	override fun onParameterChange(instance: BaseNodeProperties?) {
		updateProperties(instance)
	}

	override fun onFinishFrame(data: OutputFrameData?) { }
}