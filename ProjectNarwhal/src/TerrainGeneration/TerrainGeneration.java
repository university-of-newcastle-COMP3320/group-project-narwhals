package TerrainGeneration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Random;

public class TerrainGeneration {
	public static void main(String[] args){
		Random seed = new Random();
		TerrainGeneration tg = new TerrainGeneration();
		Color[][] pixelSheet = new Color[2048][2048];

		pixelSheet = tg.drawMap(tg.generateMap(seed.nextInt(10, 20), 2048, 2048));

		String path[] = new String[16];
		for(int i = 0; i<16; i++){
			int inc = i+1;
			path[i] = "ProjectResources/TerrainTextures/" + "heightmap" + inc + ".png";
		}

		BufferedImage[] imageForPngFile = new BufferedImage[16];
		for(int i = 0; i<16; i++){
			imageForPngFile[i] = new BufferedImage (512, 512, BufferedImage.TYPE_INT_RGB);
		}

		for(int z = 0; z < 16; z++){
			for (int x = 0; x < 512; x++) {
				for (int y = 0; y < 512; y++) {
					if(z == 0){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x][y].getRGB());
					}
					else if(z == 1){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x][y + 511].getRGB());
					}
					else if(z == 2){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 511][y].getRGB());
					}
					else if(z == 3){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 511][y + 511].getRGB());
					}
					else if(z == 4){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1023][y].getRGB());
					}
					else if(z == 5){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1023][y + 511].getRGB());
					}
					else if(z == 6){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1535][y].getRGB());
					}
					else if(z == 7){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1535][y + 511].getRGB());
					}
					else if(z == 8){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x][y+ 1023].getRGB());
					}
					else if(z == 9){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x][y + 1535].getRGB());
					}
					else if(z == 10){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 511][y + 1023].getRGB());
					}
					else if(z == 11){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 511][y + 1535].getRGB());
					}
					else if(z == 12){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1023][y + 1023].getRGB());
					}
					else if(z == 13){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1023][y + 1535].getRGB());
					}
					else if(z == 14){
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1535][y + 1023].getRGB());
					}
					else{
						imageForPngFile[z].setRGB(x, y, pixelSheet[x + 1535][y + 1535].getRGB());
					}
				}
			}
		}
		File[] imageFiles = new File[16];
		for(int i = 0; i<16; i ++){
			imageFiles[i] = new File(path[i]);
		}

		try {
			for(int i = 0; i<16; i++){
				ImageIO.write(imageForPngFile[i], "png", imageFiles[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double[][] generateMap(int seed, int width, int height) {
		double[][] map = new double[width][height];
		double doubleSeed = seed;
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				map[x][y] = Math.abs(ImprovedNoise.noise(x*(doubleSeed)/width, y*(doubleSeed-5)/height, 0.0)); //make maths scale from 0 to 1
			}
		}
		return map;
	}

	public Color[][] drawMap(double[][] map) {
		Color pixelSheet[][] = new Color[2048][2048];
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[0].length; y++) {
				pixelSheet[x][y] = new Color((float) map[x][y], (float) map[x][y], (float) map[x][y]);
			}
		}
		return pixelSheet;
	}
}
