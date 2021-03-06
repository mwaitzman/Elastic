package net.merayen.elastic.backend.logicnodes.list.signalgenerator_1

import net.merayen.elastic.backend.logicnodes.Format
import net.merayen.elastic.backend.nodes.BaseLogicNode
import net.merayen.elastic.backend.nodes.BaseNodeProperties
import net.merayen.elastic.system.intercom.NodeDataMessage
import net.merayen.elastic.system.intercom.OutputFrameData

class LogicNode : BaseLogicNode() {
	override fun onInit() {
		createInputPort("frequency")
		createOutputPort("output", Format.AUDIO)

		(properties as Properties).frequency = 440f
	}

	override fun onParameterChange(instance: BaseNodeProperties) { // Parameter change from UI
		this.updateProperties(instance) // Accept anyway
	}

	override fun onConnect(port: String) {}
	override fun onDisconnect(port: String) {}
	override fun onFinishFrame(data: OutputFrameData?) {}
	override fun onRemove() {}
	override fun onData(data: NodeDataMessage) {}
}
