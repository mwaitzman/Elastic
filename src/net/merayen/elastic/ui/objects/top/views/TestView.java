package net.merayen.elastic.ui.objects.top.views;

import net.merayen.elastic.ui.UIObject;
import net.merayen.elastic.ui.event.IEvent;
import net.merayen.elastic.ui.objects.top.Top;
import net.merayen.elastic.ui.util.Movable;

public class TestView extends View {
	private static int count_id;
	private int count;

	private class MovableBox extends UIObject {
		Movable movable;

		@Override
		protected void onInit() {
			movable = new Movable(this, this);
			movable.setHandler(new Movable.IMoveable() {

				@Override
				public void onMove() {
					
				}

				@Override
				public void onGrab() {
					
				}

				@Override
				public void onDrop() {
					
				}
			});
		}

		@Override
		protected void onDraw() {
			draw.setColor(0, 0, 200);
			draw.fillRect(0, 0, 50, 50);

			/*((Top)search.getTop()).debug.set(String.format("TestView.MovableBox.Absolute %d", count), this.absolute_translation);
			((Top)search.getTop()).debug.set(String.format("TestView.MovableBox.OutlineAbsolute %d", count), this.outline_abs_px);*/
		}

		@Override
		protected void onEvent(IEvent e) {
			movable.handle(e);
		}
	}

	public MovableBox movable_box;

	@Override
	protected void onInit() {
		movable_box = new MovableBox();
		add(movable_box);
		count = count_id++;
	}

	@Override
	protected void onDraw() {
		draw.setColor(20, 20, 50);
		draw.fillRect(0, 0, width, height);
		/*((Top)search.getTop()).debug.set(String.format("TestView.Absolute %d", count), this.absolute_translation);
		((Top)search.getTop()).debug.set(String.format("TestView.OutlineAbsolute %d", count), this.outline_abs_px);*/
	}

	@Override
	public View cloneView() {
		return new TestView();
	}
}