package net.merayen.elastic.backend.logicnodes.list.sample_1

import net.merayen.elastic.backend.logicnodes.Format
import net.merayen.elastic.backend.nodes.BaseLogicNode
import net.merayen.elastic.backend.nodes.BaseNodeProperties
import net.merayen.elastic.system.intercom.NodeDataMessage
import net.merayen.elastic.system.intercom.OutputFrameData

class LogicNode : BaseLogicNode() {
	override fun onInit() {
		createInputPort("control")
		createOutputPort("out", Format.AUDIO)
	}

	override fun onParameterChange(instance: BaseNodeProperties) {

	}
	override fun onData(data: NodeDataMessage) {}
	override fun onConnect(port: String?) {}
	override fun onDisconnect(port: String?) {}
	override fun onRemove() {}
	override fun onFinishFrame(data: OutputFrameData?) {}
}