package original;

import java.awt.image.DataBufferByte;
import java.io.File;
import javax.imageio.ImageIO;

class ModelORIG {
	private static final float XMAX = 1200.0f - 0.0001f; //	max x pos. min is 0.
	private static final float YMAX = 600.0f - 0.0001f; //	mas y pos. min is 0.
	private ControllerORIG controller;
	public byte[] map;
	float x, y, destX, destY;

	ModelORIG(ControllerORIG c) throws Exception { 
		this.controller = c;
		map = ((DataBufferByte)(ImageIO.read(new File("terrain.png"))).getRaster().getDataBuffer()).getData();
		x = 100; y = 100; destX = 100; destY = 100;
	}

	float getSpeed(float x, float y) {	// 0 <= x < MAP_WIDTH; 0 <= y < MAP_HEIGHT.
			int xx = (int)(x * 0.1f); 
			int yy = (int)(y * 0.1f);
			if(xx >= 60) { 
				xx = 119 - xx; 
				yy = 59 - yy; 
				}
			int pos = 4 * (60 * yy + xx);
			return Math.max(0.2f, Math.min(3.5f, -0.01f * (map[pos + 1] & 0xff) + 0.02f * (map[pos + 3] & 0xff)));
	}

	ControllerORIG getController() { return controller; }
	float getX() { return x; }
	float getY() { return y; }
	float getDestX() { return destX; }
	float getDestY() { return destY; }

	void setDest(float x, float y) {
		destX = x;
		destY = y;
	}
	//public double getDistanceToDestination() { return Math.sqrt((bot.x - bot.destX) * (bot.x - bot.destX) + (bot.y - bot.destY) * (bot.y - bot.destY));}

	void update() {		// Update the agents
		float speed = getSpeed(x, y);
		float dx = destX - x;
		float dy = destY - y;
		float dist = (float)Math.sqrt(dx * dx + dy * dy);
		float t = speed / Math.max(speed, dist);
		dx *= t;
		dy *= t;
		x = Math.max(0.0f, Math.min(XMAX, x += dx));
		y = Math.max(0.0f, Math.min(YMAX, y += dy));
	}
}