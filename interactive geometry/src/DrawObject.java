import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public abstract class DrawObject {

	protected int red, green, blue;
	// my listening model
	protected ArrayList<IObjectListener> myListeners;
	protected AffineTransform at;
	
	
	public abstract String toString();
	public abstract Shape getShape();
	public abstract double getThickness();
	
	protected DrawObject() {
		myListeners = new ArrayList<IObjectListener>();
		at = new AffineTransform();
	}
	
	public Color getColor() {
		return new Color(red, green, blue);
	}
	
	public abstract boolean isPoint(Point p);
	
	protected Point2D.Double nearestPoint(Point2D.Double p, double lowerT, double upperT) {
		double n = 10;
		double inc = (upperT-lowerT)/n;
		Point2D.Double lowP = computePoint(lowerT);
		//System.out.println(lowP.getX() + " " + lowP.getY());
		Point2D.Double hiP = computePoint(upperT);
		//System.out.println(hiP.getX() + " " + hiP.getY());
		if (dist(lowP,hiP) < 1.0) {
			//System.out.println(lowP.getX() + " " + lowP.getY());
			return lowP;
		}
		
		double nearT = lowerT;
		Point2D.Double nearP = lowP;
		double nearD = dist(nearP,p);
		
		for(double t = lowerT + inc; t <= upperT; t += inc) {
			Point2D.Double tp = computePoint(t);
			if (dist(tp,p) < nearD) {
				nearD = dist(tp,p);
				nearT = t;
				nearP = tp;
			}
		}
		double newLow = nearT - inc;
		if (newLow < lowerT) newLow = lowerT;
		double newHi = nearT + inc;
		if (newHi > upperT) newHi = upperT;
		return nearestPoint(p,newLow,newHi);
	}
	
	protected double dist(Point2D.Double a, Point2D.Double b) {
		// returns square of distance, okay because we want to compare not get actual
		if (a == null)
			System.out.println("A is NULL!!");
		if (b == null)
			System.out.println("B is NULL!!");
		
		double dx = a.getX() - b.getX();
		double dy = a.getY() - b.getY();
		
		return dx*dx + dy*dy;
	}
	
	
	protected abstract Point2D.Double computePoint(double t);
	
	// getters and setters for graphic manipulation
	public abstract ArrayList<Point2D.Double> getControlPoints();
	public abstract void moveControlPoint(int which, double diffInX, double diffInY);
	public abstract void changeLocation(double diffInX, double diffInY);
	public void changeColor(int newR, int newG, int newB) {
		red = newR;
		green = newG;
		blue = newB;
		notifyListeners();
	}
	public void changeColor(Color inColor) {
		red = inColor.getRed();
		green = inColor.getGreen();
		blue = inColor.getBlue();
		notifyListeners();
	}
	
	public abstract void changeThickness(double newThick);
	
	public void addObjectListener(IObjectListener objL) {
		myListeners.add(objL);
	}
	protected void notifyListeners() {
		for (int i = 0; i < myListeners.size(); i++) {
			myListeners.get(i).objectChanged();
		}
	}
	public AffineTransform getReverseTransform() {
		try {
			return at.createInverse();
		} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void concatenateTrans(AffineTransform ttp) {
		at.concatenate(ttp);
		//notifyListeners();
	}
	
	public abstract void finalizeTransform();
	public abstract void performTransform(AffineTransform tat);
	
}
