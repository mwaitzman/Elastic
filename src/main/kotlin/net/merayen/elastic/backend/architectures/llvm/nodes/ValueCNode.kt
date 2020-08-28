package net.merayen.elastic.backend.architectures.llvm.nodes

import net.merayen.elastic.backend.architectures.llvm.CWriter

/**
 * A node that adds two inputs.
 */
class ValueCNode(env: Env) : CNode(env) {
	init {
		cwriter = object : CWriter {
			override fun onNodeInit(nodeData: String) = ""

			override fun onNodeProcess(node: String, parameters: String) = ""

			override fun onInstanceProcess(parameters: String, nodeData: String, instanceData: String, inPort: (name: String) -> String, outPort: (name: String) -> String) = """
				float value = $parameters.value;
				for (int i = 0; i < $frameSize; i++)
				${outPort("out")}[i] = value;
				""".trimIndent()

			override fun onNodeCollect(node: String, parameters: String) = """
				printf("add should send data!\n");
				""".trimIndent()
		}
	}
}