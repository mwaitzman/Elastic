package net.merayen.elastic.backend.architectures.local.nodes.midi_transpose_1

import net.merayen.elastic.backend.architectures.local.LocalNode
import net.merayen.elastic.backend.architectures.local.LocalProcessor

class LNode : LocalNode(LProcessor::class.java) {
	var transpose = 0

	override fun onInit() {}
	override fun onSpawnProcessor(lp: LocalProcessor?) {}
	override fun onProcess(data: MutableMap<String, Any>?) {}

	override fun onParameter(key: String?, value: Any?) {
		if (key == "transpose" && value is Number)
			transpose = value.toInt()
	}

	override fun onFinishFrame() {}
	override fun onDestroy() {}
}