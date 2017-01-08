package net.merayen.elastic.ui.objects.top.views.nodeview.addnode;

import net.merayen.elastic.ui.event.IEvent;
import net.merayen.elastic.ui.objects.components.list.UIList;
import net.merayen.elastic.ui.objects.components.list.UIListItem;
import net.merayen.elastic.ui.objects.popupslide.PopupSlideItem;
import net.merayen.elastic.ui.util.MouseHandler;
import net.merayen.elastic.uinodes.BaseInfo;
import net.merayen.elastic.uinodes.UINodeInformation;
import net.merayen.elastic.util.Point;

class NodeListPopupSlideItem extends PopupSlideItem {
	interface Handler {
		public void onSelect(BaseInfo info);
	}

	private static class Content extends PopupSlideItem.Content {
		interface Handler {
			public void onSelect(BaseInfo info);
		}

		Handler handler;

		final UIList list = new UIList();
		final String category;

		Content(String category) {
			this.category = category;
		}

		private void setHandler(Handler handler) {
			this.handler = handler;
		}

		@Override
		protected void onInit() {
			add(list);

			list.width = 390;
			list.height = 390;

			for(BaseInfo info : UINodeInformation.getNodeInfos())
				for(String node_category : info.getCategories())
					if(node_category.equals(category))
						addNode(info);

			list.reflowVertically(4);
		}

		@Override
		protected void onDraw() {
			super.onDraw();

			draw.setColor(200, 200, 200);
			draw.setFont("", 12);
			draw.text("Will show nodes in category chosen", 20, 20);
		}

		private void addNode(BaseInfo info) {
			list.addItem(0, new UIListItem() {
				MouseHandler mouse = new MouseHandler(this);
				boolean over;

				@Override
				protected void onInit() {
					width = 100;
					height = 100;

					mouse.setHandler(new MouseHandler.Handler() {
						@Override
						public void onMouseOver() {
							over = true;
						}

						@Override
						public void onMouseOut() {
							over = false;
						}

						@Override
						public void onMouseClick(Point position) {
							handler.onSelect(info);
						}
					});
				}

				@Override
				protected void onDraw() {
					if(over)
						draw.setColor(150, 200, 150);
					else
						draw.setColor(150, 150, 150);
					draw.fillRect(0, 0, width, height);

					draw.setColor(50, 50, 50);
					draw.setStroke(2);
					draw.rect(0, 0, width, height);

					draw.setColor(0, 0, 0);
					draw.setFont("", 16);
					draw.text(info.getName(), width / 2 - draw.getTextWidth(info.getName()) / 2, 20);
				}

				@Override
				protected void onEvent(IEvent event) {
					mouse.handle(event);
				}
			});
		}
	}

	public NodeListPopupSlideItem(String category, Handler handler) {
		super(new Content(category));

		((Content)content).setHandler(new Content.Handler() {
			@Override
			public void onSelect(BaseInfo info) {
				handler.onSelect(info);
			}
		});

		width = 400;
		height = 400;

		title.text = "Select node";
	}
}
