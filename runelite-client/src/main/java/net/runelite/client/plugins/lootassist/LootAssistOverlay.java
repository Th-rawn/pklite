package net.runelite.client.plugins.lootassist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.text.DecimalFormat;
import java.util.HashMap;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class LootAssistOverlay extends Overlay
{


	private  LootAssistPlugin plugin;
	private Client client;
	public static HashMap<LocalPoint, LootPile> currentPiles = new HashMap<>();
	private DecimalFormat d = new DecimalFormat("##.#");

	@Inject
	public LootAssistOverlay(Client client, LootAssistPlugin plugin)
	{
		this.plugin = plugin;
		this.client = client;
		setLayer(OverlayLayer.ABOVE_SCENE);
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.MED);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		LootAssistPlugin.lootPiles.forEach((localPoint, pile) ->
		{
			int x = pile.getLocation().getSceneX();
			int y = pile.getLocation().getSceneY();
			int timeRemaining = (int) ((pile.getTimeAppearing() - System.currentTimeMillis()) / 1000);

			if (timeRemaining < 0)
			{
				LootAssistPlugin.lootPiles.remove(localPoint);
				client.clearHintArrow();
			}
			else
			{
				String nameOverlay = pile.getPlayerName();
				String timeOverlay = d.format((pile.getTimeAppearing() - System.currentTimeMillis()) / 1000f);
				if (WorldPoint.isInScene(client, WorldPoint.fromLocal(client, pile.getLocation()).getX(), WorldPoint.fromLocal(client, pile.getLocation()).getY()))
				{
					if (client.getScene().getTiles()[client.getPlane()][x][y] != null)
					{
						final Polygon poly = Perspective.getCanvasTilePoly(client,
							client.getScene().getTiles()[client.getPlane()][x][y].getLocalLocation());
						if (poly != null)
						{
							Point textLoc = Perspective.getCanvasTextLocation(client, graphics, pile.getLocation(),
								nameOverlay, graphics.getFontMetrics().getHeight() * 7);
							Point timeLoc = Perspective.getCanvasTextLocation(client, graphics, pile.getLocation(),
								timeOverlay, graphics.getFontMetrics().getHeight());
							OverlayUtil.renderPolygon(graphics, poly, Color.WHITE);
							if (timeRemaining < 5)
							{
								OverlayUtil.renderTextLocation(graphics, timeLoc, timeOverlay, Color.RED);
								OverlayUtil.renderTextLocation(graphics, textLoc, nameOverlay, Color.RED);
							}
							if (timeRemaining < 2)
							{
								client.setHintArrow(WorldPoint.fromLocal(client, pile.getLocation()));
							}
							else
							{
								OverlayUtil.renderTextLocation(graphics, timeLoc, timeOverlay, Color.WHITE);
								OverlayUtil.renderTextLocation(graphics, textLoc, nameOverlay, Color.WHITE);
							}
						}
					}
				}
			}
		});
		return null;
	}
}
