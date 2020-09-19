package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.architectures.llvm.nodes.TranspilerNode
import net.merayen.elastic.netlist.NetList
import net.merayen.elastic.netlist.Node

/**
 * Give it a NetList and you get C code!
 */
class CTranspiler(netList: NetList, cnodes: Map<String, TranspilerNode>) : Transpiler(netList, cnodes) {
	private class TranspileResult(
		val nodes: List<TranspileNode>
	)

	/**
	 * A single transpiled node.
	 */
	private class TranspileNode(
		val node: Node,
		val struct: String,
		val nodeInit: String,
		val nodeProcess: String,
		val instanceProcess: String,
		val nodeCollect: String,
		val level: Int // TODO no?
	)

	private val nodeCompileResults = ArrayList<TranspileNode>()

	override fun onTranspile(): Transpiler.Companion.Result {
		TODO()
	}

	private fun transpileDeep(node: Node, level: Int = 0) {
		val result = transpileLevel(node, level)
		nodeCompileResults.add(result)

		netList.nodes.filter { nodeProperties.getParent(it) == node.id }.map { transpileDeep(it, level + 1) }
	}

	private fun transpileLevel(node: Node, level: Int): TranspileNode {
		val cnode = cnodes[node.id] ?: throw RuntimeException("Node in NetList has not been added")

		val cwriter = cnode.cwriter ?: throw RuntimeException("CNode has not a CWriter set")

		// TODO probably optimize access here, without using "data->" in each iteration
		val nodeData = "data->node_${node.id}.data" // Local data in the C backend
		val parameters = "data->node_${node.id}.parameters" // Parameters sent from the Kotlin/JVM parent process
		val instanceData = "data->instance_${node.id}[instance_${node.id}_index]->data" // Index of the current node instance
		//val port = "data->instance_${node.id}[instance_${node.id}_index]->ports"

		// TODO figure out which nodes are connected to our inputs and outputs. Make lambda of it

		return TranspileNode(
			node = node,
			struct = cwriter.onStruct(),
			nodeInit = cwriter.onNodeInit(nodeData),
			nodeProcess = cwriter.onNodeProcess(nodeData, parameters),
			instanceProcess = cwriter.onInstanceProcess(parameters, nodeData, instanceData, { name -> "data->that_other_node.outputs.some_port" }, { name -> "data->node_${node.id}.outputs.$name" }),
			nodeCollect = cwriter.onNodeCollect(nodeData, parameters),
			level = level
		)
	}

	/**
	 * Retrieves the C-code.
	 */
	private fun transpileGroupNode(node: String): String {
		return """
			void node_$node(struct NodeData_$node
			""".trimIndent()
	}
}