package net.merayen.elastic.ui.objects.top.views.arrangementview

import net.merayen.elastic.backend.logicnodes.list.group_1.PlaybackStatusMessage
import net.merayen.elastic.ui.FlexibleDimension
import net.merayen.elastic.ui.UIObject
import net.merayen.elastic.ui.objects.components.autolayout.AutoLayout
import net.merayen.elastic.ui.objects.components.autolayout.LayoutMethods

class EventList : UIObject(), FlexibleDimension {
	interface Handler {
		fun onPlayheadMoved(beat: Float)
	}

	override var layoutWidth = 100f
	override var layoutHeight = 100f

	var beatWidth = 20f

	var handler: Handler? = null

	private val arrangementGrid = ArrangementGrid()
	private val arrangementEventTracks = UIObject()
	private val eventPanes = AutoLayout(LayoutMethods.HorizontalBox())
	private val playhead = Playhead()
	private val playheadBar = PlayheadBar()

	/**
	 * Used to interpolate movement of the playhead
	 */
	private var bpm = 100f

	private var playheadPosition = 0f
	private var isPlaying = false
	private var lastPlayheadPositionChange = System.currentTimeMillis()

	override fun onInit() {
		add(arrangementEventTracks)
		add(arrangementGrid)
		add(eventPanes)
		add(playhead) // Always on top

		arrangementEventTracks.translation.y = 20f
		arrangementGrid.translation.y = 20f
		eventPanes.translation.y = 20f

		playhead.handler = object : Playhead.Handler {
			override fun onMoved(beat: Float) {
				handler?.onPlayheadMoved(beat)
			}
		}

		add(playheadBar)
	}

	override fun onUpdate() {
		arrangementGrid.layoutWidth = layoutWidth
		arrangementGrid.layoutHeight = layoutHeight

		for (obj in eventPanes.search.children) {
			if (obj is EventPane) {
				obj.layoutWidth = layoutWidth
				obj.beatWidth = beatWidth
			}
		}

		eventPanes.placement.maxWidth = layoutWidth

		playhead.layoutHeight = layoutHeight
		playhead.beatWidth = beatWidth

		arrangementGrid.barWidth = beatWidth * 4

		// Smooth cursor motion when playing because
		if (isPlaying)
			playhead.setPosition(playheadPosition + (System.currentTimeMillis() - lastPlayheadPositionChange) / 1000f * (bpm / 60f))
	}

	fun addEventPane(eventPane: EventPane) = this.eventPanes.add(eventPane)
	fun removeEventPane(eventPane: EventPane) = this.eventPanes.remove(eventPane)

	fun handleMessage(message: PlaybackStatusMessage) {
		playhead.setPosition(message.currentPlayheadPosition)
		bpm = message.currentBPM
		isPlaying = message.isPlaying
		playheadPosition = message.currentPlayheadPosition
		lastPlayheadPositionChange = System.currentTimeMillis()
	}

	fun setPlayheadPosition(beat: Float) {
		playhead.setPosition(beat)
	}
}