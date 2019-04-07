package net.merayen.elastic.backend.architectures.local.nodes.mix_1;

import java.util.Map;

import net.merayen.elastic.backend.architectures.local.LocalNode;
import net.merayen.elastic.backend.architectures.local.LocalProcessor;

public class LNode extends LocalNode {
	float mix;
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
	protected void onParameter(String key, Object value) {
		if(key.equals("mix"))
			mix = ((Number)value).floatValue();
	}

	@Override
	protected void onFinishFrame() {}

	@Override
	protected void onDestroy() {}
}