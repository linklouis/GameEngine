package gameengine.threed.graphics;

import javafx.scene.image.WritableImage;

public interface PostProcess {
    WritableImage process(WritableImage image);
}
