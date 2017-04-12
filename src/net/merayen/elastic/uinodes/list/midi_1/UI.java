package net.merayen.elastic.uinodes.list.midi_1;

import net.merayen.elastic.system.intercom.NodeDataMessage;
import net.merayen.elastic.system.intercom.NodeParameterMessage;
import net.merayen.elastic.ui.objects.components.midiroll.MidiRoll;
import net.merayen.elastic.ui.objects.node.Resizable;
import net.merayen.elastic.ui.objects.node.UINode;
import net.merayen.elastic.ui.objects.node.UIPort;

public class UI extends UINode {
	private MidiRoll midi_roll;

	@Override
	protected void onInit() {
		super.onInit();
		width = 300;
		height = 200;
		titlebar.title = "MIDI Roll";

		midi_roll = new MidiRoll();
		midi_roll.translation.x = 20;
		midi_roll.translation.y = 20;
		add(midi_roll);

		add(new Resizable(this, new Resizable.Handler() {
			@Override
			public void onResize() {
				if(width < 100) width = 100;
				if(height < 100) height = 100;
				if(width > 1000) width = 1000;
				if(height > 1000) height = 1000;

				updateLayout();
			}
		}));

		updateLayout();
	}

	@Override
	protected void onCreatePort(UIPort port) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onRemovePort(UIPort port) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onMessage(NodeParameterMessage message) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onData(NodeDataMessage message) {
		// TODO Auto-generated method stub

	}

	private void updateLayout() {
		midi_roll.width = width - 40;
		midi_roll.height = height - 25;
	}
}
