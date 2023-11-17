package gameengine.threed.graphics.raytraceing.postprocesses;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static gameengine.utilities.ExtraMath.clamp;

public class Infer  implements PostProcess {
    private static final int RANGE = 4;
    private static final double ACCEPTABLE_DIFF = 0.1;
    private static final double ACCEPTABLE_MAG_DIFF = 1;
    private static final double MAX_ACCEPTABLE_STDEV = 8;
    private static final double MIN_BRIGHTNESS = 1;
    private static final int scale = 3;

    @Override
    public WritableImage process(WritableImage image, RayTraceable[][] collisionMap) {
//        for (int i = 0; i < 5; i++) {
//            processOne(image,  collisionMap);
//        }
        processOne(image, collisionMap);
        System.out.println("done");
        return image;
    }

    private WritableImage processOne(WritableImage image, RayTraceable[][] collisionMap) {
        PixelReader reader = image.getPixelReader();
        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Vector3D color = new Vector3D(reader.getColor(x, y));
//                if (color.magnitude() > MIN_BRIGHTNESS) {
//                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
//                    continue;
//                }
                List<Pixel> pixelsLarge = new LinkedList<>();
                Vector2D pixelLoc = new Vector2D(x, y);

                for (int x1 = x - RANGE * scale; x1 < x + RANGE * scale; x1++) {
                    for (int y1 = y - RANGE * scale; y1 < y + RANGE * scale; y1++) {
                        int x2 = (int) clamp(x1, 0, image.getWidth() - 1);
                        int y2 = (int) clamp(y1, 0, image.getHeight() - 1);
                        if (collisionMap[x][y].equals(collisionMap[x2][y2]) && pixelLoc.distance(new Vector2D(x2, y2)) < RANGE * scale) {
                            pixelsLarge.add(new Pixel(collisionMap[x][y], pixelLoc.distance(new Vector2D(x2, y2)), new Vector3D(reader.getColor(x2, y2))));
                        }
                    }
                }

                double totalWeight = pixelsLarge.stream().mapToDouble(Pixel::distance).sum();
                Vector3D averageLargeColor = pixelsLarge.stream()
                        .map(Pixel::weightedColor)
                        .reduce(Vector3D::add)
                        .orElse(new Vector3D(0))
                        .scalarDivide(totalWeight);

                List<Pixel> pixels = new LinkedList<>();
                for (Pixel pixel : pixelsLarge) {
                    if (pixel.distance < RANGE && pixel.weightedColor().unitVector().distance(averageLargeColor.unitVector()) < MAX_ACCEPTABLE_STDEV) {
                        pixels.add(pixel);
                    }
                }

                totalWeight = pixels.stream().mapToDouble(Pixel::distance).sum();
                Vector3D averageColor = pixels.stream()
                        .map(Pixel::weightedColor)
                        .reduce(Vector3D::add)
                        .orElse(new Vector3D(0))
                        .scalarDivide(totalWeight);


//                if (averageColor.magnitude() >= averageLargeColor.magnitude() - MIN_BRIGHTNESS/2 &&
//                        averageColor.distance(color) > ACCEPTABLE_DIFF) {
                if (averageColor.distance(color) > ACCEPTABLE_DIFF
                        && averageColor.unitVector().distance(color.unitVector()) < ACCEPTABLE_DIFF
//                || Math.abs(averageColor.magnitude() - color.magnitude()) > ACCEPTABLE_MAG_DIFF)
                ) {
                    buffer.put((int) (x + y * image.getWidth()), averageColor.oneInt());
                } else {
                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
                }
            }
        }

        image.getPixelWriter().setPixels(0, 0,
                (int) image.getWidth(), (int) image.getHeight(),
                PixelFormat.getIntArgbInstance(),
                buffer, (int) image.getWidth());
        return image;
    }

    private record Pixel(RayTraceable collision, double distance, Vector3D color) {
        private Vector3D weightedColor() {
            return color.scalarMultiply(1.0 / distance);
        }
    }
}
