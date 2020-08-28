package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.analyzer.NodeProperties
import net.merayen.elastic.backend.architectures.llvm.nodes.CNode
import net.merayen.elastic.netlist.NetList
import net.merayen.elastic.netlist.Node

/**
 * Give it a NetList and you get C code!
 */
class NetListCompiler(private val netList: NetList, private val cnodes: Map<String, CNode>) {
	private val nodeProperties = NodeProperties(netList)

	private class NodeCompileResult(
		val nodeInit: String,
		val nodeProcess: String,
		val instanceProcess: String,
		val nodeCollect: String,
		val level: Int
	)

	private val nodeCompileResults = ArrayList<NodeCompileResult>()

	init {
		val topNodes = netList.nodes.filter { nodeProperties.getParent(it) == null }
		if (topNodes.size != 1)
			throw RuntimeException("NetList must have exact 1 top-most node. Got ${topNodes.size}")

		val top = topNodes[0]
		if (nodeProperties.getName(top) != "group")
			throw RuntimeException("Top-most node must be a group-node. Got ${nodeProperties.getName(top)}")

		compileDeep(top)
	}

	private fun compileDeep(node: Node, level: Int = 0) {
		val result = compile(node, level)
		nodeCompileResults.add(result)

		netList.nodes.filter { nodeProperties.getParent(it) == node.id }.map { compileDeep(it, level + 1) }
	}

	private fun compile(node: Node, level: Int): NodeCompileResult {
		val cnode = cnodes[node.id] ?: throw RuntimeException("Node in NetList has not been added")

		val cwriter = cnode.cwriter ?: throw RuntimeException("CNode has not a CWriter set")

		// TODO probably optimize access here, without using "data->" in each iteration
		val nodeData = "data->node_${node.id}.data" // Local data in the C backend
		val parameters = "data->node_${node.id}.parameters" // Parameters sent from the Kotlin/JVM parent process
		val instanceData = "data->instance_${node.id}[instance_${node.id}_index]->data" // Index of the current node instance
		//val port = "data->instance_${node.id}[instance_${node.id}_index]->ports"

		// TODO figure out which nodes are connected to our inputs and outputs. Make lambda of it

		return NodeCompileResult(
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
	fun getCode(): String {
		return """
			#include <stdio.h>
			#include <math.h>
			
			void init_nodes() {
				${nodeCompileResults.joinToString("\n") { it.nodeInit }}
			}
			
			void process() {
				${nodeCompileResults.joinToString("\n") { it.nodeProcess.replace("\n", "\n" + ("\t".repeat(it.level + 4))) }}
				
				${nodeCompileResults.joinToString("\n") { it.instanceProcess.replace("\n", "\n" + ("\t".repeat(it.level + 4))) }}
				
				${nodeCompileResults.joinToString("\n") { it.nodeCollect.replace("\n", "\n" + ("\t".repeat(it.level + 4))) }
		}
		""".trimIndent()
	}
}