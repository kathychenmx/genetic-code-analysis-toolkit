package de.hsma.gentool.gui.helper;

import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

public class BetterGlassPane extends JPanel implements AWTEventListener {
	private static final long serialVersionUID = 1l;

	private final JRootPane pane;
	private EventListenerList listeners = new EventListenerList();
	
	public BetterGlassPane(JRootPane pane) {
		super(null);
		(this.pane=pane).setGlassPane(this);
		Toolkit.getDefaultToolkit().addAWTEventListener(this,
			AWTEvent.MOUSE_WHEEL_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.MOUSE_EVENT_MASK);
		setOpaque(false);
	}

	@Override public synchronized MouseListener[] getMouseListeners() { return listeners.getListeners(MouseListener.class); }
	@Override public synchronized void addMouseListener(MouseListener listener) { listeners.add(MouseListener.class,listener); }
	@Override public synchronized void removeMouseListener(MouseListener listener) { listeners.remove(MouseListener.class,listener); }
	
	@Override public synchronized MouseMotionListener[] getMouseMotionListeners() { return listeners.getListeners(MouseMotionListener.class); }
	@Override public synchronized void addMouseMotionListener(MouseMotionListener listener) { listeners.add(MouseMotionListener.class,listener); }
	@Override public synchronized void removeMouseMotionListener(MouseMotionListener listener) { listeners.remove(MouseMotionListener.class,listener); }
	
	@Override public synchronized MouseWheelListener[] getMouseWheelListeners() { return listeners.getListeners(MouseWheelListener.class); }
	@Override public synchronized void addMouseWheelListener(MouseWheelListener listener) { listeners.add(MouseWheelListener.class,listener); }
	@Override public synchronized void removeMouseWheelListener(MouseWheelListener listener) { listeners.remove(MouseWheelListener.class,listener); }
	
	public void eventDispatched(AWTEvent event) {
		if(event instanceof MouseEvent) {
			MouseEvent mouseEvent = (MouseEvent)event;
			if(!SwingUtilities.isDescendingFrom(mouseEvent.getComponent(),pane))
				return;
			/* change source of event to glass pane, DON'T use setSource on AWTEvent's! */
			MouseEvent newMouseEvent = !(mouseEvent instanceof MouseWheelEvent)?
				new MouseEvent(this,mouseEvent.getID(),mouseEvent.getWhen(),mouseEvent.getModifiers(),mouseEvent.getX(),mouseEvent.getY(),mouseEvent.getClickCount(),mouseEvent.isPopupTrigger(),mouseEvent.getButton()):
				new MouseWheelEvent(this,mouseEvent.getID(),mouseEvent.getWhen(),mouseEvent.getModifiers(),mouseEvent.getX(),mouseEvent.getY(),mouseEvent.getXOnScreen(),mouseEvent.getYOnScreen(),mouseEvent.getClickCount(),mouseEvent.isPopupTrigger(),((MouseWheelEvent)mouseEvent).getScrollType(),((MouseWheelEvent)mouseEvent).getScrollAmount(),((MouseWheelEvent)mouseEvent).getWheelRotation(),((MouseWheelEvent)mouseEvent).getPreciseWheelRotation());
			switch(event.getID()) {
			case MouseEvent.MOUSE_CLICKED:
				for(MouseListener listener:listeners.getListeners(MouseListener.class))
					listener.mouseClicked(newMouseEvent);
				break;
			case MouseEvent.MOUSE_ENTERED:
				for(MouseListener listener:listeners.getListeners(MouseListener.class))
					listener.mouseEntered(newMouseEvent);
				break;
			case MouseEvent.MOUSE_EXITED:
				for(MouseListener listener:listeners.getListeners(MouseListener.class))
					listener.mouseExited(newMouseEvent);
				break;
			case MouseEvent.MOUSE_PRESSED:
				for(MouseListener listener:listeners.getListeners(MouseListener.class))
					listener.mousePressed(newMouseEvent);
				break;
			case MouseEvent.MOUSE_RELEASED:
				for(MouseListener listener:listeners.getListeners(MouseListener.class))
					listener.mouseReleased(newMouseEvent);
				break;
			case MouseEvent.MOUSE_MOVED:
				for(MouseMotionListener listener:listeners.getListeners(MouseMotionListener.class))
					listener.mouseMoved(newMouseEvent);
				break;
			case MouseEvent.MOUSE_DRAGGED:
				for(MouseMotionListener listener:listeners.getListeners(MouseMotionListener.class))
					listener.mouseDragged(newMouseEvent);
				break;
			case MouseEvent.MOUSE_WHEEL:
				for(MouseWheelListener listener:listeners.getListeners(MouseWheelListener.class))
					listener.mouseWheelMoved((MouseWheelEvent)newMouseEvent);
				break;
			}
		}
	}
	

	/**
	 * If someone sets a new cursor to the GlassPane
	 * we expect that he knows what he is doing
	 * and return the super.contains(x,y)
	 * otherwise we return false to respect the cursors
	 * for the underneath components
	 */
	public boolean contains(int x, int y) {
		if(getCursor()==Cursor.getDefaultCursor())
			return false;
		return super.contains(x,y);
	}
}