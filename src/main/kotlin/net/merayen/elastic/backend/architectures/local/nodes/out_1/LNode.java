package net.merayen.elastic.backend.architectures.local.nodes.out_1;

import net.merayen.elastic.backend.architectures.local.LocalNode;
import net.merayen.elastic.backend.architectures.local.LocalProcessor;
import net.merayen.elastic.backend.architectures.local.lets.Inlet;
import net.merayen.elastic.backend.architectures.local.nodes.poly_1.OutputInterfaceNode;
import net.merayen.elastic.backend.nodes.BaseNodeProperties;
import net.merayen.elastic.system.intercom.InputFrameData;

public class LNode extends LocalNode implements OutputInterfaceNode {
	private int channelCount;

	public LNode() {
		super(LProcessor.class);
	}

	@Override
	protected void onInit() {}

	@Override
	protected void onSpawnProcessor(LocalProcessor lp) {
		((LProcessor)lp).channelDistribution = channelCount++ % 2 == 0 ? new float[]{1,0} : new float[]{0,1};
	}

	@Override
	protected void onProcess(InputFrameData data) {}

	@Override
	protected void onParameter(BaseNodeProperties instance) {}

	@Override
	protected void onFinishFrame() {}

	@Override
	protected void onDestroy() {}

	@Override
	public String getForwardPortName() {
		return "output";
	}

	@Override
	public Inlet getOutputInlet(int session_id) {
		return ((LProcessor)getProcessor(session_id)).inlet;
	}

	@Override
	public float[] getChannelDistribution(int session_id) {
		return ((LProcessor)getProcessor(session_id)).channelDistribution;
	}
}
