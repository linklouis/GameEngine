package gameengine.threed.prebuilt;

import gameengine.threed.graphics.PostProcess;
import gameengine.vectormath.Vector3D;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Smooth implements PostProcess {
    private double contrastThreshold = 0.1;

    public Smooth(double contrastThreshold) {
        this.contrastThreshold = contrastThreshold;
    }

    @Override
    public WritableImage process(WritableImage image) {
        WritableImage newImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelWriter writer = newImage.getPixelWriter();
        PixelReader reader = image.getPixelReader();
        for (int x = 1; x < image.getWidth() - 1; x++) {
            for (int y = 1; y < image.getHeight() - 1; y++) {
                writer.setColor(x, y, getColorAt(x, y, reader));
            }
        }
        return newImage;
    }

    private Color getColorAt(int x, int y, PixelReader reader) {
        Vector3D currentColor = new Vector3D(reader.getColor(x, y));
        Vector3D sum = new Vector3D(reader.getColor(x, y));
        int num = 1;
        for (int tx = x - 1; tx < x + 1; tx++) {
            for (int ty = y - 1; ty < y + 1; ty++) {
                if (currentColor
                        .subtract(new Vector3D(reader.getColor(tx, ty)))
                        .abs()
                        .magnitude() <= contrastThreshold) {
                    sum = sum.add(new Vector3D(reader.getColor(tx, ty)));
                    num++;
                }
            }
        }
        return sum.scalarDivide(num).toColor();
    }

    public double getContrastThreshold() {
        return contrastThreshold;
    }

    public void setContrastThreshold(double contrastThreshold) {
        this.contrastThreshold = contrastThreshold;
    }
}
