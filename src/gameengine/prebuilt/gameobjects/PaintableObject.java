package gameengine.prebuilt.gameobjects;

import gameengine.graphics.GraphicsObject;
import gameengine.objects.Modifier;
import gameengine.prebuilt.objectmovement.InPlane;
import gameengine.utilities.ArgumentContext;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class PaintableObject extends GraphicsObject {
    // TODO make a class that allows a skin file to define the collidiable and the

    @Override
    public void paint(GraphicsContext gc) {

    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane.class);
            }
        };
    }

    @Override
    public ArgumentContext[] getArgumentContexts() {
        return new ArgumentContext[0];
    }
}
