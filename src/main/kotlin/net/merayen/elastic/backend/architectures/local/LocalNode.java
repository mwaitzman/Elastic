package net.merayen.elastic.backend.architectures.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.merayen.elastic.backend.analyzer.NodeProperties;
import net.merayen.elastic.netlist.NetList;
import net.merayen.elastic.netlist.Node;
import net.merayen.elastic.system.intercom.NodeStatusMessage;

/**
 * A LocalNode is a node that implements the logic for local JVM processing.
 * All nodes must implement this.
 * Its processors are the ones actually performing the work.
 * LocalNodes receives parameter changes and behaves after that.
 * 
 * TODO implement ingoing data
 */
public abstract class LocalNode {
	Supervisor supervisor;
	NetList netlist;
	Node node; // The NetList-node we represent
	public int sample_rate;
	public int buffer_size;
	private final Class<? extends LocalProcessor> processor_cls;
	private NodeProperties properties;

	public Map<String, Object> ingoing = new HashMap<>(); // Data sent to this node from LogicNodes
	public final Map<String, Object> outgoing = new HashMap<>(); // Data that is the result from the processing (read from the outside)

	private long lastNodeStats = System.currentTimeMillis();

	/**
	 * Initialize here.
	 * Do not attempt to do any creation of sessions, they must be done in onProcess().
	 */
	protected abstract void onInit();

	/**
	 * Called when Node gets a new processor under itself.
	 * In this function the LocalNode can set parameters on the LocalProcessor() before the LocalProcessor()'s onInit gets called.
	 */
	protected abstract void onSpawnProcessor(LocalProcessor lp);

	/**
	 * Gets called on the beginning of processing a frame.
	 */
	protected abstract void onProcess(Map<String, Object> data);

	protected abstract void onParameter(String key, Object value);

	/**
	 * Called when a frame has been processed.
	 * LocalNode should here process the data received from the processors, forward them, if this is applicable.
	 */
	protected abstract void onFinishFrame();

	protected abstract void onDestroy();

	public LocalNode(Class<? extends LocalProcessor> cls) {
		processor_cls = cls;
	}

	public String getID() {
		return node.getID();
	}

	void compiler_setInfo(Supervisor supervisor, Node node, int sample_rate, int buffer_size) {
		this.supervisor = supervisor;
		this.netlist = supervisor.netlist;
		this.node = node;
		this.sample_rate = sample_rate;
		this.buffer_size = buffer_size;
		this.properties = new NodeProperties(netlist);
	}

	void finishFrame() {
		onFinishFrame();
		if(lastNodeStats + 1000 < System.currentTimeMillis()) {
			outgoing.put("node_stats", new NodeStatusMessage(node.getID(), (float)getStatisticsMax(), 0, getStatisticsProcessCount()));
			lastNodeStats = System.currentTimeMillis();
		}
	}

	/**
	 * Returns LocalProcessor for this LocalNode
	 */
	LocalProcessor spawnProcessor(int session_id) {
		LocalProcessor lp;
		try {
			lp = processor_cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		// Set information
		lp.LocalProcessor_setInfo(this, session_id);

		return lp;
	}

	protected List<Integer> getSessions() {
		return supervisor.processor_list.getSessions(this);
	}

	/**
	 * Retrieve the child nodes of this LocalNode.
	 */
	public List<LocalNode> getChildrenNodes() {
		List<LocalNode> result = new ArrayList<>();
		for(Node ln : supervisor.netlist_util.getChildren(node))
			result.add(supervisor.local_properties.getLocalNode(ln));

		return result;
	}

	protected LocalNode getParent() {
		Node parent = supervisor.netlist_util.getParent(node);
		if(parent == null)
			return null;

		return supervisor.local_properties.getLocalNode(parent);
	}

	void init() {
		onInit();
	}

	public LocalProcessor getProcessor(int session_id) {
		return supervisor.getProcessor(this, session_id);
	}

	public List<LocalProcessor> getProcessors() {
		return supervisor.getProcessors(this);
	}

	public Object getParameter(String key) {
		return properties.parameters.get(node, key);
	}

	double getStatisticsAvg() {
		double result = 0;
		for(LocalProcessor lp : getProcessors())
			result += lp.process_times.getAvg();
		return result;
	}

	double getStatisticsMax() {
		double result = 0;
		for(LocalProcessor lp : getProcessors())
			result += lp.process_times.getMax();
		return result;
	}

	int getStatisticsProcessCount() {
		int result = 0;
		for(LocalProcessor lp : getProcessors())
			result += lp.process_count;
		return result;
	}
}