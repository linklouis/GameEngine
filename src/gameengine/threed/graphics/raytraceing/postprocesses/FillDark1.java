package gameengine.threed.graphics.raytraceing.postprocesses;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector2D;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

import static gameengine.utilities.ExtraMath.clamp;

public class FillDark1 implements PostProcess {
    private static final int RANGE = 2;
    private static final double ACCEPTABLE_DIFF = 1;
    private static final double MAX_ACCEPTABLE_STDEV = 5;
    private static final double MAX_SMOOTHING_STDEV = 2;
    private static final double MIN_BRIGHTNESS = 0.5;
    private static final int scale = 5;

    @Override
    public WritableImage process(WritableImage image, RayTraceable[][] collisionMap) {
//        for (int i = 0; i < 5; i++) {
////            smooth(image, collisionMap);
//            processOne(image,  collisionMap);
//        }
//        smooth(image, collisionMap);
        processOne(image, collisionMap);
//        smooth(image, collisionMap);
        System.out.println("done");
        return image;
    }

    private WritableImage processOne(WritableImage image, RayTraceable[][] collisionMap) {
        PixelReader reader = image.getPixelReader();
        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Vector3D color = new Vector3D(reader.getColor(x, y));
                if (color.magnitude() > MIN_BRIGHTNESS) {
                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
                    continue;
                }
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

                List<Pixel> pixels = new LinkedList<>();
                for (Pixel pixel : pixelsLarge) {
                    if (pixel.distance < RANGE) {
                        pixels.add(pixel);
                    }
                }

                double totalWeight = pixelsLarge.stream().mapToDouble(Pixel::distance).sum();
                Vector3D averageLargeColor = pixelsLarge.stream()
                        .map(Pixel::weightedColor)
                        .reduce(Vector3D::add)
                        .orElse(new Vector3D(0))
                        .scalarDivide(totalWeight);

                totalWeight = pixels.stream().mapToDouble(Pixel::distance).sum();
                Vector3D averageColor = pixels.stream()
                        .map(Pixel::weightedColor)
                        .reduce(Vector3D::add)
                        .orElse(new Vector3D(0))
                        .scalarDivide(totalWeight);

                if (averageLargeColor.magnitude() >= MIN_BRIGHTNESS && averageColor.magnitude() >= MIN_BRIGHTNESS &&
                        /*averageLargeColor.distance(averageColor) < MAX_ACCEPTABLE_STDEV
                        && */averageColor.distance(color) > ACCEPTABLE_DIFF) {
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

    private WritableImage smooth(WritableImage image, RayTraceable[][] collisionMap) {
        PixelReader reader = image.getPixelReader();
        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int numa = 0;
                Vector3D averageColor = new Vector3D();
                for (int x1 = x - RANGE; x1 < x + RANGE; x1++) {
                    for (int y1 = y - RANGE; y1 < y + RANGE; y1++) {
                        int x2 = (int) clamp(x1, 0, image.getWidth() - 1);
                        int y2 = (int) clamp(y1, 0, image.getHeight() - 1);
                        if (collisionMap[x][y].equals(collisionMap[x2][y2])) {
                            averageColor = averageColor.add(reader.getColor(x2, y2));
                            numa++;
                        }
                    }
                }
                averageColor = averageColor.subtract(new Vector3D(reader.getColor(x, y))).scalarDivide(numa - 1);

                int num = 0;
                Vector3D correctedAverage = new Vector3D();
                for (int x1 = x - RANGE; x1 < x + RANGE; x1++) {
                    for (int y1 = y - RANGE; y1 < y + RANGE; y1++) {
                        int x2 = (int) clamp(x1, 0, image.getWidth() - 1);
                        int y2 = (int) clamp(y1, 0, image.getHeight() - 1);
                        if (collisionMap[x][y].equals(collisionMap[x2][y2])
                                && averageColor.distance(reader.getColor(x2, y2)) < MAX_SMOOTHING_STDEV
                                && Math.abs(averageColor.magnitude() - new Vector3D(reader.getColor(x2, y2)).magnitude()) < MAX_SMOOTHING_STDEV) {
                            correctedAverage = correctedAverage.add(reader.getColor(x2, y2));
                            num++;
                        }
                    }
                }

                if (num > 3) {
                    correctedAverage = correctedAverage.scalarDivide(num);
                    buffer.put((int) (x + y * image.getWidth()), correctedAverage.oneInt());
                } else {
                    buffer.put((int) (x + y * image.getWidth()), Vector3D.oneInt(reader.getColor(x, y)));
                }
            }
        }

        image.getPixelWriter().setPixels(0, 0,
                (int) image.getWidth(), (int) image.getHeight(),
                PixelFormat.getIntArgbInstance(),
                buffer, (int) image.getWidth());
        return image;
    }

    private WritableImage smootha(WritableImage image, RayTraceable[][] collisionMap) {
        PixelReader reader = image.getPixelReader();
        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int num = 0;
                Vector3D averageColor = new Vector3D();
                for (int x1 = x - RANGE; x1 < x + RANGE; x1++) {
                    for (int y1 = y - RANGE; y1 < y + RANGE; y1++) {
                        int x2 = (int) clamp(x1, 0, image.getWidth() - 1);
                        int y2 = (int) clamp(y1, 0, image.getHeight() - 1);
                        if (collisionMap[x][y].equals(collisionMap[x2][y2])) {
                            averageColor = averageColor.add(reader.getColor(x2, y2));
                            num++;
                        }
                    }
                }
                buffer.put((int) (x + y * image.getWidth()),
                        averageColor.scalarDivide(num).oneInt());
            }
        }

        image.getPixelWriter().setPixels(0, 0,
                (int) image.getWidth(), (int) image.getHeight(),
                PixelFormat.getIntArgbInstance(),
                buffer, (int) image.getWidth());
        return image;
    }

//    private WritableImage processOne(WritableImage image, RayTraceable[][] collisionMap) {
//        PixelReader reader = image.getPixelReader();
//        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));
//
//        for (int x = RANGE; x < image.getWidth() - RANGE; x++) {
//            for (int y = RANGE; y < image.getHeight() - RANGE; y++) {
//                Vector3D color = new Vector3D(reader.getColor(x, y));
//                if (color.magnitude() > 0.5) {
//                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
//                    continue;
//                }
//
//                Vector3D averageColor = new Vector3D();
//                for (int x1 = x - RANGE; x1 < x + RANGE; x1++) {
//                    for (int y1 = y - RANGE; y1 < y + RANGE; y1++) {
//                        if (collisionMap[x][y].equals(collisionMap[x1][y1])) {
//                            averageColor = averageColor.add(reader.getColor(x1, y1));
//                        }
//                    }
//                }
//
//                int num = 0;
//                Vector3D averageCorrectedColor = new Vector3D();
//                for (int x1 = x - RANGE; x1 < x + RANGE; x1++) {
//                    for (int y1 = y - RANGE; y1 < y + RANGE; y1++) {
//                        if (collisionMap[x][y].equals(collisionMap[x1][y1])
//                                && averageColor.distance(reader.getColor(x1, y1)) < MAX_ACCEPTABLE_STDEV) {
//                            averageCorrectedColor = averageCorrectedColor.add(reader.getColor(x1, y1));
//                            num++;
//                        }
//
//                    }
//                }
//                /*if (collisionMap[x][y].equals(collisionMap[x - 1][y])) {
//                    averageCorrectedColor = averageCorrectedColor.add(reader.getColor(x - 1, y));
//                    num++;
//                }
//                if (collisionMap[x][y].equals(collisionMap[x + 1][y])) {
//                    averageCorrectedColor = averageCorrectedColor.add(reader.getColor(x + 1, y));
//                    num++;
//                }
//                if (collisionMap[x][y].equals(collisionMap[x][y - 1])) {
//                    averageCorrectedColor = averageCorrectedColor.add(reader.getColor(x, y - 1));
//                    num++;
//                }
//                if (collisionMap[x][y].equals(collisionMap[x][y + 1])) {
//                    averageCorrectedColor = averageCorrectedColor.add(reader.getColor(x, y + 1));
//                    num++;
//                } else */if (num == 0) {
//                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
//                    continue;
//                }
////                System.out.println("b");
//                averageCorrectedColor = averageCorrectedColor.scalarDivide(num);
//                if (averageCorrectedColor.distance(color) <= ACCEPTABLE_DIFF) {
//                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
//                    continue;
//                }
//
////                System.out.println("a");
//                color = averageCorrectedColor.scalarDivide(num);
//                buffer.put((int) (x + y * image.getWidth()), color.oneInt());
//            }
//        }
//
//        image.getPixelWriter().setPixels(0, 0,
//                (int) image.getWidth(), (int) image.getHeight(),
//                PixelFormat.getIntArgbInstance(),
//                buffer, (int) image.getWidth());
//        return image;
//    }

    private Vector3D addIfValid(Vector3D original, Color toAdd) {
        if (original.distance(toAdd) < MAX_ACCEPTABLE_STDEV) {
            return original.add(toAdd);
        }
        return original;
    }
}
