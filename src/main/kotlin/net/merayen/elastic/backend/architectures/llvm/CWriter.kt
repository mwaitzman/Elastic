package net.merayen.elastic.backend.architectures.llvm

interface CWriter {
	/**
	 * Define the content in the struct for this node.
	 *
	 * This is where it stores and retrieves its data.
	 */
	fun onStruct(): String

	/**
	 * Initializing of the node here.
	 * Only happens once for the lifetime of the process.
	 *
	 * E.g run calloc on your data, make sine lookup-table etc.
	 */
	fun onNodeInit(nodeData: String): String

	/**
	 * Called on every frame before processing.
	 *
	 * Do your preparation for current frame here.
	 */
	fun onNodeProcess(node: String, parameters: String): String

	/**
	 * C-code where all the instances should process the frame.
	 */
	fun onInstanceProcess(parameters: String, nodeData: String, instanceData: String, inPort: (name: String) -> String, outPort: (name: String) -> String): String

	/**
	 * C-code that gets run when done processing. Like collecting data from all the instances and forwarding them(?)
	 */
	fun onNodeCollect(nodeData: String, parameters: String): String
}