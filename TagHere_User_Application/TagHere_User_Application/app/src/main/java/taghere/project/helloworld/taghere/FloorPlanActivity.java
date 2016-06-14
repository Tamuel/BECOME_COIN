package taghere.project.helloworld.taghere;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import taghere.project.helloworld.taghere.Compass.Compass;
import taghere.project.helloworld.taghere.DataProvider.DataProvider;
import taghere.project.helloworld.taghere.FloorPlanObjects.FPObject;
import taghere.project.helloworld.taghere.FloorPlanObjects.Icon;
import taghere.project.helloworld.taghere.FloorPlanObjects.IconType;
import taghere.project.helloworld.taghere.FloorPlanObjects.Line;
import taghere.project.helloworld.taghere.FloorPlanObjects.ObjectType;
import taghere.project.helloworld.taghere.FloorPlanObjects.Oval;
import taghere.project.helloworld.taghere.FloorPlanObjects.Rectangle;
import taghere.project.helloworld.taghere.FloorPlanObjects.Tag;

public class FloorPlanActivity extends Activity{
    /** Current list of objects */
    private ArrayList<FPObject> objects;

    private int scrollOffsetX = -70;
    private int scrollOffsetY = 20;

    private int currentScrollOffsetX = 0;
    private int currentScrollOffsetY = 0;

    private double scaleOffset = 0.7;

    private Compass compass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        objects = DataProvider.getInstance().getCurrentObjects();

        CanvasView temp = new CanvasView(this);
        setContentView(temp);

        ImageView compassImage = new ImageView(this);
        compassImage.setImageResource(R.drawable.arrow_image);
        compassImage.setMaxHeight(30);
        compassImage.setMinimumHeight(30);
        compassImage.setMaxWidth(30);
        compassImage.setMinimumWidth(30);
        compassImage.setPivotX(100);
        compassImage.setPivotY(100);
        compass = new Compass(FloorPlanActivity.this, compassImage);
        compass.start();
    }

    protected class CanvasView extends View {
        public CanvasView(Context context) {
            super(context);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(null);
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            paint.setTextSize(50);
            paint.setStyle(Paint.Style.STROKE);

            for(FPObject object : objects) {
                if (object.getEndPosition() != null)
                    drawObject(
                            object,
                            canvas,
                            object.getType(),
                            object.getStartPosition(),
                            object.getEndPosition(),
                            paint
                    );
            }
        }

        /**
        * Draw object
        * Line - Draw line at start position to end position.
        * Oval - Draw oval, start position is center position and end position is radius of oval.
        * Rectangle - Draw rectangle, start position is center position and end position is size of rectangle.
        * @param object
        * @param g Graphics
        * @param type Object Type
        * @param start Start position (Point)
        * @param end End position (Point)
        * @author DongKyu
        * */
        public void drawObject(FPObject object, Canvas g, ObjectType type, Point start, Point end, Paint paint) {
            int offsetX = scrollOffsetX + currentScrollOffsetX;
            int offsetY = scrollOffsetY + currentScrollOffsetY;

            switch(type) {
                case ICON:
                    int d = 0;
                    switch(((Icon)object).getIconType()) {
                        case DOOR:
                            d = R.drawable.door_icon;
                            break;

                        case ELEVATOR:
                            d = R.drawable.elevator_icon;
                            break;

                        case STAIRS:
                            d = R.drawable.stairs_icon;
                            break;

                        case TOILET:
                            d = R.drawable.toilet_icon;
                            break;

                        case ESCALATOR_UP:
                            d = R.drawable.escalator_up_icon;
                            break;

                        case ESCALATOR_DOWN:
                            d = R.drawable.escalator_down_icon;
                            break;

                        case ESCALATOR_UP_DOWN:
                            d = R.drawable.escalator_up_down_icon;
                            break;
                    }

                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), d);
                    Rect source = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                    Rect bitmapRect = new Rect(
                            (int)((start.x + offsetX) * scaleOffset - getResources().getDimension(R.dimen.icon_radius)),
                            (int)((start.y + offsetY) * scaleOffset - getResources().getDimension(R.dimen.icon_radius)),
                            (int)((start.x + offsetX) * scaleOffset + getResources().getDimension(R.dimen.icon_radius)),
                            (int)((start.y + offsetY) * scaleOffset + getResources().getDimension(R.dimen.icon_radius))
                    );
                    g.drawBitmap(bitmap, source, bitmapRect, paint);

                    break;

                case LINE:
                    Log.i("LINE", start.x + " " + start.y + " " + end.x + " " + end.y);
                    g.drawLine(
                            (int)((start.x + offsetX) * scaleOffset),
                            (int)((start.y + offsetY) * scaleOffset),
                            (int)((end.x + offsetX) * scaleOffset),
                            (int)((end.y + offsetY) * scaleOffset),
                            paint
                    );

                    break;

                case OVAL:
                    Log.i("OVAL", start.x + " " + start.y + " " + end.x + " " + end.y);
                    g.drawOval(
                            (int)((start.x - Math.abs(end.x - start.x) + offsetX) * scaleOffset),
                            (int)((start.y - Math.abs(end.y - start.y) + offsetY) * scaleOffset),
                            (int)((start.x + Math.abs(start.x - end.x) + offsetX) * scaleOffset),
                            (int)((start.y + Math.abs(start.y - end.y) + offsetY) * scaleOffset),
                            paint
                    );

                    break;

                case RECTANGLE:
                    Log.i("RECT", start.x + " " + start.y + " " + end.x + " " + end.y);
                    g.drawRect(
                            (int)((start.x - Math.abs(end.x - start.x) + offsetX) * scaleOffset),
                            (int)((start.y - Math.abs(end.y - start.y) + offsetY) * scaleOffset),
                            (int)((start.x + Math.abs(start.x - end.x) + offsetX) * scaleOffset),
                            (int)((start.y + Math.abs(start.y - end.y) + offsetY) * scaleOffset),
                            paint
                    );

                    break;

                case TAG:
                    int d2 = 0;
                        d2 = R.drawable.tag_on_icon;

                    Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), d2);
                    Rect source2 = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                    Rect bitmapRect2 = new Rect(
                            (int)((start.x + offsetX) * scaleOffset - getResources().getDimension(R.dimen.icon_radius)),
                            (int)((start.y + offsetY) * scaleOffset - getResources().getDimension(R.dimen.icon_radius)),
                            (int)((start.x + offsetX) * scaleOffset + getResources().getDimension(R.dimen.icon_radius)),
                            (int)((start.y + offsetY) * scaleOffset + getResources().getDimension(R.dimen.icon_radius))
                    );
                    g.drawBitmap(bitmap2, source2, bitmapRect2, paint);

                    break;
            }
        }
    }
}
