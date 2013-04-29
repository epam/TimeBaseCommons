package deltix.util.swing.slider;

import com.jidesoft.plaf.basic.BasicRangeSliderUI;
import com.jidesoft.swing.RangeSlider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * User: TurskiyS
 * Date: 4/8/13
 */
public class DrawnSliderUI extends BasicRangeSliderUI {

    private RangeSlider slider;

    private int leftDefaultArea = 0;

    private boolean overrideThumb = false;

    protected static final int MOUSE_HANDLE_BEFORE_MIN = 5;

    public DrawnSliderUI(RangeSlider slider) {
        super(slider);
        this.slider = slider;
    }


    @Override
    public void paintThumb(Graphics g) {
        if (overrideThumb) {

        } else {
            //super.paintThumb(g);
        }
    }

    @Override
    public void paintTrack(Graphics g) {
        if (slider != null) {
            int value = slider.getValue();
            int low = slider.getLowValue();
            int high = slider.getHighValue();
            int min = slider.getMinimum();
            int max = slider.getMaximum();

            int borderWidth = (int) SliderOptions.STROKE_WIDTH;
            int x1 = borderWidth, x2 = borderWidth, x3;

            Rectangle bounds = slider.getBounds();

            if (leftDefaultArea > 0 && low != 0) {
                g.setColor(SliderOptions.CACHE_AREA_COLOR);
                g.fillRect(borderWidth,
                        borderWidth,
                        x1 = xPositionForValue(leftDefaultArea) - borderWidth,
                        bounds.height - 2 * borderWidth);
            }

            if (min != low) {
                g.setColor(SliderOptions.PREALLOCATE_AREA_COLOR);
                g.fillRect(x1,
                        borderWidth,
                        x2 = xPositionForValue(value) - borderWidth,
                        bounds.height - 2 * borderWidth);
            }

            if (value != max) {
                g.setColor(SliderOptions.MAX_MEMORY_COLOR);
                g.fillRect(x2,
                        borderWidth,
                        x3 = xPositionForValue(high) - borderWidth,
                        bounds.height - 2 * borderWidth);
            } else {
                x3 = xPositionForValue(max);
            }

            if (high < max) {
                g.setColor(SliderOptions.TOTAL_PHYSICAL_MEMORY_COLOR);
                g.fillRect(x3,
                        borderWidth,
                        xPositionForValue(max),
                        bounds.height - 2 * borderWidth);
            }
        }
    }

    @Override
    protected TrackListener createTrackListener(JSlider slider) {
        return new RangeTrackListener();
    }

    @Override
    protected int getMouseHandle(int x, int y) {
        Rectangle rect = trackRect;

        if (slider.getOrientation() == JSlider.VERTICAL) {
            int minBeforeY = yPositionForValue(leftDefaultArea);
            int minY = yPositionForValue(slider.getLowValue());
            int maxY = yPositionForValue(slider.getHighValue());

            Rectangle minBeforeRect = new Rectangle(
                    rect.x + rect.width / 2,
                    minBeforeY,
                    _lowerIcon.getIconWidth(),
                    _lowerIconV.getIconHeight());

            if (minBeforeRect.contains(x, y)){
                return MOUSE_HANDLE_BEFORE_MIN;
            }

            Rectangle minRect = new Rectangle(
                    rect.x + rect.width / 2,
                    minY - _lowerIconV.getIconHeight() / 2,
                    _lowerIcon.getIconWidth(),
                    _lowerIconV.getIconHeight());
            if (minRect.contains(x, y)) {
                return MOUSE_HANDLE_MIN;
            }

            Rectangle maxRect = new Rectangle(rect.x,
                    maxY - _upperIconV.getIconHeight() / 2,
                    _upperIconV.getIconWidth(),
                    _upperIconV.getIconHeight());
            if (maxRect.contains(x, y)) {
                return MOUSE_HANDLE_MAX;
            }

            return MOUSE_HANDLE_NONE;
        } else {
            int minBeforeX = xPositionForValue(leftDefaultArea);
            int minX = xPositionForValue(slider.getLowValue());
            int maxX = xPositionForValue(slider.getHighValue());


            Rectangle minBeforeRect = new Rectangle(
                    minBeforeX,
                    rect.y,
                    _lowerIcon.getIconWidth(),
                    _lowerIconV.getIconHeight());

            if (minBeforeRect.contains(x, y)){
                return MOUSE_HANDLE_BEFORE_MIN;
            }


            Rectangle minRect = new Rectangle(
                    minX - _lowerIcon.getIconWidth() / 2,
                    rect.y,//rect.y + rect.height / 2,
                    _lowerIcon.getIconWidth(),
                    _lowerIcon.getIconHeight()
            );

            if (minRect.contains(x, y)) {
                return MOUSE_HANDLE_MIN;
            }


            Rectangle maxRect = new Rectangle(
                    maxX - _upperIcon.getIconWidth() / 2,
                    rect.y,
                    _upperIcon.getIconWidth(),
                    _upperIcon.getIconHeight());
            if (maxRect.contains(x, y)) {
                return MOUSE_HANDLE_MAX;
            }

            return MOUSE_HANDLE_NONE;
        }
    }

    private void offset(int delta) {
        slider.getModel().setValue(slider.getLowValue() + delta);
    }

    public void setLeftDefaultArea(int leftDefaultArea) {
        this.leftDefaultArea = leftDefaultArea;
    }

    public int getLeftDefaultArea() {
        return leftDefaultArea;
    }

    public void setOverrideThumb(boolean overrideThumb) {
        this.overrideThumb = overrideThumb;
    }

    public int getXLocation(int value) {
        return xPositionForValue(value);
    }

    public int getYLocation(int value) {
        return yPositionForValue(value);
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

            handle = getMouseHandle(e.getX(), e.getY());

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

            if (handle == (MOUSE_HANDLE_MIN | MOUSE_HANDLE_MAX)) {
                if ((newLocation - mouseStartLocation) > 2) {
                    handle = MOUSE_HANDLE_MAX;
                } else if ((newLocation - mouseStartLocation) < -2) {
                    handle = MOUSE_HANDLE_MIN;
                } else {
                    return;
                }
            }

            RangeSlider rangeSlider = slider;
            DrawnSliderUI sliderUI = (DrawnSliderUI) slider.getUI();
            switch (handle) {
                case MOUSE_HANDLE_BEFORE_MIN:
                    sliderUI.setLeftDefaultArea(Math.min(newValue, rangeSlider.getLowValue()));
                    break;
                case MOUSE_HANDLE_MIN:
                    rangeSlider.setLowValue(leftDefaultArea == 0 ?
                            Math.min(newValue, rangeSlider.getHighValue()) :
                            Math.max(Math.min(newValue, rangeSlider.getHighValue()), leftDefaultArea));
                    break;
                case MOUSE_HANDLE_MAX:
                    rangeSlider.setHighValue(Math.max(rangeSlider.getLowValue(), newValue));
                    break;
                case MOUSE_HANDLE_MIDDLE:
                    int delta = (slider.getOrientation() == JSlider.VERTICAL) ?
                            valueForYPosition(newLocation - handleOffset) - rangeSlider.getLowValue() :
                            valueForXPosition(newLocation - handleOffset) - rangeSlider.getLowValue();
                    if ((delta < 0) && ((rangeSlider.getLowValue() + delta) < rangeSlider.getMinimum())) {
                        delta = rangeSlider.getMinimum() - rangeSlider.getLowValue();
                    }

                    if ((delta > 0) && ((rangeSlider.getHighValue() + delta) > rangeSlider.getMaximum())) {
                        delta = rangeSlider.getMaximum() - rangeSlider.getHighValue();
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

            switch (getMouseHandle(e.getX(), e.getY())) {
                case MOUSE_HANDLE_BEFORE_MIN:
                    setMouseRollover(MOUSE_HANDLE_BEFORE_MIN);
                    setCursor((slider.getOrientation() == JSlider.VERTICAL) ? Cursor.N_RESIZE_CURSOR : Cursor.W_RESIZE_CURSOR);
                    break;
                case MOUSE_HANDLE_MIN:
                    setMouseRollover(MOUSE_HANDLE_MIN);
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
    }

}

