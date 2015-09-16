package net.merayen.merasynth.client.ui_test;

import net.merayen.merasynth.ui.objects.UIClip;
import net.merayen.merasynth.ui.objects.UIGroup;

public class ClipTest extends UIGroup {
	class Inner extends UIGroup {

		int level;

		public void set(int level) {
			this.level = level;
		}

		@Override
		protected void onDraw() {
			super.onDraw();
			draw.setColor(50 + 50*level, 0, 0);
			draw.fillRect(0, 0, 30 - level * 10, 30 - level * 10);
		}
	}

	class InnerInner extends UIGroup {
		@Override
		protected void onDraw() {
			super.onDraw();
			draw.setColor(50, 0, 0);
			draw.fillRect(0, 0, 10, 10);
		}
	}

	private UIClip clip1, clip2;
	private Inner inner1, inner2;

	@Override
	protected void onInit() {
		super.onInit();

		clip1 = new UIClip();
		clip1.translation.x = 0f;
		clip1.translation.y = 0f;
		clip1.width = 20f;
		clip1.height = 20f;
		add(clip1);

		inner1 = new Inner();
		inner1.set(1);
		clip1.add(inner1);

		clip2 = new UIClip();
		clip2.translation.x = 0f;
		clip2.translation.y = 0f;
		clip2.width = 10f;
		clip2.height = 10f;
		inner1.add(clip2);

		inner2 = new Inner();
		inner2.set(2);
		clip2.add(inner2);
	}

	@Override
	protected void onDraw() {
		double t = (System.currentTimeMillis() / 1000.0);

		draw.setColor(0, 0, 0);
		draw.setStroke(0.1f);
		draw.rect(0, 0, 20, 20);

		inner1.translation.x = (float)Math.sin(t) * 10f;
		inner2.translation.y = (float)Math.sin(t) * 10f;

		super.onDraw();
	}
}
