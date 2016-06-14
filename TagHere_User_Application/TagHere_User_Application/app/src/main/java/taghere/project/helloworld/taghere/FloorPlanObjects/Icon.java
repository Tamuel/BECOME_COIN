package taghere.project.helloworld.taghere.FloorPlanObjects;

import android.graphics.Color;
import android.graphics.Point;

import taghere.project.helloworld.taghere.R;

/**
 * Created by DongKyu on 2015-12-01.
 */
public class Icon extends FPObject {

    /** Type for icon */
    private IconType iconType;
    /** Point for position */
    private Point position;
    /** String for extra data */
    private String text;

    public Icon(Point startPoint) {
        setPosition(startPoint);
    }

    public Icon(int startPosX, int startPosY) {
        setPosition(new Point(startPosX, startPosY));
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
        return ObjectType.ICON;
    }

    @Override
    public VertexType getIntersectVertex(Point p) {
        double distance = Math.sqrt(Math.pow(getPosition().x - p.x, 2) + Math.pow(getPosition().y - p.y, 2));
        if(distance <= R.dimen.vertex_radius)
            return VertexType.START;

        return null;
    }

    @Override
    public void setStartPosition(Point p) {
        this.position = p;
    }

    @Override
    public void setEndPosition(Point p) {
        this.position = p;
    }

    public IconType getIconType() {
        return iconType;
    }

    public void setIconType(IconType iconType) {
        this.iconType = iconType;
    }

    @Override
    public Point getIntersectPosition(VertexType t) {
        switch(t) {
            case START:
            case END:
                return getStartPosition();

            default:
                return null;
        }
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public void setColor(Color color) {
    }
}
