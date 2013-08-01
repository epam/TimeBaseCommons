package deltix.util.swing.slider;

import com.jidesoft.plaf.basic.BasicRangeSliderUI;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * User: TurskiyS
 * Date: 4/8/13
 */
public class DrawnSliderUI extends BasicRangeSliderUI{

    protected static final int MOUSE_HANDLE_BEFORE_MIN   = 5;
    protected static final int MOUSE_HANDLE_BEFORMIN_MIN = 6;
    protected static final int MOUSE_HANDLE_MIN_MAX      = 7;

    private DrawnSlider slider;

    public DrawnSliderUI(DrawnSlider slider) {
        super(slider);
        this.slider = slider;
    }


    @Override
    public void paintThumb(Graphics g) {
        if (slider.getOptions().isOverrideThumb()) {

        } else {
            //super.paintThumb(g);
        }
    }

    @Override
    public void paintTrack(Graphics g) {
        if (slider!= null) {
            int beforeMin = slider.getThirdValue();
            int low = slider.getLowValue();
            int high = slider.getHighValue();
            int min = slider.getMinimum();
            int max = slider.getMaximum();

            int borderWidth = (int) SliderOptions.STROKE_WIDTH;
            int y= SliderOptions.Y_START_COORDINATE;
            int x1 = borderWidth, x2 = borderWidth, x3;

            Rectangle bounds = slider.getBounds();

            Font defFont = g.getFont();
            Font customFont = new Font(defFont.getFontName(), Font.BOLD, defFont.getSize());
            g.setFont(customFont);

            if (beforeMin > 0 && low != 0 && slider.getOptions().isVisibile(0)) {
                drawArea(
                        g,
                        customFont,
                        new Rectangle(
                                borderWidth,
                                y,
                                x1 = xPositionForValue(beforeMin) - borderWidth,
                                bounds.height - 2 * borderWidth),
                        0
                );
            }

            if (min != low) {
                drawArea(
                        g,
                        customFont,
                        new Rectangle(
                                x1,
                                y,
                                x2 = xPositionForValue(low) - borderWidth +
                                        (beforeMin == 0 && high == max ? SliderOptions.DRAWN_THUMB_SIZE_PX : 0),
                                bounds.height - 2 * borderWidth),
                        1
                );
            }

            if (low != max) {
                drawArea(
                        g,
                        customFont,
                        new Rectangle(
                                x2,
                                y,
                                x3 = xPositionForValue(high) - borderWidth +
                                        (low == 0 && high == max ? SliderOptions.DRAWN_THUMB_SIZE_PX : 0),
                                bounds.height - 2 * borderWidth),
                        2
                );
            } else {
                x3 = xPositionForValue(max);
            }

            if (high < max && slider.getOptions().isVisibile(2)) {
                drawArea(
                        g,
                        customFont,
                        new Rectangle(
                                x3,
                                y,
                                xPositionForValue(max),
                                bounds.height - 2 * borderWidth),
                        3
                );
            }
        }
    }

    private void drawArea(Graphics g, Font font, Rectangle rect, int areaIndex){
        g.setColor(slider.getOptions().getColor(areaIndex));
        int x1;
        g.fillRect((int) rect.getX(),
                (int) rect.getY(),
                x1 = (int) rect.getWidth(),
                (int) rect.getHeight()
        );

        String drawnString;
        int drawnStringWidth = g.getFontMetrics(font).stringWidth(
                drawnString = slider.getOptions().getLabel(
                        areaIndex,
                        (int) (x1  - rect.getX() - SliderOptions.DRAWN_THUMB_SIZE_PX),
                        g.getFontMetrics(font)
                )
        );
        g.setColor(slider.getOptions().getTextColor(areaIndex));
        g.drawString(
                drawnString,
                (int) (rect.getX() +
                        SliderOptions.DRAWN_THUMB_SIZE_PX +
                        (x1 - rect.getX() - SliderOptions.DRAWN_THUMB_SIZE_PX  - drawnStringWidth) / 2),
                slider.getBounds().height * 2 / 3);
    }

    @Override
    protected TrackListener createTrackListener(JSlider slider) {
        return new RangeTrackListener();
    }

    @Override
    protected int getMouseHandle(int x, int y) {
        Rectangle rect = trackRect;

        int beforeMin = slider.getThirdValue();
        int low = slider.getLowValue();
        int high = slider.getHighValue();

        int results = 0;
        int[][] matchedRects = new int[][]{{-1, -1, -1}, {-1, -1, -1}};

        if (slider.getOrientation() == JSlider.VERTICAL) {
            int minBeforeY = yPositionForValue(beforeMin);
            int minY = yPositionForValue(low);
            int maxY = yPositionForValue(high);



            Rectangle minBeforeRect = new Rectangle(
                    rect.x,
                    minBeforeY -5,
                    rect.width,
                    SliderOptions.DRAWN_THUMB_SIZE_PX);

            if (minBeforeRect.contains(x, y)){
                matchedRects[0][0] = MOUSE_HANDLE_BEFORE_MIN;
                matchedRects[1][0] = beforeMin;
                results ++;
            }

            Rectangle minRect = new Rectangle(
                    rect.x,
                    minY - 3,
                    rect.width,
                    SliderOptions.DRAWN_THUMB_SIZE_PX);
            if (minRect.contains(x, y)) {
                matchedRects[0][1] =  MOUSE_HANDLE_MIN;
                matchedRects[1][1] = low;
                results ++;
            }

            Rectangle maxRect = new Rectangle(rect.x,
                    maxY - 1,
                    rect.width,
                    SliderOptions.DRAWN_THUMB_SIZE_PX);
            if (maxRect.contains(x, y)) {
                matchedRects[0][2] = MOUSE_HANDLE_MAX;
                matchedRects[1][2] = high;
                results++;
            }
        } else {
            int minBeforeX = xPositionForValue(beforeMin);
            int minX = xPositionForValue(low);
            int maxX = xPositionForValue(high);


            Rectangle minBeforeRect = new Rectangle(
                    minBeforeX - SliderOptions.DRAWN_THUMB_SIZE_PX,
                    rect.y,
                    SliderOptions.DRAWN_THUMB_SIZE_PX,
                    rect.height
            );

            if (minBeforeRect.contains(x, y)){
                matchedRects[0][0] = MOUSE_HANDLE_BEFORE_MIN;
                matchedRects[1][0] = beforeMin;
                results ++;
            }


            Rectangle minRect = new Rectangle(
                    minX - SliderOptions.DRAWN_THUMB_SIZE_PX / 2,
                    rect.y,
                    SliderOptions.DRAWN_THUMB_SIZE_PX,
                    rect.height
            );

            if (minRect.contains(x, y)) {
                matchedRects[0][1] =  MOUSE_HANDLE_MIN;
                matchedRects[1][1] = low;
                results ++;
            }


            Rectangle maxRect = new Rectangle(
                    maxX,
                    rect.y,
                    SliderOptions.DRAWN_THUMB_SIZE_PX,
                    rect.height
            );
            if (maxRect.contains(x, y)) {
                matchedRects[0][2] = MOUSE_HANDLE_MAX;
                matchedRects[1][2] = high;
                results++;
            }
        }

        switch (results) {
            case 0:
                return MOUSE_HANDLE_NONE;
            case 1:
                for (int matchedRect : matchedRects[0]) {
                    if (matchedRect != -1) {
                        return matchedRect;
                    }
                }
                break;
            case 2:
                if (matchedRects[1][0] != -1 && matchedRects[1][1] != -1){//beforeMin and Min are in one start position
                    return MOUSE_HANDLE_BEFORMIN_MIN;
                }else if (matchedRects[1][1] != -1 && matchedRects[1][2] != -1){//Min  and Max are in one start position
                    return MOUSE_HANDLE_MIN_MAX;
                }
                for (int matchedRect : matchedRects[0]) {
                    if (matchedRect != -1) {
                        return matchedRect;
                    }
                }
            case 3:
                int sum = 0;
                for (int matchedRect : matchedRects[1]) {
                    sum += matchedRect;
                }
                if (sum == 0){
                    return MOUSE_HANDLE_MAX;
                }
                for (int matchedRect : matchedRects[0]) {
                    if (matchedRect != -1) {
                        return matchedRect;
                    }
                }
        }

        return MOUSE_HANDLE_NONE;
    }

    private void offset(int delta) {
        slider.getModel().setValue(slider.getLowValue() + delta);
    }

    public int getXLocation(int value) {
        return xPositionForValue(value);
    }

    protected class RangeTrackListener extends TrackListener {
        int handle;
        int handleOffset;
        int mouseStartLocation;

        public RangeTrackListener() {

        }

        /**
         * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }

            handle = changeHandleAccordingToVisibility(getMouseHandle(e.getX(), e.getY()));

            handleOffset = (slider.getOrientation() == JSlider.VERTICAL) ?
                    e.getY() - yPositionForValue(slider.getLowValue()) :
                    e.getX() - xPositionForValue(slider.getLowValue());

            mouseStartLocation = (slider.getOrientation() == JSlider.VERTICAL) ? e.getY() : e.getX();

            slider.getModel().setValueIsAdjusting(true);
        }

        /**
         * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }

            int newLocation = (slider.getOrientation() == JSlider.VERTICAL) ? e.getY() : e.getX();

            int newValue = (slider.getOrientation() == JSlider.VERTICAL) ? valueForYPosition(newLocation) : valueForXPosition(newLocation);

            if (newValue < slider.getModel().getMinimum()) {
                newValue = slider.getModel().getMinimum();
            }

            if (newValue > slider.getModel().getMaximum()) {
                newValue = slider.getModel().getMaximum();
            }

            boolean rightDragDirection = newLocation - mouseStartLocation > 0;
            boolean leftDragDirection = newLocation - mouseStartLocation < 0;

            if (handle == (MOUSE_HANDLE_MIN | MOUSE_HANDLE_MAX)) {
                if (rightDragDirection) {
                    handle = MOUSE_HANDLE_MAX;
                } else if (leftDragDirection) {
                    handle = MOUSE_HANDLE_MIN;
                } else {
                    return;
                }
            }

            switch (handle){
                case MOUSE_HANDLE_BEFORMIN_MIN:
                    if (rightDragDirection) {
                        handle = MOUSE_HANDLE_MIN;
                    } else if (leftDragDirection) {
                        handle = MOUSE_HANDLE_BEFORE_MIN;
                    }
                    break;
                case MOUSE_HANDLE_MIN_MAX:
                    if (rightDragDirection) {
                        handle = MOUSE_HANDLE_MAX;
                    } else if (leftDragDirection) {
                        handle = MOUSE_HANDLE_MIN;
                    }
            }

            processMouseHandlerWithSliding(newValue, newLocation);

        }

        private void processMouseHandlerWithStopping(int newValue, int newLocation){
            int beforeMin = slider.getThirdValue();
            SliderScale scale = SliderScale.getInstance(slider.getMaximum());
            switch (handle) {
                case MOUSE_HANDLE_BEFORE_MIN:
                    slider.setThirdValue(
                            Math.min (
                                    Math.min(newValue, slider.getLowValue()),
                                    scale.multiplyValue(
                                            slider.getHighValue(),
                                            SliderOptions.PART_OF_HEAP_CACHE )
                            )
                    );
                    ChangeEvent ce = new ChangeEvent(slider);
                    for(ChangeListener cl : slider.getChangeListeners()){
                        cl.stateChanged(ce);
                    }
                    break;
                case MOUSE_HANDLE_MIN:
                    slider.setLowValue(beforeMin == 0 ?
                            Math.min(newValue, slider.getHighValue()) :
                            Math.max(Math.min(newValue, slider.getHighValue()), beforeMin));
                    break;
                case MOUSE_HANDLE_MAX:
                    slider.setHighValue(
                            Math.max (
                                    Math.max(slider.getLowValue(), newValue),
                                    scale.multiplyValue(
                                            beforeMin,
                                            1/SliderOptions.PART_OF_HEAP_CACHE )
                            )

                    );
                    break;
                case MOUSE_HANDLE_MIDDLE:
                    int delta = (slider.getOrientation() == JSlider.VERTICAL) ?
                            valueForYPosition(newLocation - handleOffset) - slider.getLowValue() :
                            valueForXPosition(newLocation - handleOffset) - slider.getLowValue();
                    if ((delta < 0) && ((slider.getLowValue() + delta) < slider.getMinimum())) {
                        delta = slider.getMinimum() - slider.getLowValue();
                    }

                    if ((delta > 0) && ((slider.getHighValue() + delta) > slider.getMaximum())) {
                        delta = slider.getMaximum() - slider.getHighValue();
                    }

                    if (delta != 0) {
                        offset(delta);
                    }
                    break;
            }
        }


        private void processMouseHandlerWithSliding(int newValue, int newLocation){
            int beforeMin = slider.getThirdValue();
            SliderScale scale = SliderScale.getInstance(slider.getMaximum());
            switch (handle) {
                case MOUSE_HANDLE_BEFORE_MIN:
                    int borderValue = Math.min(newValue,
                            scale.multiplyValue(
                                    slider.getHighValue(),
                                    SliderOptions.PART_OF_HEAP_CACHE)
                    );
                    slider.setThirdValue(borderValue);
                    if (borderValue > slider.getLowValue()){
                        slider.setLowValue(borderValue);
                    }
                    ChangeEvent ce = new ChangeEvent(slider);
                    for(ChangeListener cl : slider.getChangeListeners()){
                        cl.stateChanged(ce);
                    }
                    break;
                case MOUSE_HANDLE_MIN:
                    slider.setLowValue(newValue);
                    if (newValue < beforeMin){
                        slider.setThirdValue(newValue);
                    }
                    if (newValue > slider.getHighValue()){
                        slider.setHighValue(newValue);
                    }
                    break;
                case MOUSE_HANDLE_MAX:
                    borderValue = Math.max(newValue,
                            scale.multiplyValue(
                                    beforeMin,
                                    1 / SliderOptions.PART_OF_HEAP_CACHE)
                    );
                    slider.setHighValue(borderValue);

                    if (borderValue < slider.getLowValue()){
                        slider.setLowValue(borderValue);
                    }
                    break;
                case MOUSE_HANDLE_MIDDLE:
                    int delta = (slider.getOrientation() == JSlider.VERTICAL) ?
                            valueForYPosition(newLocation - handleOffset) - slider.getLowValue() :
                            valueForXPosition(newLocation - handleOffset) - slider.getLowValue();
                    if ((delta < 0) && ((slider.getLowValue() + delta) < slider.getMinimum())) {
                        delta = slider.getMinimum() - slider.getLowValue();
                    }

                    if ((delta > 0) && ((slider.getHighValue() + delta) > slider.getMaximum())) {
                        delta = slider.getMaximum() - slider.getHighValue();
                    }

                    if (delta != 0) {
                        offset(delta);
                    }
                    break;
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            slider.getModel().setValueIsAdjusting(false);
        }

        private void setCursor(int c) {
            Cursor cursor = Cursor.getPredefinedCursor(c);

            if (slider.getCursor() != cursor) {
                slider.setCursor(cursor);
            }
        }

        /**
         * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            if (!slider.isEnabled()) {
                return;
            }

            int currentHandle = changeHandleAccordingToVisibility(getMouseHandle(e.getX(), e.getY()));

            switch (currentHandle) {
                case MOUSE_HANDLE_BEFORE_MIN:
                    setMouseRollover(MOUSE_HANDLE_BEFORE_MIN);
                    setCursor((slider.getOrientation() == JSlider.VERTICAL) ? Cursor.N_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR);
                    break;
                case MOUSE_HANDLE_BEFORMIN_MIN:
                    setMouseRollover(MOUSE_HANDLE_BEFORMIN_MIN);
                    setCursor((slider.getOrientation() == JSlider.VERTICAL) ? Cursor.N_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR);
                    break;
                case MOUSE_HANDLE_MIN:
                    setMouseRollover(MOUSE_HANDLE_MIN);
                    setCursor((slider.getOrientation() == JSlider.VERTICAL) ? Cursor.N_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR);
                    break;
                case MOUSE_HANDLE_MIN_MAX:
                    setMouseRollover(MOUSE_HANDLE_MIN_MAX);
                    setCursor((slider.getOrientation() == JSlider.VERTICAL) ? Cursor.N_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR);
                    break;
                case MOUSE_HANDLE_MAX:
                    setMouseRollover(MOUSE_HANDLE_MAX);
                    setCursor((slider.getOrientation() == JSlider.VERTICAL) ? Cursor.S_RESIZE_CURSOR : Cursor.E_RESIZE_CURSOR);
                    break;
                case MOUSE_HANDLE_MIDDLE:
                    setMouseRollover(MOUSE_HANDLE_MIDDLE);
                    setCursor(Cursor.MOVE_CURSOR);
                    break;
                case MOUSE_HANDLE_NONE:
                    setMouseRollover(MOUSE_HANDLE_NONE);
                    setCursor(Cursor.DEFAULT_CURSOR);
                    break;
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                //implement restore
                //slider.repaint();
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
        }

        /**
         * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
         */
        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(Cursor.DEFAULT_CURSOR);
        }

        private int changeHandleAccordingToVisibility(int value) {
            switch (value) {
                case MOUSE_HANDLE_BEFORE_MIN:
                    if (!slider.getOptions().isVisibile(0)) {
                        return MOUSE_HANDLE_NONE;
                    }
                    break;
                case MOUSE_HANDLE_BEFORMIN_MIN:
                    if ((slider.getOptions().getVisibilities() & 0x110) == 0x000) {
                        return MOUSE_HANDLE_NONE;
                    }
                    break;
                case MOUSE_HANDLE_MIN:
                    if (!slider.getOptions().isVisibile(1)) {
                        return MOUSE_HANDLE_NONE;
                    }
                    break;
                case MOUSE_HANDLE_MIN_MAX:
                    if ((slider.getOptions().getVisibilities() & 0x011) == 0x000) {
                        return MOUSE_HANDLE_NONE;
                    }
                    break;
                case MOUSE_HANDLE_MAX:
                    if (!slider.getOptions().isVisibile(2)) {
                        return MOUSE_HANDLE_NONE;
                    }
            }

            return value;
        }
    }

}

