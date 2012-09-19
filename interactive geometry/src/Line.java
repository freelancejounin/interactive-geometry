import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Line extends DrawObject {
	
	private double x1, y1, x2, y2, thickness;

	public Line (double _x1, double _y1, double _x2, double _y2,
			double _thickness, int _red, int _green, int _blue) {
		
		super();
		
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		thickness = _thickness;
		red = _red;
		green = _green;
		blue = _blue;
		
	}
	
	@Override
	public String toString() {
		return "L " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + thickness + " " + red + " " + green + " " + blue;
	}

	@Override
	public Shape getShape() {
		
		double tx1, ty1, tx2, ty2;
		
		double[] points = {x1,y1,x2,y2};
		double[] tpoints = {0,0,0,0};
		
		at.transform(points, 0, tpoints, 0, 2);
		
		tx1 = tpoints[0];
		ty1 = tpoints[1];
		tx2 = tpoints[2];
		ty2 = tpoints[3];
		
		return new Line2D.Double(tx1, ty1, tx2, ty2);
	}

	@Override
	public double getThickness() {
		return thickness;
	}

	@Override
	public boolean isPoint(Point p) {
		
		//if(((Line2D.Double)getShape()).ptLineDist(p) <= 5)
			//return true;
	
	// is always false for a line
//		if(getShape().contains(p))
//			return true;
		
		Point2D.Double convert = new Point2D.Double();
		convert.setLocation(p.getX(),p.getY());
		
		//System.out.println("Lower t: " + computePoint(0));
		//System.out.println("Upper t: " + computePoint(1));
		
		
		if (dist(convert,nearestPoint(convert,0,1)) <= 25) {
			return true;
		}
		
		return false;
		
	}

	@Override
	protected Point2D.Double computePoint(double t) {
		
		Point2D.Double result = new Point2D.Double();
		// use parametric equations to generate x and y
		double x = x1 + (x2 - x1) * t;
		double y = y1 + (y2 - y1) * t;
		result.setLocation(x,y);
		
		return result;
	}

	@Override
	public ArrayList<Point2D.Double> getControlPoints() {
		// TODO Auto-generated method stub
		double tx1, ty1, tx2, ty2;
		
		double[] points = {x1,y1,x2,y2};
		double[] tpoints = {0,0,0,0};
		
		at.transform(points, 0, tpoints, 0, 2);
		
		tx1 = tpoints[0];
		ty1 = tpoints[1];
		tx2 = tpoints[2];
		ty2 = tpoints[3];
		
		
		ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>();
		Point2D.Double p1 = new Point2D.Double(tx1,ty1);
		temp.add(p1);
		Point2D.Double p2 = new Point2D.Double(tx2,ty2);
		temp.add(p2);
		
		return temp;
	}

	@Override
	public void moveControlPoint(int which, double diffInX, double diffInY) {
		// TODO Auto-generated method stub
		switch(which) {
		case 0:
			x1 += diffInX;
			y1 += diffInY;
			break;
		case 1:
			x2 += diffInX;
			y2 += diffInY;
			break;
		default:
			break;
		}
		notifyListeners();
	}

	@Override
	public void changeLocation(double diffInX, double diffInY) {
		// TODO Auto-generated method stub
		x1 += diffInX;
		y1 += diffInY;
		x2 += diffInX;
		y2 += diffInY;
		notifyListeners();
	}

	@Override
	public void changeThickness(double newThick) {
		// TODO Auto-generated method stub
		thickness = newThick;
		notifyListeners();
	}

	@Override
	public void finalizeTransform() {
		// TODO Auto-generated method stub
		double[] points = {x1,y1,x2,y2};
		double[] tpoints = {0,0,0,0};
		
		at.transform(points, 0, tpoints, 0, 2);
		
		x1 = tpoints[0];
		y1 = tpoints[1];
		x2 = tpoints[2];
		y2 = tpoints[3];
		
		at = new AffineTransform();
		notifyListeners();
	}

	@Override
	public void performTransform(AffineTransform tat) {
		// TODO Auto-generated method stub
		double[] points = {x1,y1,x2,y2};
		double[] tpoints = {0,0,0,0};
		
	
		tat.transform(points, 0, tpoints, 0, 2);
		
		x1 = tpoints[0];
		y1 = tpoints[1];
		x2 = tpoints[2];
		y2 = tpoints[3];
		
		//at = new AffineTransform();
		notifyListeners();
	}



}
