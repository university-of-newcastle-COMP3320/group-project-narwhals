package TerrainGeneration;

import java.awt.*;
import java.lang.Math;

public class TerrainGeneration {
	public static void main(String[] args){
		TerrainGeneration tg = new TerrainGeneration();
		Color[][] image = tg.drawMap(tg.generateMap(808, 10, 10));
		System.out.println(image[3][2]);
		//option 1: png
		//option 2: without png
	}

	public double[][] generateMap(long seed, int width, int height) {
		double[][] map = new double[width][height];

		OpenSimplexNoise simplex = new OpenSimplexNoise(seed);

		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				map[x][y] = Math.abs(simplex.eval(x, y)); //make maths scale from 0 to 1
			}
		}
		return map;
	}

	public Color[][] drawMap(double[][] map) {
		Color image[][] = new Color[10][10];
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				image[x][y] = new Color((float) map[x][y], (float) map[x][y], (float) map[x][y]);
			}
		}
		return image;
	}
}
