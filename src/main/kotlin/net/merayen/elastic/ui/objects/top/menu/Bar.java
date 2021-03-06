package net.merayen.elastic.ui.objects.top.menu;

import net.merayen.elastic.ui.Draw;
import net.merayen.elastic.ui.UIObject;

import java.util.ArrayList;

public class Bar extends UIObject {
	public ArrayList<MenuBarItem> items = new ArrayList<>();
	public float width = 500f;
	private final float height = 20f;

	public void onDraw(Draw draw) {
		float w = 1f;
		for(MenuBarItem x : items) {
			x.getTranslation().x = w;
			w += x.getLabelWidth() + 20f;
		}

		draw.setColor(100, 100, 100);
		draw.fillRect(0, 0, width, height);
		draw.setColor(130, 130, 130);
		draw.fillRect(0, height - 0.1f, width, 2f);

		super.onDraw(draw);
	}

	public void addMenuBarItem(MenuBarItem x) {
		add(x);
		items.add(x);
		x.setHandler(new MenuBarItem.Handler() {
			@Override
			public void onOpen() {
				for(MenuBarItem c : items)
					if(c != x)
						c.hideMenu();
			}
		});
	}

	public void removeMenuBarItem(MenuBarItem x) {
		remove(x);
		items.remove(x);
	}
}
