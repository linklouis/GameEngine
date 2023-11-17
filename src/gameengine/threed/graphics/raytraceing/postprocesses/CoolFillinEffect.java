package gameengine.threed.graphics.raytraceing.postprocesses;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.nio.IntBuffer;

public class CoolFillinEffect implements PostProcess {
    private static final int RANGE = 2;
    private static final double ACCEPTABLE_DIFF = 1.4;
    private static final double MAX_ACCEPTABLE_STDEV = 10;

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

        for (int x = RANGE * 2; x < image.getWidth() - RANGE * 2; x++) {
            for (int y = RANGE * 2; y < image.getHeight() - RANGE * 2; y++) {
                Vector3D color = new Vector3D(reader.getColor(x, y));
//                if (color.magnitude() > 0.5) {
//                    buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
//                    continue;
//                }

                int num = 0;
                Vector3D averageColor = new Vector3D();
                for (int x1 = x - RANGE; x1 < x + RANGE; x1++) {
                    for (int y1 = y - RANGE; y1 < y + RANGE; y1++) {
                        if (collisionMap[x][y].equals(collisionMap[x1][y1])) {
                            averageColor = averageColor.add(reader.getColor(x1, y1));
                            num++;
                        }
                    }
                }
                averageColor = averageColor.scalarDivide(num);

                int numLarge = 0;
                Vector3D averageLargeColor = new Vector3D();
                for (int x1 = x - RANGE * 2; x1 < x + RANGE * 2; x1++) {
                    for (int y1 = y - RANGE * 2; y1 < y + RANGE * 2; y1++) {
                        if (collisionMap[x][y].equals(collisionMap[x1][y1])) {
                            averageLargeColor = averageLargeColor.add(reader.getColor(x1, y1));
                            numLarge++;
                        }
                    }
                }
                averageLargeColor = averageLargeColor.scalarDivide(numLarge);

                if (averageLargeColor.distance(averageColor) < MAX_ACCEPTABLE_STDEV
                        && averageLargeColor.distance(color) > ACCEPTABLE_DIFF) {
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
