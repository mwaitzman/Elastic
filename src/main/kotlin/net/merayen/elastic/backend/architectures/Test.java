package net.merayen.elastic.backend.architectures;

import net.merayen.elastic.netlist.NetList;
import net.merayen.elastic.netlist.Node;
import net.merayen.elastic.system.intercom.backend.InitBackendMessage;
import net.merayen.elastic.util.NetListMessages;
import net.merayen.elastic.util.Postmaster;
import net.merayen.elastic.util.Postmaster.Message;

public class Test {
	public static void test() {
		NetList netlist = new NetList();

		Node test1 = createTestNode(netlist);
		Node test2 = createTestNode(netlist);

		netlist.connect(test1, "output", test2, "input");

		Dispatch dispatch = new Dispatch(Architecture.LOCAL, new Dispatch.Handler() {
			@Override
			public void onMessageFromProcessor(Message message) {
				
			}
		});

		dispatch.launch(new InitBackendMessage(44100, 16, 1024, ""));

		for(Postmaster.Message m : NetListMessages.INSTANCE.disassemble(netlist))
			dispatch.executeMessage(m);

		long t = System.currentTimeMillis() + 3000;
		while(t > System.currentTimeMillis()) { // Let it run for 3 seconds
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		dispatch.stop();
	}

	private static Node createTestNode(NetList netlist) {
		Node n = netlist.createNode();
		n.properties.put("name", "test");
		n.properties.put("version", 100);

		n.properties.put("test", "Hello on you!");
		netlist.createPort(n, "input");
		netlist.createPort(n, "output");
		return n;
	}
}