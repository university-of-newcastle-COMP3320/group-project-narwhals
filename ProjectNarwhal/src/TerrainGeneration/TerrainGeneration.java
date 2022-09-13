package TerrainGeneration;

import java.awt.*;
import java.lang.Math;

public class TerrainGeneration {
	public static void main(String[] args){
		TerrainGeneration tg = new TerrainGeneration();
		Color[][] colour = tg.drawMap(tg.generateMap(808, 10, 10));
		System.out.println(colour[3][2]);
	}

	public double[][] generateMap(long seed, int width, int height) {
		double[][] map = new double[width][height];

		OpenSimplexNoise simplex = new OpenSimplexNoise(seed);

		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				map[x][y] = Math.abs(simplex.eval(x, y));
			}
		}
		return map;
	}

	public Color[][] drawMap(double[][] map) {
		Color colour[][] = new Color[10][10];
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				colour[x][y] = new Color((float) map[x][y], (float) map[x][y], (float) map[x][y]);
			}
		}
		return colour;
	}
}
