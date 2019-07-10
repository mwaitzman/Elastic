package net.merayen.elastic.backend.logicnodes.list.midi_1

import java.util.ArrayList

import net.merayen.elastic.backend.interfacing.types.MidiPacket
import net.merayen.elastic.backend.logicnodes.Format
import net.merayen.elastic.backend.nodes.BaseLogicNode
import net.merayen.elastic.system.intercom.OutputFrameData

class LogicNode : BaseLogicNode() {
	internal var buffer: MutableList<MidiPacket> = ArrayList()

	private val parameters = Parameters(this)

	override fun onCreate() {
		createPort(object : BaseLogicNode.PortDefinition() {
			init {
				name = "in"
			}
		})

		createPort(object : BaseLogicNode.PortDefinition() {
			init {
				name = "out"
				output = true
				format = Format.MIDI
			}
		})
	}

	override fun onInit() {}

	override fun onParameterChange(key: String, value: Any) {
		set(key, value)

		when (key) {
			"mute" -> System.out.println("Mute! Got it!")
			"trackName" -> System.out.println("Midi node received track-name '${value as String}'")
		}
	}

	override fun onConnect(port: String) {}

	override fun onDisconnect(port: String) {}

	override fun onRemove() {}

	override fun onPrepareFrame(data: MutableMap<String, Any>) {
		val midi = arrayOfNulls<ShortArray>(buffer.size)

		var i = 0
		for (mp in buffer)
			midi[i++] = mp.midi

		buffer.clear()

		data["midi"] = midi
	}

	override fun onFinishFrame(data: OutputFrameData) {}

	override fun onData(data: Any) {
		when (data) {
			is PushTangentMessage -> {
				buffer.add(MidiPacket(shortArrayOf(144.toShort(), data.tangent, 64), 0))
			}
			is ReleaseTangentMessage -> {
				buffer.add(MidiPacket(shortArrayOf(128.toShort(), data.tangent, 64), 0))
			}
			is AddEventZoneMessage -> {
				println("Adding EventZone id=${data.eventZoneId}, start=${data.start}, length=${data.length}")
				val eventZones = parameters.getEventZones()
				val eventZone = Parameters.EventZone()
				eventZone.id = data.eventZoneId
				eventZone.start = data.start
				eventZone.length = data.length
				eventZones.add(eventZone)

				parameters.setEventZones(eventZones)
			}
			is ChangeEventZoneMessage -> {
				println("Changing EventZone id=${data.eventZoneId}, start=${data.start}, length=${data.length}")
				val eventZones = parameters.getEventZones()
				val eventZone = eventZones.find { it.id == data.eventZoneId }

				if (eventZone != null) {
					eventZone.start = data.start
					eventZone.length = data.length
					parameters.setEventZones(eventZones)
				}
			}
			is RemoveEventZoneMessage -> {
				println("Removing EventZone id=${data.eventZoneId}")
				val eventZones = parameters.getEventZones()
				val eventZone = eventZones.removeIf { it.id == data.eventZoneId }
				parameters.setEventZones(eventZones)
			}
		}
	}
}
