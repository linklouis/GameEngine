package gameengine.threed.graphics.raytraceing;

import javafx.scene.image.WritableImage;

public interface PostProcess {
    WritableImage process(WritableImage image);
}
