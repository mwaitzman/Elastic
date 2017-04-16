package net.merayen.elastic.backend.logicnodes.list.test_100;

import java.util.Map;

import net.merayen.elastic.backend.nodes.BaseLogicNode;

public class LogicNode extends BaseLogicNode {

	@Override
	protected void onCreate() {
		BaseLogicNode.PortDefinition input = new BaseLogicNode.PortDefinition();
		input.name = "input";
		createPort(input);
	}

	@Override
	protected void onInit() {}

	@Override
	protected void onParameterChange(String key, Object value) {
		set(key, value);
	}

	@Override
	protected void onConnect(String port) {}

	@Override
	protected void onDisconnect(String port) {}

	@Override
	protected void onPrepareFrame(Map<String, Object> data) {}

	@Override
	protected void onFinishFrame(Map<String, Object> data) {}

	@Override
	protected void onRemove() {}

	@Override
	protected void onData(Map<String, Object> data) {
		// TODO Auto-generated method stub
		
	}
}
