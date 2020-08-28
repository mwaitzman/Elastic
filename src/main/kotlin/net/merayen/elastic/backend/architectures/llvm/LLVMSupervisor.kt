package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.architectures.llvm.nodes.CNode
import net.merayen.elastic.netlist.NetList
import net.merayen.elastic.netlist.Node
import net.merayen.elastic.system.intercom.*

class LLVMSupervisor {
	private var netList: NetList? = null
	private var upcomingNetList: NetList? = null

	private val cnodes = HashMap<String, CNode>()

	fun handle(messages: List<ElasticMessage>) {
		for (message in messages) {
			if (message is NetListMessage) {
				if (upcomingNetList == null)
					upcomingNetList = netList?.copy() ?: NetList()

				when (message) {
					is CreateNodeMessage -> TODO()
					is RemoveNodeMessage -> TODO()
					is CreateNodePortMessage -> TODO()
					is RemoveNodePortMessage -> TODO()
					is NodeConnectMessage -> TODO()
					is NodeDisconnectMessage -> TODO()
				}
			}

			if (message is ProcessRequestMessage) {
				val upcomingNetList = upcomingNetList
				if (upcomingNetList != null) {
					compileC(upcomingNetList)
					netList = upcomingNetList
				}

				netList ?: throw RuntimeException("Can not process. The NetList is empty")
			}
		}
	}

	private fun compileC(netList: NetList) {
		val compiler = NetListCompiler(netList, cnodes)
	}
}