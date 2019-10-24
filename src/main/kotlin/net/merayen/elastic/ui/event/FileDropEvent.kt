package net.merayen.elastic.ui.event

class FileDropEvent(surfaceID: String, id: Int, x: Int, y: Int, action: Action, button: Button, val files: Array<String>) : MouseEvent(surfaceID, id, x, y, action, button)