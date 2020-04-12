package net.merayen.elastic.backend.architectures.local.nodes.value_1

import net.merayen.elastic.backend.architectures.local.LocalProcessor
import net.merayen.elastic.backend.architectures.local.lets.SignalOutlet

class LProcessor : LocalProcessor() {
	private var currentTime = 0.0
	private var done = false

	override fun onProcess() {
		val time = getOutlet("time") as? SignalOutlet

		if (time != null) { // Time in seconds sine the
			var t = currentTime
			val increase = 1.0 / sampleRate
			for (i in time.signal.indices) {
				time.signal[i] = t.toFloat()
				t += increase
			}

			currentTime = t

			time.push()
		}

		done = true
	}

	override fun onPrepare() {
		done = false
	}

	override fun onInit() {}
	override fun onDestroy() {}
}