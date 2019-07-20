package net.merayen.elastic.system.actions;

import net.merayen.elastic.backend.nodes.BaseNodeData;
import net.merayen.elastic.system.Action;
import net.merayen.elastic.system.intercom.*;
import net.merayen.elastic.system.intercom.backend.CreateCheckpointMessage;
import net.merayen.elastic.system.intercom.backend.InitBackendMessage;
import net.merayen.elastic.system.intercom.ui.InitUIMessage;
import net.merayen.elastic.util.tap.Tap;
import net.merayen.elastic.util.tap.TapSpreader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates a new blank project, with a few nodes.
 */
public class NewProject extends Action {
	ArrayList<CreateNodeMessage> nodes = new ArrayList<>();
	private final String project_path;

	public NewProject(String project_path) {
		if (new File(project_path).exists())
			throw new RuntimeException("Project already exists. Use LoadProject()-action instead.");
		this.project_path = project_path;
	}

	@Override
	protected void run() {
		try (
				Tap<Object> from_backend = system.tapIntoMessagesFromBackend()) {

			from_backend.set(new TapSpreader.Func<Object>() {
				public void receive(Object message) {
					if (message instanceof CreateNodeMessage) {
						nodes.add((CreateNodeMessage) message);
					} else if (message instanceof BeginResetNetListMessage) {
						nodes.clear();
					} else if (message instanceof ProcessMessage) {
						//ProcessMessage pm = (ProcessMessage)message;
						System.out.print("Process response\n");
					}
				}
			});

			init();
		}
	}

	/**
	 * This is an example to show how it is possible to script Elastic, setting it up, creating nodes, connecting them and then have audio play out on
	 * the speakers.
	 * Whole Elastic can be controlled by just sending and receiving messages. This will open up for "multiplayer music creation" if we would like to
	 * go down that road.
	 */
	private void init() {
		// Make sure everything is destroyed before we init
		system.end();

		// Start the backend (audio processing and logic)
		system.sendMessageToBackend(new InitBackendMessage(44100, 16, 512, project_path));

		// Init the UI, which will display a window
		system.sendMessageToUI(new InitUIMessage());

		// Create the top-most node which will contain everything
		system.sendMessageToBackend(new CreateNodeMessage("group", 1, null));

		// Wait until the top-most node has been created
		waitFor(() -> nodes.size() == 1);

		// Create nodes inside our top-most node
		system.sendMessageToBackend(new CreateNodeMessage("signalgenerator", 1, nodes.get(0).node_id));
		system.sendMessageToBackend(new CreateNodeMessage("output", 1, nodes.get(0).node_id));

		// Wait until all the nodes has been reported to have been created (we receive async messages back upon backend having created them)
		waitFor(() -> nodes.size() == 3);

		// Set the position of the nodes
		net.merayen.elastic.backend.logicnodes.list.output_1.Data outputNodeData = new net.merayen.elastic.backend.logicnodes.list.output_1.Data();
		outputNodeData.setUiTranslation(new BaseNodeData.UITranslation(200f, 0f));
		system.sendMessageToBackend(new NodeParameterMessage(nodes.get(2).node_id, outputNodeData));

		// Connect signal generator to output
		system.sendMessageToBackend(new NodeConnectMessage(nodes.get(1).node_id, "output", nodes.get(2).node_id, "input"));

		// Set frequency parameter on one of the nodes
		net.merayen.elastic.backend.logicnodes.list.signalgenerator_1.Data signalgeneratorNodeData = new net.merayen.elastic.backend.logicnodes.list.signalgenerator_1.Data();
		signalgeneratorNodeData.setFrequency(1000f);
		system.sendMessageToBackend(new NodeParameterMessage(nodes.get(1).node_id, signalgeneratorNodeData));

		// Store the whole project (same as going to "File" --> "Save project")
		system.sendMessageToBackend(new CreateCheckpointMessage());
	}
}
