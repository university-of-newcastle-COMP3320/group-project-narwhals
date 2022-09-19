package TerrainGeneration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Random;

public class TerrainGeneration {
	static double lastX = 0;
	static double lastY = 0;
	static double lastZ = 0;
	public static void main(String[] args){
		Random seed = new Random();
		TerrainGeneration tg = new TerrainGeneration();
		Color[][][] images = new Color[4][512][512];

		for(int i = 0; i<4; i++){
			images[i] = tg.drawMap(tg.generateMap(seed.nextInt(10, 20), 512, 512));
		}

		String path = "C:/Users/Tom/IdeaProjects/group-project-narwhals/ProjectNarwhal/ProjectResources/TerrainTextures/" + "heightmap" + ".png";
		BufferedImage image = new BufferedImage(images[0].length, images[0][0].length, BufferedImage.TYPE_INT_RGB);
		for(int z = 0; z < 4; z++){
			for (int x = 0; x < 512; x++) {
				for (int y = 0; y < 512; y++) {
					image.setRGB(x, y, images[z][x][y].getRGB());
				}
			}
		}

		File ImageFile = new File(path);
		try {
			ImageIO.write(image, "png", ImageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double[][] generateMap(long seed, int width, int height) {
		double[][] map = new double[width][height];
		double seedDouble = seed;

		for(int x=0; x<width; x++){
			lastX++;
			for(int y=0; y<height; y++){
				lastY++;
				map[x][y] = Math.abs(ImprovedNoise.noise(x*seedDouble/width, y*10.0/height, 0.0)); //make maths scale from 0 to 1
			}
		}
		return map;
	}

	public Color[][] drawMap(double[][] map) {
		Color image[][] = new Color[512][512];
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				image[x][y] = new Color((float) map[x][y], (float) map[x][y], (float) map[x][y]);
			}
		}
		return image;
	}
}
