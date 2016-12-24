package net.merayen.elastic.backend.logicnodes.list.output_1;

import net.merayen.elastic.backend.interfacing.AbstractDevice;
import net.merayen.elastic.backend.interfacing.devicetypes.AudioDevice;
import net.merayen.elastic.backend.logicnodes.Environment;
import net.merayen.elastic.backend.mix.datatypes.Audio;
import net.merayen.elastic.backend.nodes.BaseLogicNode;
import net.merayen.elastic.system.intercom.NodeParameterMessage;
import net.merayen.elastic.util.pack.FloatArray;
import net.merayen.elastic.util.pack.PackArray;
import net.merayen.elastic.util.pack.PackDict;

public class LogicNode extends BaseLogicNode {

	@Override
	protected void onCreate() {
		createPort(new BaseLogicNode.PortDefinition() {{
			name = "input";
		}});
	}

	@Override
	protected void onInit() {
		Environment env = (Environment)getEnv();
		for(AbstractDevice ad : env.mixer.getAvailableDevices())
			if(ad instanceof AudioDevice)
				if(((AudioDevice)ad).isOutput())
					System.out.println(ad.id);
	}

	@Override
	protected void onConnect(String port) {}

	@Override
	protected void onDisconnect(String port) {}

	@Override
	protected void onParameterChange(NodeParameterMessage message) {
		set(message);
	}

	@Override
	protected void onPrepareFrame(PackDict data) {}

	@Override
	protected void onFinishFrame(PackDict data) {
		PackArray pa = (PackArray)data.data.get("audio");
		FloatArray[] fa = (FloatArray[])pa.data;

		// Count max channels
		int channel_count = 0;
		for(int i = 0; i < fa.length; i++)
			if(fa[i] != null)
				channel_count = i + 1;

		if(channel_count == 0)
			return; // Don't bother

		// Figure out sample count by checking one of the channels (the last channel in this case)
		int sample_count = fa[channel_count - 1].data.length;

		float[/* channel no */][/* sample no */] out = new float[channel_count][];

		int i = 0;
		for(int channel_no = 0; channel_no < channel_count; channel_no++) {
			FloatArray channel = fa[channel_no];

			if(channel != null) {
				//System.out.printf("Output onFinishFrame() got channel no %d with %d samples\n", i, channel.data.length);
				out[i] = channel.data;
			} else {
				out[i] = new float[sample_count];
			}
			i++;
		}

		((Environment)getEnv()).mixer.send("oracle_java:PulseAudio Mixer", new Audio(out));
	}
}
