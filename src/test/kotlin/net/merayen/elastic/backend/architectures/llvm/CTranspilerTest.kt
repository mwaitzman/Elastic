package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.architectures.llvm.nodes.ValueTranspilerNode
import net.merayen.elastic.backend.architectures.llvm.nodes.TranspilerNode
import net.merayen.elastic.netlist.NetList
import net.merayen.elastic.system.intercom.CreateNodeMessage
import net.merayen.elastic.util.NetListMessages
import org.junit.jupiter.api.Test

internal class CTranspilerTest {
	@Test
	fun `generate c code`() {
		val netList = NetList()
		NetListMessages.apply(netList, CreateNodeMessage("group0", "group", 1, null))
		NetListMessages.apply(netList, CreateNodeMessage("value0", "value", 1, "group0"))

		val cnodes = HashMap<String, TranspilerNode>()
		cnodes["group0"] = ValueTranspilerNode(TranspilerNode.Env("group0", 44100, 256))
		cnodes["value0"] = ValueTranspilerNode(TranspilerNode.Env("value0", 44100, 256))

		val compiler = CTranspiler(netList, cnodes)
		println(compiler.transpile())
	}
}