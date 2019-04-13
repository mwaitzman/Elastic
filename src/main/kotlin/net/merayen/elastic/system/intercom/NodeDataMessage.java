package net.merayen.elastic.system.intercom;

import net.merayen.elastic.util.Postmaster;

/**
 * Similar to NodeParameterMessage(), but does not set any state of the Node, only pure data (like audio, FFT results etc).
 * This is also by no way serialized and stored.
 */
public class NodeDataMessage extends Postmaster.Message implements NodeMessage {
	public final String nodeId;
	public final Object value; // Must be compatible for JSON serializing

	public NodeDataMessage(String node_id, Object value) {
		this.nodeId = node_id;
		this.value = value;
	}

	@Override
	public String getNodeId() {
		return nodeId;
	}
}
