package net.merayen.elastic.backend.architectures.llvm

import net.merayen.elastic.backend.analyzer.NodeProperties
import net.merayen.elastic.backend.architectures.llvm.nodes.TranspilerNode
import net.merayen.elastic.netlist.NetList

/**
 * Interface for classes that transpiles NetList to another programming language.
 * TODO only transpile the actual changed parts
 */
abstract class Transpiler(protected val netList: NetList, protected val cnodes: Map<String, TranspilerNode>) {
	companion object {
		class Result(val code: String) // TODO should be split up into several modules so that e.g clang only needs to compile a smaller section
	}

	protected val nodeProperties = NodeProperties(netList)

	fun transpile() {
		val topNodes = netList.nodes.filter { nodeProperties.getParent(it) == null }
		if (topNodes.size != 1)
			throw RuntimeException("NetList must have exact 1 top-most node. Got ${topNodes.size}")

		val top = topNodes[0]
		if (nodeProperties.getName(top) != "group")
			throw RuntimeException("Top-most node must be a group-node. Got ${nodeProperties.getName(top)}")
	}
	/**
	 * Transpiler inheriting should do a new transpile.
	 * TODO accept a Node-argument that tells from which point to transpile
	 */
	abstract protected fun onTranspile(): Result
}