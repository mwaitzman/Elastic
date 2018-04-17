package net.merayen.elastic.ui.objects.contextmenu

import net.merayen.elastic.ui.Draw
import java.util.ArrayList

import net.merayen.elastic.ui.UIObject

internal class Menu(private val count: Int) : UIObject() {
    var radius = 150f

    private var pointer_x: Float = 0.toFloat()
    private var pointer_y: Float = 0.toFloat()

    private var selected = -1

    private val items = ArrayList<ContextMenuItem>()

    override fun onDraw(draw: Draw) {
        val steps = count

        draw.setColor(150, 150, 150)

        draw.setStroke(10f)
        draw.oval(0f, 0f, radius * 2, radius * 2)

        draw.setStroke(3f)
        draw.oval(radius - radius / 3, radius - radius / 3, radius * 2 / 3, radius * 2 / 3)

        draw.setColor(200, 200, 200)

        draw.setStroke(5f)
        draw.oval(0f, 0f, radius * 2, radius * 2)

        draw.setStroke(1f)
        draw.oval(radius - radius / 3, radius - radius / 3, radius * 2 / 3, radius * 2 / 3)

        val item_radius = radius / (3 * (steps / 8.toFloat()))
        var marked = false

        selected = -1

        for (i in 0 until steps) {
            var active = false
            val x = radius - item_radius + Math.sin((i / steps.toFloat()).toDouble() * Math.PI * 2.0).toFloat() * radius
            val y = radius - item_radius + Math.cos((i / steps.toFloat()).toDouble() * Math.PI * 2.0).toFloat() * radius

            if (!marked && (Math.abs(pointer_x) > radius / 3 || Math.abs(pointer_y) > radius / 3)) {
                val pointer = ((Math.atan2((-pointer_x).toDouble(), (-pointer_y).toDouble()) / Math.PI + 1) / 2 * steps + 0.5f) % steps
                if (pointer.toInt() == i) {
                    marked = true
                    active = true
                }
            }

            val menu_index = Math.floorMod(-i + count / 2, steps) // Makes items begin at 12 o'clock
            if (menu_index < items.size) {
                val cmi = items[menu_index]
                cmi.active = active
                cmi.radius = item_radius
                cmi.translation.x = x
                cmi.translation.y = y

                if (active)
                    selected = menu_index
            }
        }
    }

    fun setPointer(x: Float, y: Float) {
        pointer_x = x
        pointer_y = y
    }

    fun addMenuItem(item: ContextMenuItem) {
        items.add(item)
        add(item)
    }

    fun getSelected(): ContextMenuItem? {
        return if (selected > -1 && selected < items.size) items[selected] else null

    }
}