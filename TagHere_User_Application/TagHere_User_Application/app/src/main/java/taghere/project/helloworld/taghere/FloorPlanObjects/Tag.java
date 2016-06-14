package taghere.project.helloworld.taghere.FloorPlanObjects;

import android.graphics.Color;
import android.graphics.Point;

import taghere.project.helloworld.taghere.R;

/**
 * Created by DongKyu on 2015-12-01.
 */
public class Tag extends FPObject {

    /** Point for position */
    private Point position;
    /** Long integer for key */
    private long key;
    /** String for extra data */
    private String data;
    private boolean isTaged = false;

    public Tag(int startPosX, int startPosY) {
        setPosition(new Point(startPosX, startPosY));
    }

    public Tag(Point startPoint) {
        setPosition(startPoint);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public Point getStartPosition() {
        return getPosition();
    }

    @Override
    public Point getEndPosition() {
        return getPosition();
    }

    @Override
    public ObjectType getType() {
        return ObjectType.TAG;
    }

    @Override
    public void setStartPosition(Point p) {
        setPosition(p);
    }

    @Override
    public void setEndPosition(Point p) {
        setPosition(p);
    }
    @Override
    public VertexType getIntersectVertex(Point p) {
        double distance = Math.sqrt(Math.pow(getPosition().x - p.x, 2) + Math.pow(getPosition().y - p.y, 2));
        if(distance <= R.dimen.vertex_radius)
            return VertexType.START;

        return null;
    }

    @Override
    public Point getIntersectPosition(VertexType t) {
        switch(t) {
            case START:
                return getStartPosition();

            case END:
                return getEndPosition();

            default:
                return null;
        }
    }

    @Override
    public Color getColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setColor(Color color) {
        // TODO Auto-generated method stub

    }


    public boolean isTaged() {
        return isTaged;
    }

    public void setTaged(boolean isTaged) {
        this.isTaged = isTaged;
    }

}