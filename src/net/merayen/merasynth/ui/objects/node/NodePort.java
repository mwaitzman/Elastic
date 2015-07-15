package net.merayen.merasynth.ui.objects.node;

class NodePort {
	public final boolean output; // true == port outputs data. false == port receives data
	public final UIPort port;
	public final String name;

	public NodePort(String name, UIPort port, boolean output) {
		this.name = name;
		this.port = port;
		this.output = output;
	}
}