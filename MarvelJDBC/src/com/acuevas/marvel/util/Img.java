package com.acuevas.marvel.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.acuevas.marvel.view.View;

public abstract class Img {

	public static void run() {
		JFrame frame = new JFrame();

		BufferedImage img = null;
		ImageIcon icon = null;
		JLabel label = null;

		try {
			File f = new File("resources/marstucommapXIsaacZ2.png");
			img = ImageIO.read(f);
			img = resize(img, 700, 700);
			icon = new ImageIcon(img);
			label = new JLabel(icon);
		} catch (IOException e) {
			View.printError(e.getMessage());
		}

		frame.add(label);
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

}
