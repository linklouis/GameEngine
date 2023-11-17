package gameengine.threed.graphics.raytraceing.postprocesses;

import gameengine.threed.graphics.raytraceing.objectgraphics.RayTraceable;
import javafx.scene.image.WritableImage;

public interface PostProcess {
    WritableImage process(WritableImage image, RayTraceable[][] collisionMap);
}
