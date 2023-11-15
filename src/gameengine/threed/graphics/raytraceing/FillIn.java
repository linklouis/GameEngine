package gameengine.threed.graphics.raytraceing;

import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.nio.IntBuffer;
import java.util.stream.IntStream;

public class FillIn implements PostProcess {
    private static final int RANGE = 4;
    private static final double ACCEPTABLE_DIFF = 8;
    private static final double MAX_ACCEPTABLE_STDEV = 8;

    @Override
//    public WritableImage process(WritableImage image) {
//        PixelReader reader = image.getPixelReader();
//        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));
//
//        IntStream.range(RANGE, (int) (image.getWidth() - RANGE)).parallel().forEach(x ->
//                IntStream.range(RANGE, (int) (image.getHeight() - RANGE)).forEach(y ->
//                        {
//                            Vector3D color = new Vector3D();
//                            IntStream.range(x - RANGE, x + RANGE).forEach(x1 ->
//                                    IntStream.range(y - RANGE, y + RANGE).forEach(y1 ->
//                                            {
//                                                color.addMutable(new Vector3D(reader.getColor(x1, y1)));
//                                            }
//                                    ));
//
//                            color.scalarDivide(RANGE * RANGE * 4);
//                            buffer.put((int) (x + y * image.getWidth()), color.oneInt());
//                        }
//                ));
////        IntStream.range(RANGE, (int) (image.getWidth() - RANGE)).parallel().forEach(x ->
////                IntStream.range(RANGE, (int) (image.getHeight() - RANGE)).forEach(y ->
////                        {
////                            buffer.put((int) (x + y * image.getWidth()), Vector3D.oneInt(reader.getColor(x, y)));
////                        }
////                ));
//
//        image.getPixelWriter().setPixels(0, 0,
//                (int) image.getWidth(), (int) image.getHeight(),
//                PixelFormat.getIntArgbInstance(),
//                buffer, (int) image.getWidth());
//        return image;
//    }
    public WritableImage process(WritableImage image) {
        PixelReader reader = image.getPixelReader();
        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));

        IntStream.range(RANGE, (int) (image.getWidth() - RANGE)).parallel().forEach(x ->
                IntStream.range(RANGE, (int) (image.getHeight() - RANGE)).forEach(y ->
                        {
                            Vector3D color = new Vector3D();
                            IntStream.range(x - RANGE, x + RANGE).forEach(x1 ->
                                    IntStream.range(y - RANGE, y + RANGE).forEach(y1 ->
                                        color.addMutable(new Vector3D(reader.getColor(x1, y1)))
                                    ));

                            // Step 2: Calculate sum of squared differences
                            double sumSquaredDiff = IntStream.range(x - RANGE, x + RANGE).mapToDouble(x1 ->
                                    IntStream.range(y - RANGE, y + RANGE).mapToDouble(y1 -> {
                                        Vector3D diff = new Vector3D(reader.getColor(x1, y1)).subtract(color);
                                        return diff.dotProduct(diff);
                                    }).sum()
                            ).sum();

                            // Step 3: Calculate variance
                            double variance = sumSquaredDiff / (RANGE * RANGE * 4 * RANGE * RANGE * 4 - 1);

                            // Step 4: Calculate standard deviation
                            double standardDeviation = Math.sqrt(variance);

                            if (/*color.subtract(new Vector3D(reader.getColor(x, y)))
                                    .scalarDivide(RANGE * RANGE * 4 - 1).subtract(new Vector3D(reader.getColor(x, y)))
                                    .magnitude() > ACCEPTABLE_DIFF
                                    && */standardDeviation < MAX_ACCEPTABLE_STDEV) {
                                color.subtract(new Vector3D(reader.getColor(x, y))).scalarDivide(RANGE * RANGE * 4 - 1);
                                buffer.put((int) (x + y * image.getWidth()), color.oneInt());
                            } else {
//                                color.scalarDivide(RANGE * RANGE * 4);
//                                buffer.put((int) (x + y * image.getWidth()), color.oneInt());
                                buffer.put((int) (x + y * image.getWidth()), new Vector3D(reader.getColor(x, y)).oneInt());
                            }
                        }
                ));

        image.getPixelWriter().setPixels(0, 0,
                (int) image.getWidth(), (int) image.getHeight(),
                PixelFormat.getIntArgbInstance(),
                buffer, (int) image.getWidth());
        return image;
    }


//    public WritableImage process(WritableImage image) {
//        PixelReader reader = image.getPixelReader();
//        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
//        IntBuffer buffer = IntBuffer.allocate((int) (image.getWidth() * image.getHeight() * Integer.BYTES));
//
//        IntStream.range(RANGE, (int) (image.getWidth() - RANGE)).parallel().forEach(x ->
//                IntStream.range(RANGE, (int) (image.getHeight() - RANGE)).forEach(y ->
//                        {
//                            Vector3D color = new Vector3D(reader.getColor(x, y));
//                            IntStream.range(x - RANGE, x + RANGE).forEach(x1 ->
//                                    IntStream.range(y - RANGE, y + RANGE).forEach(y1 ->
//                                                    color.add(new Vector3D(reader.getColor(x1, y1)))
//                                    ));
//                            color.scalarDivide(RANGE * RANGE);
//                            buffer.put((int) (x + y * image.getWidth()), color.oneInt());
//                        }
//                ));
//
//        newImage.getPixelWriter().setPixels(0, 0,
//                (int) image.getWidth(), (int) image.getHeight(),
//                PixelFormat.getIntArgbInstance(),
//                buffer, (int) image.getWidth());
//        return newImage;
//    }
}
