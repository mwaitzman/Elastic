package net.merayen.elastic.system.intercom

/**
 * Data sent from the process backend when finish with a frame.
 * Read by the logic node for further processing/interpretation.
 */
open class OutputFrameData(override val nodeId: String, var nodeStats: NodeStatusMessage? = null) : NodeDataMessage