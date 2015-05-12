package terrain.researchtools;

import java.awt.*;

import javax.swing.JPanel;
import terrain.SimplexNoise;

public class GraphicsPanel extends JPanel
{
	private int sizeX;
	private int sizeY;
	private double scale = .002;
	
	public GraphicsPanel(int sizeX, int sizeY)
	{
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		setPreferredSize(new Dimension(sizeX, sizeY));
	}
	
	
	private double sumOctave(int num_iterations, double x, double y, double persistence, double scale)
	{
	    double maxAmp = 0;
	    double amp = 1;
	    double noise = 0;

	    //#add successively smaller, higher-frequency terms
	    for(int i = 0; i < num_iterations; ++i)
	    {
	    	noise += SimplexNoise.noise2D(x, y,   scale) * amp;
			maxAmp += amp;
			amp *= persistence;
			scale *= 2;
	    }

	    //take the average value of the iterations
	    noise /= maxAmp;

	    return noise;	
	}
	
	public double normalize(double value, double low, double high)
	{
	    return value * (high - low) / 2 + (high + low) / 2;
	}
	
	public void paintComponent(Graphics g)
	{
		Double big = Double.MIN_VALUE;
		Double small = Double.MAX_VALUE;

		for(double x=0; x<sizeX; x++)
		{
			for(double y=0; y<sizeY; y++)
			{
					Double c = sumOctave(8, x, y, .5, scale);
					//Double c = SimplexNoise.noise3D(x, y, z, scale);
					
					//c = (c * 4) + 4;
					//c = normalize(c, 0, 255);
					
					if(c< small)
					{
						small = c;
					}
					if(c> big)
					{
						big = c;
					}
					
					g.setColor(Color.getHSBColor(c.floatValue() , .99f, .99f));
					int x1 = new Double(x).intValue();
					int y1 = new Double(y).intValue();
					g.drawLine(x1, y1, x1, y1);
			}
		}
		System.out.println(small + " to " + big);
	}	
}
