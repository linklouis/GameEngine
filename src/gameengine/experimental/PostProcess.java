package gameengine.experimental;

import javafx.scene.image.WritableImage;

public interface PostProcess {
    WritableImage process(WritableImage image);
}
