package terrain.researchtools;
import terrain.researchtools.GraphicsPanel;
import java.util.Scanner;

import javax.swing.JFrame;
import terrain.SimplexNoise;


public class PixelPlotter
{
	static Scanner input = new Scanner(System.in);
	private static int frameSizeX = 400; // physical size of the frame in pixels
	private static int frameSizeY = 400; // physical size of the frame in pixels
	

	public static void main(String[] args)
	{
		SimplexNoise.setSeed(12345l);
		JFrame frame = new JFrame("Pixel Plotter");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsPanel panel = new GraphicsPanel(frameSizeX, frameSizeY);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}

}
