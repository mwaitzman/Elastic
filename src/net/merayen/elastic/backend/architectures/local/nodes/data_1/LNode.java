package net.merayen.elastic.backend.architectures.local.nodes.data_1;

import net.merayen.elastic.backend.architectures.local.LocalNode;
import net.merayen.elastic.backend.architectures.local.LocalProcessor;

import java.util.Map;

/**
 * Does absolutely nothing. Just a datacontainer for e.g UI objects that stores their data in a node.
 */
public class LNode extends LocalNode {
	public LNode() {
		super(LProcessor.class);
	}

	@Override
	protected void onInit() {}

	@Override
	protected void onSpawnProcessor(LocalProcessor lp) {}

	@Override
	protected void onProcess(Map<String, Object> data) {}

	@Override
	protected void onParameter(String key, Object value) {}

	@Override
	protected void onFinishFrame() {}

	@Override
	protected void onDestroy() {}
}