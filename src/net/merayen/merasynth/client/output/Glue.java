package net.merayen.merasynth.client.output;

import java.util.HashMap;

import org.json.simple.JSONObject;

import net.merayen.merasynth.glue.Context;
import net.merayen.merasynth.glue.nodes.GlueNode;

public class Glue extends GlueNode {
	private float frequency;

	public Glue(Context context) {
		super(context);
	}

	@Override
	public String getClassPath() { // XXX Da wut, don't we already know this?
		return "net.merayen.merasynth.client.output";
	}

	@Override
	public String getFriendlyName() {
		return "Output";
	}

	@Override
	public String getDescription() {
		return "Outputs audio";
	}

	@Override
	public void onInit() {
		super.onInit();
		createPort("input");
	}

	protected void onDump(JSONObject state) {
		
	}

	protected void onRestore(JSONObject state) {
		frequency = ((Double)state.get("frequency")).floatValue();
		System.out.printf("PulseGenerator is getting restored! My freq: %f\n", frequency);

		ui().whenReady( () -> ui().setFrequency(frequency));
	}

	private net.merayen.merasynth.client.signalgenerator.UI ui() {
		return ((net.merayen.merasynth.client.signalgenerator.UI)this.getUINode());
	}

	// Functions called from UI
	public void testbuttonClicked() {
		((Net)getNetNode()).requestAudio();
	}

	public HashMap<String, Number> getStatistics() {
		return ((Net)getNetNode()).getStatistics();
	}
}
