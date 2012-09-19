import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Curve extends DrawObject {

	private double x1, y1, x2, y2, x3, y3, x4, y4, thickness;
	
	public Curve (double _x1, double _y1, double _x2, double _y2, double _x3, double _y3,
			double _x4, double _y4, double _thickness, int _red, int _green, int _blue) {
		
		super();
		x1 = _x1;
		y1 = _y1;
		x2 = _x2;
		y2 = _y2;
		x3 = _x3;
		y3 = _y3;
		x4 = _x4;
		y4 = _y4;
		thickness = _thickness;
		red = _red;
		green = _green;
		blue = _blue;
	}
	
	@Override
	public String toString() {
		return "B " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + x3 + " " + y3 + " " + x4 + " " + y4 + " " + thickness + " " + red + " " + green + " " + blue;
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
		
		
		return new CubicCurve2D.Double(tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4);
	}

	@Override
	public double getThickness() {
		return thickness;
	}

	@Override
	public boolean isPoint(Point p) {
		
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
		
		// geometry matrix
		double[][] gM = {{x1,x2,x3,x4},{y1,y2,y3,y4}};
		// spline matrix
		double[][] sM = {{-1,3,-3,1},{3,-6,3,0},{-3,3,0,0},{1,0,0,0}};
		// coefficient matrix
		double[][] cM = {{0,0,0,0},{0,0,0,0}};
		// t matrix
		double[][] tM = {{t*t*t},{t*t},{t},{1}};
		
		// multiply geometry matrix by spline matrix to get coefficient matrix
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				cM[i][j] = gM[i][0] * sM[0][j] + gM[i][1] * sM[1][j] + gM[i][2] * sM[2][j] + gM[i][3] * sM[3][j]; 
			}
		}
		
		// multiply coefficient matrix by t matrix (use the parametric equations) to get x and y
		double x = cM[0][0] * tM[0][0] + cM[0][1] * tM[1][0] + cM[0][2] * tM[2][0] + cM[0][3] * tM[3][0];
		double y = cM[1][0] * tM[0][0] + cM[1][1] * tM[1][0] + cM[1][2] * tM[2][0] + cM[1][3] * tM[3][0];
		
		Point2D.Double result = new Point2D.Double();
		
		result.setLocation(x,y);
		
		return result;
	}

	@Override
	public ArrayList<Point2D.Double> getControlPoints() {
		// TODO Auto-generated method stub
		
		double tx1, ty1, tx2, ty2, tx3, ty3, tx4, ty4;
		
		double[] points = {x1,y1,
				x2,y2,
				x3,y3,
				x4,y4};
		double[] tpoints = {0,0, 0,0, 0,0, 0,0};
		
		at.transform(points, 0, tpoints, 0, 4);
		
		tx1 = tpoints[0];
		ty1 = tpoints[1];
		tx2 = tpoints[2];
		ty2 = tpoints[3];
		tx3 = tpoints[4];
		ty3 = tpoints[5];
		tx4 = tpoints[6];
		ty4 = tpoints[7];
		
		ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>();
		Point2D.Double p1 = new Point2D.Double(tx1,ty1);
		temp.add(p1);
		Point2D.Double p2 = new Point2D.Double(tx2,ty2);
		temp.add(p2);
		Point2D.Double p3 = new Point2D.Double(tx3,ty3);
		temp.add(p3);
		Point2D.Double p4 = new Point2D.Double(tx4,ty4);
		temp.add(p4);
		
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
		case 2:
			x3 += diffInX;
			y3 += diffInY;
			break;
		case 3:
			x4 += diffInX;
			y4 += diffInY;
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

	@Override
	public void performTransform(AffineTransform tat) {
		// TODO Auto-generated method stub
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
