package original;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

class ViewORIG extends JFrame implements ActionListener {
	private ControllerORIG controller;
	private ModelORIG model;
	private MyPanel panel;

	ViewORIG(ControllerORIG c, ModelORIG m) throws Exception {
		this.controller = c;
		this.model = m;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Moving Robot");
		this.setSize(1203, 636);
		(this.panel = new MyPanel()).addMouseListener(controller);
		this.getContentPane().add(this.panel);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent evt) { repaint(); } // indirectly-> MyPanel.paintComponent

	private class MyPanel extends JPanel {
		private Image currentImg, destImg, foundImg;

		MyPanel() throws Exception {
			loadImages();
		}

		public void paintComponent(Graphics g) {
			// Give the agents a chance to make decisions
			controller.update();
			// Draw the view
			drawMap(g);
			controller.agent.drawPlan(g, model);
			drawMarkers(g);
		}
		
		private void drawMap(Graphics g) {
			byte[] map = model.map;
			int posBlue = 0;
			int posRed = (60 * 60 - 1) * 4;
			for(int y = 0; y < 60; y++) {
				for(int x = 0; x < 60; x++) {
					int bb = map[posBlue + 1] & 0xff;
					int gg = map[posBlue + 2] & 0xff;
					int rr = map[posBlue + 3] & 0xff;
					g.setColor(new Color(rr, gg, bb));
					g.fillRect(10 * x, 10 * y, 10, 10);
					posBlue += 4;
				}
				for(int x = 60; x < 120; x++) {
					int bb = map[posRed + 1] & 0xff;
					int gg = map[posRed + 2] & 0xff;
					int rr = map[posRed + 3] & 0xff;
					g.setColor(new Color(rr, gg, bb));
					g.fillRect(10 * x, 10 * y, 10, 10);
					posRed -= 4;
				}
			}
		}
		
		private void drawMarkers(Graphics g) {
			g.drawImage(currentImg, (int)model.getX() - 8, (int)model.getY() - 8, null);
			g.drawImage(destImg, (int)model.getDestX() - 8, (int)model.getDestY()- 8, null);
			if(model.getX() == model.getDestX() && model.getY() == model.getDestY())
				g.drawImage(foundImg, (int)model.getX() -8, (int)model.getY() - 8, null);
		}
		
		public void loadImages() throws IOException {
			currentImg = ImageIO.read(new File("yelX.png"));
			destImg = ImageIO.read(new File("redX.png"));
			foundImg = ImageIO.read(new File("grnX.png"));
		}
	}
}