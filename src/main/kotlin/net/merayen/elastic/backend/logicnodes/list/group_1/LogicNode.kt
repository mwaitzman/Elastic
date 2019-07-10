package net.merayen.elastic.backend.logicnodes.list.group_1

import net.merayen.elastic.backend.nodes.BaseLogicNode
import net.merayen.elastic.system.intercom.OutputFrameData

/**
 * Doesn't do anything, other than having children.
 */
class LogicNode : BaseLogicNode() {
	override fun onCreate() {}
	override fun onInit() {}
	override fun onConnect(port: String) {}
	override fun onDisconnect(port: String) {}
	override fun onRemove() {}
	override fun onPrepareFrame(data: Map<String, Any>) {}
	override fun onFinishFrame(data: OutputFrameData) {}
	override fun onData(data: Any) {}
	override fun onParameterChange(key: String, value: Any) = set(key, value)
}
