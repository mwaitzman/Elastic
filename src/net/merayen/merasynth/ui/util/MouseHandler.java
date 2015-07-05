package net.merayen.merasynth.ui.util;

import net.merayen.merasynth.ui.event.IEvent;
import net.merayen.merasynth.ui.event.MouseEvent;
import net.merayen.merasynth.ui.objects.UIObject;

public class MouseHandler {
	/*
	 * Helper class to make an UIObject moveable.
	 * Tightly coupled to UIObjects
	 * TODO implement depth testing of UIObject().current_z_index!
	 */

	public interface IMouseHandler {
		public void onMouseDown(net.merayen.merasynth.ui.Point position); // Mouse down on the hitbox
		public void onMouseUp(net.merayen.merasynth.ui.Point position); // Mouse up on the hitbox
		public void onMouseOver(); // Mouse enters the hitbox
		public void onMouseOut(); // Mouse leaves the hitbox
		public void onMouseMove(net.merayen.merasynth.ui.Point position); // Mouse moves over the hitbox
		public void onMouseDrag(net.merayen.merasynth.ui.Point start_point, net.merayen.merasynth.ui.Point offset); // Mouse down on hitbox and is now moving
		public void onMouseDrop(net.merayen.merasynth.ui.Point start_point, net.merayen.merasynth.ui.Point offset); // Mouse has dragged item but now drops it
		public void onGlobalMouseMove(net.merayen.merasynth.ui.Point global_position); // Mouse has been moved anywhere. TODO add others too? Like click
	}

	private IMouseHandler handler_class;
	protected UIObject uiobject;

	private net.merayen.merasynth.ui.Point drag_start;

	private boolean mouse_down = false;
	private boolean mouse_over = false;
	private boolean mouse_dragging = false;

	public MouseHandler(UIObject uiobject) {
		this.uiobject = uiobject;
	}

	public void setHandler(IMouseHandler cls) {
		this.handler_class = cls;
	}

	public void handle(IEvent event) {
		/*
		 * Call this from your UIObject to handle event
		 */
		if(event instanceof MouseEvent) {
			MouseEvent e = (MouseEvent)event;
			int x = e.mouse_event.getX();
			int y = e.mouse_event.getY();
			net.merayen.merasynth.ui.Point p_relative = uiobject.getPointFromPixel(x, y);
			net.merayen.merasynth.ui.Point p_absolute = uiobject.getAbsolutePointFromPixel(x, y);
			
			// TODO read hit from MouseEvent instead
			boolean hit = (e.getTopmostHit() == uiobject);
			
			if(e.action == MouseEvent.action_type.DOWN && hit) {
				mouse_down = true;
				handler_class.onMouseDown(p_relative);
				
				drag_start = new net.merayen.merasynth.ui.Point(p_relative.x, p_relative.y);
				handler_class.onMouseDrag(p_relative, new net.merayen.merasynth.ui.Point(0, 0));
			}
			else if(e.action == MouseEvent.action_type.UP) {
				if(hit)
					handler_class.onMouseUp(p_relative);
				if(mouse_dragging && mouse_down) {
					mouse_dragging = false;
					handler_class.onMouseDrop(p_relative, new net.merayen.merasynth.ui.Point(p_relative.x - drag_start.x, p_relative.y - drag_start.y));
				}
				mouse_down = false;
			}
			else if(e.action == MouseEvent.action_type.MOVE) {
				handler_class.onGlobalMouseMove(p_absolute);
				
				if(hit) {
					handler_class.onMouseMove(p_relative);
				
					if(!mouse_over) {
						mouse_over = true;
						handler_class.onMouseOver();
					}
				}
				
				if(!hit && mouse_over) {
					mouse_over = false;
					handler_class.onMouseOut();
				}
				
				if(mouse_down) {
					mouse_dragging = true;
					handler_class.onMouseDrag(p_relative, new net.merayen.merasynth.ui.Point(p_relative.x - drag_start.x, p_relative.y - drag_start.y));
				}
			}
		}
	}
}
