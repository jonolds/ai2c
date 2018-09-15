package ai2c;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

class Model {  //max horizontal/vertical screen position. min is 0
	public static final float XMAX = 1200.0f - 0.0001f, YMAX = 600.0f - 0.0001f;

	private Controller controller;
	public byte[] terrain;
	private ArrayList<Sprite> sprites;

	Model(Controller c) { this.controller = c; }

	void initGame() throws Exception {
		BufferedImage bufImg = ImageIO.read(new File("terrain.png"));
		if(bufImg.getWidth() != 60 || bufImg.getHeight() != 60)
			throw new Exception("Expected the terrain image to have dimensions of 60-by-60");
		terrain = ((DataBufferByte)bufImg.getRaster().getDataBuffer()).getData();
		sprites = new ArrayList<>();
		sprites.add(new Sprite(100, 100));
	}

	byte[] getTerrain() { return this.terrain; }
	int[] getT2() { return this.t2; }
	ArrayList<Sprite> getSprites() { return this.sprites; }

	void update() {// Update the agents
		for(int i = 0; i < sprites.size(); i++)
			sprites.get(i).update();
	}

	// 0 <= x < MAP_WIDTH. 0 <= y < MAP_HEIGHT.
	float getTravelSpeed(float x, float y) {
		int xx = (int)(x * 0.01f);
		int yy = (int)(y * 0.01f);
//		if(xx >= 60) { xx = 119 - xx; yy = 59 - yy; }
		int pos = 4 * (11 * yy + xx);
		float speed = Math.max(0.2f, Math.min(3.5f, -0.01f * (t2[pos + 1]) + 0.02f * (t2[pos + 3])));
		return speed;
	}

	Controller getController() { return controller; }
	float getX() { return sprites.get(0).x; }
	float getY() { return sprites.get(0).y; }
	float getDestX() { return sprites.get(0).destX; }
	float getDestY() { return sprites.get(0).destY; }

	void setDest(float x, float y) {
		Sprite s = sprites.get(0);
		s.destX = x;
		s.destY = y;
	}

	double distToDest(int sprite) {
		Sprite s = sprites.get(sprite);
		return Math.sqrt((s.x - s.destX) * (s.x - s.destX) + (s.y - s.destY) * (s.y - s.destY));
	}

	class Sprite {
		float x, y, destX, destY;

		Sprite(float x, float y) { 
			this.x = x; this.y = y; this.destX = x; this.destY = y; }

		void update() {
			float speed = Model.this.getTravelSpeed(this.x, this.y);
			float dx = this.destX - this.x;
			float dy = this.destY - this.y;
			float dist = (float)Math.sqrt(dx * dx + dy * dy);
			float t = speed / Math.max(speed, dist);
			dx *= t;
			dy *= t;
			this.x += dx;
			this.y += dy;
			this.x = Math.max(0.0f, Math.min(XMAX, this.x));
			this.y = Math.max(0.0f, Math.min(YMAX, this.y));
		}
	}

	final int[] t2 = new int[]{
			/*							 	     x=1						  x=2						  x=3						 x=4						 x=5			   x=6               x=7               x=8               x=9               x=10*/
			/*y=0  */255, 145, 105, 83, /*  x=1*/255, 141, 109, 84, /*      */255, 129, 122, 87, /*  x=3*/255, 106, 143, 92, /* x=4*/255, 89, 131, 92, /*      */255, 83, 108, 89, 255, 83, 107, 89, 255, 87, 125, 92, 255, 92, 147, 96, 255, 93, 154, 97, 255, 94, 154, 98,
			/*y=1  */255, 163, 87, 78, /*   x=1*/2255, 165, 85, 77, /*      */255, 156, 94, 80, /*   x=3*/255, 96, 142, 94, /*  x=4*/255, 96, 142, 94, /*      */255, 88, 127, 93, 255, 84, 114, 90, 255, 84, 113, 90, 255, 89, 130, 95, 255, 93, 152, 100, 255, 84, 116, 90,
			/*y=2  */255, 166, 84, 77, /*   x=1*/2255, 165, 85, 77, /*      */255, 156, 94, 80, /*   x=3*/255, 156, 94, 80, /*  x=4*/255, 94, 134, 94, /*      */255, 85, 114, 91, 255, 83, 107, 89, 255, 83, 108, 89, 255, 89, 130, 97, 255, 93, 142, 102, 255, 93, 142, 101,
			/*y=3  */255, 162, 88, 78, /*   x=1*/2255, 150, 100, 81, /*     */255, 131, 119, 87, /*  x=3*/255, 107, 142, 95, /* x=4*/255, 90, 128, 95, /*      */255, 83, 107, 89, 255, 83, 107, 89, 255, 84, 110, 90, 255, 88, 121, 96, 255, 92, 135, 103, 255, 88, 123, 98,
			/*y=4  */255, 142, 109, 84, /*  x=1*/2255, 115, 135, 93, /*     */255, 103, 146, 99, /*  x=3*/255, 98, 151, 104, /* x=4*/255, 94, 142, 103, /*     */255, 95, 141, 106, 255, 95, 137, 108, 255, 94, 134, 107, 255, 92, 131, 106, 255, 94, 131, 108, 255, 86, 114, 94,
			/*y=5  */255, 111, 139, 93, /*  x=1*/2255, 96, 153, 100, /*     */255, 96, 153, 105, /*  x=3*/255, 96, 150, 107, /* x=4*/255, 98, 149, 112, /*     */255, 100, 146, 116, 255, 100, 143, 119, 255, 100, 141, 121, 255, 100, 140, 122, 255, 101, 140, 124, 255, 96, 131, 114,
			/*y=6  */255, 97, 153, 100, /*  x=1*/2255, 96, 153, 105, /*     */255, 98, 151, 107, /*  x=3*/255, 97, 146, 112, /* x=4*/255, 99, 144, 116, /*  x=5*/255, 101, 142, 122, 255, 102, 141, 125, 255, 103, 139, 127, 255, 103, 137, 129, 255, 104, 137, 130, 255, 104, 138, 130};
}