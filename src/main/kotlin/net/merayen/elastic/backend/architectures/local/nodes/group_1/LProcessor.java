package net.merayen.elastic.backend.architectures.local.nodes.group_1;

import net.merayen.elastic.backend.architectures.local.LocalProcessor;
import net.merayen.elastic.backend.architectures.local.exceptions.SpawnLimitException;

public class LProcessor extends LocalProcessor {
	int session_id = -1;

	@Override
	protected void onInit() {}

	@Override
	protected void onPrepare() {}

	@Override
	protected void onProcess() {
		
			try {
				if(session_id == -1)
					session_id = spawnSession(0);
			} catch (SpawnLimitException e) {
				throw new RuntimeException("Should not happen");
			}
	}

	@Override
	protected void onMessage(Object message) {}

	@Override
	protected void onDestroy() {}

}
