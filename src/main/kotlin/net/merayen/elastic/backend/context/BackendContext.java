package net.merayen.elastic.backend.context;

import net.merayen.elastic.backend.architectures.Architecture;
import net.merayen.elastic.backend.architectures.Dispatch;
import net.merayen.elastic.backend.logicnodes.Environment;
import net.merayen.elastic.backend.nodes.Supervisor;
import net.merayen.elastic.system.ElasticSystem;
import net.merayen.elastic.system.intercom.ElasticMessage;
import net.merayen.elastic.system.intercom.backend.InitBackendMessage;

/**
 * Glues together NetList, MainNodes and the processing backend (architecture)
 */
public class BackendContext {
	Environment env;
	Supervisor logicnode_supervisor;
	Dispatch dispatch;

	public MessageHandler message_handler = new MessageHandler(this);

	/**
	 * Create a new default context, and starts it automatically.
	 * Remember to call end()!
	 */
	public BackendContext(ElasticSystem system, InitBackendMessage message) {
		env = Env.INSTANCE.create(system, message);

		dispatch = new Dispatch(Architecture.LOCAL, message1 -> message_handler.queueFromProcessor(message1));

		dispatch.launch(message);

		logicnode_supervisor = new Supervisor(env, new Supervisor.Handler() {
			@Override
			public void sendMessageToUI(ElasticMessage message) { // Message sent from LogicNodes to the UI
				message_handler.handleFromLogicToUI(message);
			}

			@Override
			public void sendMessageToProcessor(ElasticMessage message) { // Messages sent further into the backend, from the LogicNodes
				message_handler.handleFromLogicToProcessor(message);
			}

			@Override
			public void onProcessDone() {
				env.synchronization.push();
				env.mixer.dispatch(message.buffer_size);
			}
		});
	}

	public void start() {
		env.synchronization.start();
	}

	public Supervisor getLogicNodeList() {
		return logicnode_supervisor;
	}

	public synchronized void update() {
		message_handler.executeMessagesToBackend();
		message_handler.executeMessagesFromProcessor();
	}

	/**
	 * Stops the whole backend. Not possible to restart it.
	 */
	public void end() {
		dispatch.stop();
		env.synchronization.end();

		dispatch = null;
		env = null;
		logicnode_supervisor = null;
		message_handler = null;
	}

	public Environment getEnvironment() {
		return env;
	}
}
