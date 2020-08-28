package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.architectures.llvm.nodes.ValueCNode
import net.merayen.elastic.backend.architectures.llvm.nodes.CNode
import net.merayen.elastic.netlist.NetList
import net.merayen.elastic.system.intercom.CreateNodeMessage
import net.merayen.elastic.util.NetListMessages
import org.junit.jupiter.api.Test

internal class NetListCompilerTest {
	@Test
	fun `generate c code`() {
		val netList = NetList()
		NetListMessages.apply(netList, CreateNodeMessage("group0", "group", 1, null))
		NetListMessages.apply(netList, CreateNodeMessage("value0", "value", 1, "group0"))

		val cnodes = HashMap<String, CNode>()
		cnodes["group0"] = ValueCNode(CNode.Env("group0", 44100, 256))
		cnodes["value0"] = ValueCNode(CNode.Env("value0", 44100, 256))

		val compiler = NetListCompiler(netList, cnodes)
		println(compiler.getCode())
	}
}