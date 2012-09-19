import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;


public class SelectionRectangle extends DrawObject {

	private double x1, y1, x2, y2, x3, y3, x4, y4, x5, y5, thickness;
	private AffineTransform transToPerform;
	
	public SelectionRectangle (double _leftX, double _topY, double _width,
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
		x5 = _leftX + (_width/2);
		y5 = _topY - 15;
		thickness = _thickness;
		red = _red;
		green = _green;
		blue = _blue;
		transToPerform = new AffineTransform();
	}
	
	
	@Override
	public String toString() {
		return "R " + x1 + " " + y1 + " " + (x2-x1) + " " + (y3-y1) + " " + thickness + " " + red + " " + green + " " + blue;
	}
	

	@Override
	public Shape getShape() {
		
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
		
		// selection rectangle is never filled for purposes of manipulation
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
		
		return false;
	}
	
	public boolean contains(Point p) {
		if (getShape().contains(p)) {
			return true;
		}
		
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
			tx5, ty5, tx6, ty6, tx7, ty7, tx8, ty8, tx9, ty9;
		
		double[] points = {x1,y1, (x1 + x2)/2,(y1 + y2)/2,
				x2,y2, (x2 + x3)/2,(y2 + y3)/2,
				x3,y3, (x3 + x4)/2,(y3 + y4)/2,
				x4,y4, (x4 + x1)/2,(y4 + y1)/2,
				x5,y5};
		double[] tpoints = {0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0, 0,0};
		
		at.transform(points, 0, tpoints, 0, 9);
		
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
		tx9 = tpoints[16];
		ty9 = tpoints[17];
		
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
		
		// special rotation point
		Point2D.Double p9 = new Point2D.Double(tx9,ty9);
		temp.add(p9);
		
		// center proof
	//	Point2D.Double center = new Point2D.Double((tx1 + tx5)/2,(ty1 + ty5)/2);
	//	temp.add(center);
		
		return temp;
	}

	@Override
	public void moveControlPoint(int which, double diffInX, double diffInY) {
		// TODO Auto-generated method stub
		switch(which) {
		case 0:
			/*
			x1 += diffInX;
			x4 += diffInX;
			x5 += (diffInX/2);
			y1 += diffInY;
			y2 += diffInY;
			y5 += diffInY;
			*/
			
			transToPerform = AffineTransform.getTranslateInstance(x3,y3);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					(x3 - (x1 + diffInX))/(x3 - x1),
					(y3 - (y1 + diffInY))/(y3 - y1)));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-x3, -y3));
			
		//	at.concatenate(transToPerform);
			break;
		case 1:
			/*
			y1 += diffInY;
			y2 += diffInY;
			y5 += diffInY;
			*/
			transToPerform = AffineTransform.getTranslateInstance((x3+x4)/2,(y3+y4)/2);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					1,
					((y3+y4)/2 - ((y1+y2)/2 + diffInY))/((y3+y4)/2 - (y1+y2)/2)));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-(x3+x4)/2, -(y3+y4)/2));
			
		//	at.concatenate(transToPerform);
			break;
		case 2:
			/*
			x2 += diffInX;
			x3 += diffInX;
			x5 += (diffInX/2);
			y1 += diffInY;
			y2 += diffInY;
			y5 += diffInY;
			*/
			transToPerform = AffineTransform.getTranslateInstance(x4,y4);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					(x4 - (x2 + diffInX))/(x4 - x2),
					(y4 - (y2 + diffInY))/(y4 - y2)));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-x4, -y4));

			break;
		case 3:
			/*
			x2 += diffInX;
			x3 += diffInX;
			x5 += (diffInX/2);
			*/
			transToPerform = AffineTransform.getTranslateInstance((x1+x4)/2,(y1+y4)/2);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					((x1+x4)/2 - ((x2+x3)/2 + diffInX))/((x1+x4)/2 - (x2+x3)/2),
					1));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-(x1+x4)/2, -(y1+y4)/2));
			
			break;
		case 4:
			/*
			x2 += diffInX;
			x3 += diffInX;
			x5 += (diffInX/2);
			y3 += diffInY;
			y4 += diffInY;
			*/
			transToPerform = AffineTransform.getTranslateInstance(x1,y1);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					(x1 - (x3 + diffInX))/(x1 - x3),
					(y1 - (y3 + diffInY))/(y1 - y3)));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-x1, -y1));
			
			break;
		case 5:
			/*
			y3 += diffInY;
			y4 += diffInY;
			*/
			transToPerform = AffineTransform.getTranslateInstance((x1+x2)/2,(y1+y2)/2);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					1,
					((y1+y2)/2 - ((y3+y4)/2 + diffInY))/((y1+y2)/2 - (y3+y4)/2)));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-(x1+x2)/2, -(y1+y2)/2));
			
		//	at.concatenate(transToPerform);
			break;
		case 6:
			/*
			x1 += diffInX;
			x4 += diffInX;
			x5 += (diffInX/2);
			y3 += diffInY;
			y4 += diffInY;
			*/
			transToPerform = AffineTransform.getTranslateInstance(x2,y2);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					(x2 - (x4 + diffInX))/(x2 - x4),
					(y2 - (y4 + diffInY))/(y2 - y4)));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-x2, -y2));
			
			break;
		case 7:
			/*
			x1 += diffInX;
			x4 += diffInX;
			x5 += (diffInX/2);
			*/
			transToPerform = AffineTransform.getTranslateInstance((x2+x3)/2,(y2+y3)/2);
			transToPerform.concatenate(AffineTransform.getScaleInstance(
					((x2+x3)/2 - ((x1+x4)/2 + diffInX))/((x2+x3)/2 - (x1+x4)/2),
					1));
			transToPerform.concatenate(AffineTransform.getTranslateInstance(-(x2+x3)/2, -(y2+y3)/2));
			
			break;
		case 8:
			// super-complex rotation
			//Point2D.Double a = new Point2D.Double(x5,y5);
			Point2D.Double b = new Point2D.Double(x5 + diffInX,y5 + diffInY);
			Point2D.Double center = new Point2D.Double((x1 + x3)/2,(y1 + y3)/2);
						
			double theta;
						
			double aX = x5 - center.getX();
			double aY = y5 - center.getY();
			double bX = b.getX() - center.getX();
			double bY = b.getY() - center.getY();
			theta = Math.atan2(bY,bX) - Math.atan2(aY,aX);
			
			//transToPerform = AffineTransform.getTranslateInstance(center.getX(), center.getY());
			transToPerform = AffineTransform.getRotateInstance(theta, center.getX(), center.getY());
			//transToPerform.concatenate(AffineTransform.getTranslateInstance(-center.getX(), -center.getY()));
			
			//at.concatenate(transToPerform);
			
			break;
		case 10:	//	special case for selection rectangle creation
			
			x2 += diffInX;
			x3 += diffInX;
			x5 += (diffInX/2);
			y3 += diffInY;
			y4 += diffInY;
			
			break;
		default:
			break;
		}
		//notifyListeners();
	}

	@Override
	public void changeLocation(double diffInX, double diffInY) {
		
		/*
		// TODO Auto-generated method stub
		x1 += diffInX;
		y1 += diffInY;
		x2 += diffInX;
		y2 += diffInY;
		x3 += diffInX;
		y3 += diffInY;
		x4 += diffInX;
		y4 += diffInY;	
		x5 += diffInX;
		y5 += diffInY;
		*/
		
		//notifyListeners();
		transToPerform = AffineTransform.getTranslateInstance(diffInX, diffInY);
		
		//at.concatenate(transToPerform);
	}

	
	@Override
	public void changeThickness(double newThick) {
		// TODO Auto-generated method stub
		thickness = newThick;
		notifyListeners();
	}
	
	public double getRotateX() {
		return x5;
	}
	
	public double getRotateY() {
		return y5;
	}
	
	public AffineTransform getTransToPerform() {
		return transToPerform;
	}


	@Override
	public void finalizeTransform() {
		// TODO Auto-generated method stub
		double[] points = {x1,y1,x2,y2,x3,y3,x4,y4,x5,y5};
		double[] tpoints = {0,0,0,0,0,0,0,0,0,0};
		
		at.transform(points, 0, tpoints, 0, 5);
		
		x1 = tpoints[0];
		y1 = tpoints[1];
		x2 = tpoints[2];
		y2 = tpoints[3];
		x3 = tpoints[4];
		y3 = tpoints[5];
		x4 = tpoints[6];
		y4 = tpoints[7];
		x5 = tpoints[8];
		y5 = tpoints[9];
		
		at = new AffineTransform();
	}


	@Override
	public void performTransform(AffineTransform tat) {
		// TODO Auto-generated method stub
		double[] points = {x1,y1,x2,y2,x3,y3,x4,y4,x5,y5};
		double[] tpoints = {0,0,0,0,0,0,0,0,0,0};
		
		tat.transform(points, 0, tpoints, 0, 5);
		
		x1 = tpoints[0];
		y1 = tpoints[1];
		x2 = tpoints[2];
		y2 = tpoints[3];
		x3 = tpoints[4];
		y3 = tpoints[5];
		x4 = tpoints[6];
		y4 = tpoints[7];
		x5 = tpoints[8];
		y5 = tpoints[9];
	}

}
