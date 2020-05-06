package functionholders;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import static java.lang.Math.*;

public class GraphicsFunctions {
	public static BufferedImage scaleImage(BufferedImage image, int scale) {
		BufferedImage after = new BufferedImage(image.getWidth() * abs(scale), image.getHeight() * abs(scale), BufferedImage.TYPE_INT_ARGB);
		AffineTransform at = new AffineTransform();
		at.scale(abs(scale), abs(scale));
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		after = scaleOp.filter(image, after);
		return(after);
	}
	
	public static BufferedImage flipImageHorizontally(BufferedImage image) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-image.getWidth(), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		
		return(op.filter(image, null));
    }
}
