package net.merayen.elastic.backend.logicnodes.list.out_1

import net.merayen.elastic.backend.nodes.BaseLogicNode

class LogicNode : BaseLogicNode() {
	override fun onCreate() {
		createPort(PortDefinition("in"))
	}

	override fun onInit() {}
	override fun onParameterChange(key: String, value: Any) = set(key, value)
	override fun onData(data: Any) {}
	override fun onConnect(port: String) {}
	override fun onDisconnect(port: String) {}
	override fun onRemove() {}
	override fun onPrepareFrame(data: Map<String, Any>) {}
	override fun onFinishFrame(data: Map<String, Any>) {}
}
