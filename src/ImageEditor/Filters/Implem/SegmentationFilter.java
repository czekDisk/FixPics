package ImageEditor.Filters.Implem;

import ImageEditor.Filters.FilterStrategy;

import java.awt.image.BufferedImage;
import java.util.Random;

public class SegmentationFilter extends FilterStrategy {

    /**
     *funkcja nakłada na image filtr segmentacji obrazu
     * @param image obraz
     */

    private final int ITERATIONS = 5;  // ile iteracji zanim kolor sie ustabilizuje
    private final int K_COLORS = 16;  // ile kolorow ma byc

    public void applyFilter(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int[][] centroids = new int[K_COLORS][3];

        // losujemy kolory poczatkowe do naszych grup
        Random rand = new Random();
        for (int i = 0; i < K_COLORS; i++) {
            int color = image.getRGB(rand.nextInt(width), rand.nextInt(height));
            centroids[i][0] = (color >> 16) & 0xFF;
            centroids[i][1] = (color >> 8) & 0xFF;
            centroids[i][2] = (color & 0xFF);
        }

        // tablica przyporzadkowan kolorow {0, 1, ..., K_COLORS-1} dla kazdego piksela
        byte[][] labels = new byte[height][width];  // byte bo int nie jest potrzebny, tych kolorow bedzie niewiele
        for (int i = 0; i < ITERATIONS; i++) {
            long[][] sumRGB = new long[K_COLORS][3];  // tu liczymy srednie kolorow R, G, B dla kazdej grupy
            int[] pixelCount = new int[K_COLORS]; // przechowuje ilosc pikseli w danej grupie

            // szukamy najblizszych kolorow ze wszystkich grup dla kazdego piksela
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);

                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    // indeks grupy najblizszej kolorem
                    int bestClusterIdx = findNearestCentroid(r, g, b, centroids);

                    labels[y][x] = (byte)bestClusterIdx;

                    // dodajemy wartosci do wyliczenia sredniego koloru
                    sumRGB[bestClusterIdx][0] += r;
                    sumRGB[bestClusterIdx][1] += g;
                    sumRGB[bestClusterIdx][2] += b;
                    pixelCount[bestClusterIdx]++;
                }
            }

            // zmieniamy kolor w naszych grupach na bardziej usredniony
            for (int k = 0; k < K_COLORS; k++) {
                if (pixelCount[k] > 0) {
                    centroids[k][0] = (int) (sumRGB[k][0] / pixelCount[k]);
                    centroids[k][1] = (int) (sumRGB[k][1] / pixelCount[k]);
                    centroids[k][2] = (int) (sumRGB[k][2] / pixelCount[k]);
                }
            }
        }

        // podmieniamy piksele w obrazie na ostateczne kolory ich grup
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int groupIndex = labels[y][x];
                int newR = centroids[groupIndex][0];
                int newG = centroids[groupIndex][1];
                int newB = centroids[groupIndex][2];

                int newColor = (newR << 16) | (newG << 8) | newB;
                image.setRGB(x, y, (image.getRGB(x, y) & 0xFF000000) | newColor);
            }
        }
    }

    // zwraca ktory z kolorow centroidow jest najblizej zadanego piksela
    private int findNearestCentroid(int r, int g, int b, int[][] centroids) {
        int bestIdx = 0;

        // pierwszy z centroidow przyjmujemy za najblizszego
        int c0r = centroids[0][0];
        int c0g = centroids[0][1];
        int c0b = centroids[0][2];
        int minDistance = (r - c0r)*(r - c0r) + (g - c0g)*(g - c0g) + (b - c0b)*(b - c0b);

        // dalej szukamy tych co sa jeszcze blizej
        for (int i = 1; i < centroids.length; i++) {
            int cr = centroids[i][0];
            int cg = centroids[i][1];
            int cb = centroids[i][2];

            // dist = (red - centroidRed)^2 + (green - centroidGreen)^2 + (blue - centroidBlue)^2
            int dist = (r - cr)*(r - cr) + (g - cg)*(g - cg) + (b - cb)*(b - cb);

            if (dist < minDistance) {
                minDistance = dist;
                bestIdx = i;
            }
        }
        return bestIdx;
    }
}