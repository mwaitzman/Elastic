package net.merayen.elastic.backend.architectures.local.nodes.output_1;

import net.merayen.elastic.backend.architectures.local.LocalProcessor;
import net.merayen.elastic.util.Postmaster.Message;

/**
 * Processor for outputting audio.
 * Accumulates audio and then the Net-node will acquire data when needed.
 */
public class LProcessor extends LocalProcessor {
	private boolean valid = true;

	@Override
	protected void onProcess() {
		// TODO output directly to interface
	}

	@Override
	protected void onInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onPrepare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
	}
}