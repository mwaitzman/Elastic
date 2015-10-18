package net.merayen.merasynth.client.midi_input;

import net.merayen.merasynth.ui.objects.components.Label;
import net.merayen.merasynth.ui.objects.node.UINode;
import net.merayen.merasynth.ui.objects.node.UIPort;

public class UI extends UINode {
	private UIPort port;

	private Label midi_device;

	public void onInit() {
		super.onInit();

		width = 100f;
		height = 50f;

		titlebar.title = "MIDI Input";

		// Statistics
		midi_device = new Label();
		midi_device.translation.x = 10f;
		midi_device.translation.y = 20f;
		add(midi_device);
	}

	@Override
	protected void onDraw() {
		midi_device.label = "Device: ";
		super.onDraw();
	}

	@Override
	public void onCreatePort(String name) {
		if(name.equals("output")) {
			port = new UIPort("output", false);
			port.translation.x = width;
			port.translation.y = 20f;
			addPort(port);
		}
	}

	@Override
	public void onRemovePort(String name) {
		// Never removes any port anyway, so not implemented
	}
}
