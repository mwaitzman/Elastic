package net.merayen.elastic.backend.architectures.local.nodes.mix_1;

import net.merayen.elastic.backend.architectures.local.LocalProcessor;
import net.merayen.elastic.backend.architectures.local.lets.AudioInlet;
import net.merayen.elastic.backend.architectures.local.lets.AudioOutlet;
import net.merayen.elastic.backend.architectures.local.lets.Inlet;
import net.merayen.elastic.backend.architectures.local.lets.Outlet;
import net.merayen.elastic.util.Postmaster.Message;

public class LProcessor extends LocalProcessor {

	@Override
	protected void onInit() {}

	@Override
	protected void onPrepare() {}

	@Override
	protected void onProcess() {
		Inlet a = getInlet("a");
		Inlet b = getInlet("b");
		Inlet fac = getInlet("fac");
		Outlet out = getOutlet("out");

		if(a instanceof AudioInlet && b instanceof AudioInlet && out != null) {
			int available = available();

			float[][] a_buffer = ((AudioOutlet)a.outlet).audio;
			float[][] b_buffer = ((AudioOutlet)b.outlet).audio;

			int channel_count = Math.max(a_buffer.length, b_buffer.length);

			((AudioOutlet)out).setChannelCount(channel_count);

			float[][] out_buffer = ((AudioOutlet)out).audio;

			int stop = out.written + available;

			if(fac instanceof AudioInlet && fac.outlet instanceof AudioOutlet) {
				float[][] fac_buffer = ((AudioOutlet)fac.outlet).audio;

				for(int channel_no = 0; channel_no < channel_count; channel_no++) {
					for(int i = out.written; i < stop; i++) {
						float af = (a_buffer.length > channel_no ? a_buffer[channel_no][i] : 0);
						float bf = (b_buffer.length > channel_no ? b_buffer[channel_no][i] : 0);

						float volume_a = 1 - Math.max(0, Math.min(1, fac_buffer[0][i] * -1));
						float volume_b = 1 - Math.max(0, Math.min(1, fac_buffer[0][i]     ));

						out_buffer[channel_no][i] = (af * volume_a) + (bf * volume_b);
					}
				}

			} else {
				for(int channel_no = 0; channel_no < channel_count; channel_no++) {
					for(int i = out.written; i < stop; i++) {
						float af = (a_buffer.length > channel_no ? a_buffer[channel_no][i] : 0);
						float bf = (b_buffer.length > channel_no ? b_buffer[channel_no][i] : 0);
						out_buffer[channel_no][i] = af + bf;
					}
				}
			}

			out.written = stop;
			a.read = stop;
			b.read = stop;
			out.push();

		} else if(out != null) { // No input, output nothing
			out.written = out.buffer_size;
			out.push();
		}
	}

	@Override
	protected void onMessage(Message message) {}

	@Override
	protected void onDestroy() {}
}
