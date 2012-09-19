import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;


public class Rectangle extends DrawObject {

	private double x1, y1, x2, y2, x3, y3, x4, y4, thickness;
	//private AffineTransform at;
	
	public Rectangle (double _leftX, double _topY, double _width,
			double _height, double _thickness, int _red, int _green, int _blue) {
		
		super();
		x1 = _leftX;
		y1 = _topY;
		x2 = _leftX + _width;
		y2 = _topY;
		x3 = _leftX + _width;
		y3 = _topY + _height;
		x4 = _leftX;
		y4 = _topY + _height;
		thickness = _thickness;
		red = _red;
		green = _green;
		blue = _blue;
		//at = new AffineTransform();
	}
	
	@Override
	public String toString() {
		return "R " + x1 + " " + y1 + " " + (x2-x1) + " " + (y3-y1) + " " + thickness + " " + red + " " + green + " " + blue;
	}

	@Override
	public Shape getShape() {
		/*
		int[] xpoints = {(int) x1, (int) x2, (int) x3, (int) x4};
		int [] ypoints = {(int) y1, (int) y2, (int) y3, (int) y4};
		
		//return new Rectangle2D.Double(leftX, topY, width, height);
		return new Polygon(xpoints, ypoints, 4);
		*/
		
		double tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4;
		
		double[] points = {x1,y1,x2,y2,x3,y3,x4,y4};
		double[] tpoints = {0,0,0,0,0,0,0,0};
		
		at.transform(points, 0, tpoints, 0, 4);
		
		tx1 = tpoints[0];
		ty1 = tpoints[1];
		tx2 = tpoints[2];
		ty2 = tpoints[3];
		tx3 = tpoints[4];
		ty3 = tpoints[5];
		tx4 = tpoints[6];
		ty4 = tpoints[7];
		
		int[] xpoints = {(int) tx1, (int) tx2, (int) tx3, (int) tx4};
		int [] ypoints = {(int) ty1, (int) ty2, (int) ty3, (int) ty4};
		
		//return new Rectangle2D.Double(leftX, topY, width, height);
		return new Polygon(xpoints, ypoints, 4);
	}

	@Override
	public double getThickness() {
		return thickness;
	}

	@Override
	public boolean isPoint(Point p) {
		
	//	if(thickness == 0) {
			if (getShape().contains(p)) {
				return true;
			}
	/*	}
		else {
			Point2D.Double convert = new Point2D.Double();
			convert.setLocation(p.getX(),p.getY());

			// if within sides and close to top or bottom
			if (p.getX() >= x1 && p.getX() <= x2) {
				if ((p.getY() >= (y1 - 5) && p.getY() <= (y1 + 5)) ||
						(p.getY() >= (y3 - 5) && p.getY() <= (y3 + 5))) {
					
					return true;
				}
			}
			
			// if within top and bottom and close to one of the sides
			if (p.getY() >= y1 && p.getY() <= y3){
				if ((p.getX() >= (x1 - 5) && p.getX() <= (x1 + 5)) ||
						(p.getX() >= (x2 - 5) && p.getX() <= (x2 + 5))) {

					return true;
				}
			}
			
			// if close to one of the corners (25 because of square in dist)
			if ((dist(convert,new Point2D.Double(x1,y1)) <= 25) ||
						(dist(convert,new Point2D.Double(x2,y2)) <= 25) ||
						(dist(convert,new Point2D.Double(x3,y3)) <= 25) ||
						(dist(convert,new Point2D.Double(x4,y4)) <= 25))
					return true;

		}
	*/	
		
		
		return false;
	}

	@Override
	protected Point2D.Double computePoint(double t) {
		// ignore this for rectangles, shouldn't be asking it this
		return null;
	}

	@Override
	public ArrayList<Double> getControlPoints() {
		// TODO Auto-generated method stub
		double tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4,
			tx5, ty5, tx6, ty6, tx7, ty7, tx8, ty8;
		
		double[] points = {x1,y1, (x1 + x2)/2,(y1 + y2)/2,
				x2,y2, (x2 + x3)/2,(y2 + y3)/2,
				x3,y3, (x3 + x4)/2,(y3 + y4)/2,
				x4,y4, (x4 + x1)/2,(y4 + y1)/2};
		double[] tpoints = {0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0};
		
		at.transform(points, 0, tpoints, 0, 8);
		
		tx1 = tpoints[0];
		ty1 = tpoints[1];
		tx2 = tpoints[2];
		ty2 = tpoints[3];
		tx3 = tpoints[4];
		ty3 = tpoints[5];
		tx4 = tpoints[6];
		ty4 = tpoints[7];
		tx5 = tpoints[8];
		ty5 = tpoints[9];
		tx6 = tpoints[10];
		ty6 = tpoints[11];
		tx7 = tpoints[12];
		ty7 = tpoints[13];
		tx8 = tpoints[14];
		ty8 = tpoints[15];
		
		ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>();
		Point2D.Double p1 = new Point2D.Double(tx1,ty1);
		temp.add(p1);
		Point2D.Double p2 = new Point2D.Double(tx2,ty2);
		temp.add(p2);
		Point2D.Double p3 = new Point2D.Double(tx3,ty3);
		temp.add(p3);
		Point2D.Double p4 = new Point2D.Double(tx4,ty4);
		temp.add(p4);
		Point2D.Double p5 = new Point2D.Double(tx5,ty5);
		temp.add(p5);
		Point2D.Double p6 = new Point2D.Double(tx6,ty6);
		temp.add(p6);
		Point2D.Double p7 = new Point2D.Double(tx7,ty7);
		temp.add(p7);
		Point2D.Double p8 = new Point2D.Double(tx8,ty8);
		temp.add(p8);
		
		return temp;
	}

	@Override
	public void moveControlPoint(int which, double diffInX, double diffInY) {
		// TODO Auto-generated method stub
		switch(which) {
		case 0:
			x1 += diffInX;
			x4 += diffInX;
			y1 += diffInY;
			y2 += diffInY;			
			break;
		case 1:
			y1 += diffInY;
			y2 += diffInY;
			break;
		case 2:
			x2 += diffInX;
			x3 += diffInX;
			y1 += diffInY;
			y2 += diffInY;
			break;
		case 3:
			x2 += diffInX;
			x3 += diffInX;
			break;
		case 4:
			x2 += diffInX;
			x3 += diffInX;
			y3 += diffInY;
			y4 += diffInY;
			break;
		case 5:
			y3 += diffInY;
			y4 += diffInY;
			break;
		case 6:
			x1 += diffInX;
			x4 += diffInX;
			y3 += diffInY;
			y4 += diffInY;
			break;
		case 7:
			x1 += diffInX;
			x4 += diffInX;
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
		x3 += diffInX;
		y3 += diffInY;
		x4 += diffInX;
		y4 += diffInY;	
		
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
		//double tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4;
		
		double[] points = {x1,y1,x2,y2,x3,y3,x4,y4};
		double[] tpoints = {0,0,0,0,0,0,0,0};
		
		at.transform(points, 0, tpoints, 0, 4);
		
		x1 = tpoints[0];
		y1 = tpoints[1];
		x2 = tpoints[2];
		y2 = tpoints[3];
		x3 = tpoints[4];
		y3 = tpoints[5];
		x4 = tpoints[6];
		y4 = tpoints[7];
		
		at = new AffineTransform();
		notifyListeners();
	}
	
	public void performTransform(AffineTransform tat) {
		// TODO Auto-generated method stub
		//double tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4;
		
		double[] points = {x1,y1,x2,y2,x3,y3,x4,y4};
		double[] tpoints = {0,0,0,0,0,0,0,0};
		
		tat.transform(points, 0, tpoints, 0, 4);
		
		x1 = tpoints[0];
		y1 = tpoints[1];
		x2 = tpoints[2];
		y2 = tpoints[3];
		x3 = tpoints[4];
		y3 = tpoints[5];
		x4 = tpoints[6];
		y4 = tpoints[7];
		
		//at = new AffineTransform();
		notifyListeners();
	}
	
	

}
