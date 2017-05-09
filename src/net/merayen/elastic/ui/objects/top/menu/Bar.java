package net.merayen.elastic.ui.objects.top.menu;

import java.util.ArrayList;

import net.merayen.elastic.ui.UIObject;
import net.merayen.elastic.ui.event.IEvent;
import net.merayen.elastic.ui.event.KeyboardEvent;

public class Bar extends UIObject {
	public ArrayList<MenuBarItem> items = new ArrayList<MenuBarItem>();
	public float width = 500f;
	private final float height = 20f;

	protected void onDraw() {
		float w = 1f;
		for(MenuBarItem x : items) {
			x.translation.x = w;
			w += x.getLabelWidth() + 20f;
		}

		draw.setColor(100, 100, 100);
		draw.fillRect(0, 0, width, height);
		draw.setColor(130, 130, 130);
		draw.fillRect(0, height - 0.1f, width, 2f);

		super.onDraw();
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

	@Override
	protected void onEvent(IEvent event) {
		if(event instanceof KeyboardEvent) {
			KeyboardEvent e = (KeyboardEvent)event;
			//System.out.println(e.action + ": " + e.character + " (" + e.code + ")");
		}
	}
}
