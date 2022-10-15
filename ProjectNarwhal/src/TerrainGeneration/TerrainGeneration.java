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

        BufferedImage imageForPngFile = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 2048; x++) {
            for (int y = 0; y < 2048; y++) {
                imageForPngFile.setRGB(x, y, pixelSheet[x][y].getRGB());
            }
        }

        File imageFile = new File("ProjectResources/TerrainTextures/heightmap.png");
        try {
            ImageIO.write(imageForPngFile, "png", imageFile);
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
