package taghere.project.helloworld.taghere.FloorPlanObjects;

import android.graphics.Color;
import android.graphics.Point;

/**
 * Created by DongKyu on 2015-12-01.
 */
public abstract class FPObject {

    abstract public Point getStartPosition();
    abstract public void setStartPosition(Point p);
    abstract public Point getEndPosition();
    abstract public void setEndPosition(Point p);
    abstract public ObjectType getType();
    abstract public VertexType getIntersectVertex(Point p);
    abstract public Point getIntersectPosition(VertexType t);
    abstract public Color getColor();
    abstract public void setColor(Color color);
}
