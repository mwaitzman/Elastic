package net.merayen.elastic;

import net.merayen.elastic.glue.nodes.GlueNode;

public class Main {
	public static void main(String jhgjhg[]) {
		WindowManager wm = new WindowManager();
		Window w = wm.create();

		GlueNode node;

		for(int i = 0; i < 3; i++ ) {
			node = w.node_system.addNode(net.merayen.elastic.client.signalgenerator.Glue.class);
			node.getUINode().translation.x = i * 150f;
			node.getUINode().translation.y = 50;
		}
		node = w.node_system.addNode(net.merayen.elastic.client.output.Glue.class);
		node.getUINode().translation.x = 400f;

		node = w.node_system.addNode(net.merayen.elastic.client.output_benchmark.Glue.class);
		node.getUINode().translation.x = 500f;

		node = w.node_system.addNode(net.merayen.elastic.client.voices.Glue.class);
		node.getUINode().translation.x = 300f;
		node.getUINode().translation.y = 100f;

		node = w.node_system.addNode(net.merayen.elastic.client.midi_input.Glue.class);
		node.getUINode().translation.x = 30f;
		node.getUINode().translation.y = 20f;

		/*node = w.node_system.addNode(net.merayen.merasynth.client.adsr.Glue.class);
		node.getUINode().translation.x = 200f;
		node.getUINode().translation.y = 200f;*/

		node = w.node_system.addNode(net.merayen.elastic.client.mix.Glue.class);
		node.getUINode().translation.x = 50f;
		node.getUINode().translation.y = 200f;

		node = w.node_system.addNode(net.merayen.elastic.client.add.Glue.class);
		node.getUINode().translation.x = 180f;
		node.getUINode().translation.y = 100f;

		/*node = w.node_system.addNode(net.merayen.merasynth.client.delay.Glue.class);
		node.getUINode().translation.x = 100f;
		node.getUINode().translation.y = 30f;*/

		/*node = w.node_system.addNode(net.merayen.merasynth.client.vu.Glue.class);
		node.getUINode().translation.x = 0f;
		node.getUINode().translation.y = 0f;

		node = w.node_system.addNode(net.merayen.merasynth.client.graph.Glue.class);
		node.getUINode().translation.x = 2f;
		node.getUINode().translation.y = 50f;

		node = w.node_system.addNode(net.merayen.merasynth.client.ui_test.Glue.class);
		node.getUINode().translation.x = 100f;
		node.getUINode().translation.y = 100f;*/
	}
}