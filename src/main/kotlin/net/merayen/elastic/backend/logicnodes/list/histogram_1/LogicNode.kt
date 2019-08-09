package net.merayen.elastic.backend.logicnodes.list.histogram_1

import net.merayen.elastic.backend.architectures.local.nodes.histogram_1.Histogram1NodeOutputFrameData
import net.merayen.elastic.backend.nodes.BaseLogicNode
import net.merayen.elastic.backend.nodes.BaseNodeData
import net.merayen.elastic.system.intercom.NodeDataMessage
import net.merayen.elastic.system.intercom.OutputFrameData

class LogicNode : BaseLogicNode() {
	override fun onParameterChange(instance: BaseNodeData?) {
		updateProperties(instance)
	}

	override fun onCreate() {
		createInputPort("data")
	}

	override fun onInit() {}
	override fun onData(data: NodeDataMessage?) {}
	override fun onConnect(port: String?) {}
	override fun onDisconnect(port: String?) {}
	override fun onRemove() {}

	override fun onFinishFrame(data: OutputFrameData?) {
		if (data is Histogram1NodeOutputFrameData)
			sendDataToUI(HistogramUpdateMessage(nodeId = id, buckets = data.buckets))
	}
}