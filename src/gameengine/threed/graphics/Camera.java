package gameengine.threed.graphics;

import gameengine.skeletons.GameObject;
import gameengine.skeletons.Modifier;
import gameengine.threed.prebuilt.objectmovement.InPlane3D;
import gameengine.twod.graphics.Visual2D;
import gameengine.vectormath.Vector3D;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Camera extends GameObject {
    private Vector3D direction;
    private WritableImage image;
    /**
     * The number of degrees in the horizontal field of view.
     */
    private double fieldOfViewDegrees;
    private boolean updateNeeded = true;
    private List<PostProcess> postProcesses = new ArrayList<>();

    /*
     * Construction:
     */

    public Camera(double x, double y, double z, Vector3D direction,
                  int imageWidth, int imageHeight, double fieldOfViewDegrees) {
        super(new InPlane3D(), new Visual2D());
        get(InPlane3D.class).instantiate(this, x, y, z);

        this.direction = direction.unitVector();
        image = new WritableImage(imageWidth, imageHeight);
        setFieldOfViewDegrees(fieldOfViewDegrees);
    }

    @Override
    public List<Class<? extends Modifier>> getDependencies() {
        return new ArrayList<>() {
            {
                add(InPlane3D.class);
            }
        };
    }


    /*
     * Functionality:
     */

    public boolean update(Visual3D[] renderableObjects) {
        if (updateNeeded) {
//            renderImage(renderableObjects);
            renderWithProcessing(renderableObjects);
            updateNeeded = false;
            return true;
        }
        return false;
    }

    public void displayOn(Pane root) {
        root.getChildren().clear();
        root.getChildren().setAll(new ImageView(image));
    }

    public void renderWithProcessing(Visual3D[] renderableObjects) {
        image = applyPostProcessing(renderImage(renderableObjects));
    }

    public abstract WritableImage renderImage(Visual3D[] renderableObjects);

    public void requestUpdate() {
        updateNeeded = true;
    }

    public void saveToFile(String fileName) throws IOException {
        String format = "png" ;
        File file = new File(fileName) ;
        // Get the directory path from the fileName
//        Path directoryPath = Paths.get(file.getParent());

        // Check if the directory exists, and if not, create it
//        if (!Files.exists(directoryPath)) {
//            Files.createDirectories(directoryPath);
//        }

        ImageIO.write(SwingFXUtils.fromFXImage(image, null), format, file);
    }

    public WritableImage applyPostProcessing(WritableImage image) {
        WritableImage currentImage = image;
        for (PostProcess process : postProcesses) {
            currentImage = process.process(image);
        }
        return currentImage;
    }

    /*
     * Utilities:
     */

    public boolean needsUpdate() {
        return updateNeeded;
    }

    public double getFieldOfViewDegrees() {
        return fieldOfViewDegrees;
    }

    public double getFieldOfViewRadians() {
        return Math.toRadians(fieldOfViewDegrees);
    }

    protected void setFieldOfView(double fieldOfViewDegrees) {
        this.fieldOfViewDegrees = fieldOfViewDegrees;
    }

    public void setFieldOfViewDegrees(double fieldOfViewDegrees) {
        setFieldOfView(fieldOfViewDegrees);
    }

    public void setFieldOfViewRadians(double fieldOfViewRadians) {
        setFieldOfView(Math.toDegrees(fieldOfViewRadians));
    }

    public Vector3D getDirection() {
        return direction;
    }

    public void setDirection(Vector3D direction) {
        this.direction = direction;
    }

    public Vector3D getLocation() {
        return get(InPlane3D.class).getLocation();
    }

    public WritableImage getImage() {
        return image;
    }

    public void setImage(WritableImage image) {
        this.image = image;
    }

    public double getWidth() {
        return getImage().getWidth();
    }

    public double getHeight() {
        return getImage().getHeight();
    }

    public List<PostProcess> getPostProcesses() {
        return postProcesses;
    }

    public void newPostProcess(PostProcess process) {
        getPostProcesses().add(process);
    }
}
