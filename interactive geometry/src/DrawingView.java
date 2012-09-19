import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


public class DrawingView extends JPanel implements IDrawingView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DrawingModel myModel;
	private DrawObject curSel;
	private int selIndex;
	private int curControl;
	private Point2D.Double dragStart;
	private Point2D.Double oldLoc;
	private boolean moveable;
	private JFrame parentFrame;
	private HashMap<String,DrawingModel> myMapRef;
	private Color curColor;
	private double curThickness;
	private int curMode;
	private DrawObject selRect;		// the selection rectangle
	private boolean creatingSel;
	private boolean editableSel;
	private ArrayList<DrawObject> curSels;
	
	// menu objects
	private JMenuBar menuBar;
	private JMenu fileMenu, setMenu;
	private JMenuItem openMI, saveMI, saveAsMI, colorMI, thicknessMI;
	
	// button palette objects
	private JPanel palette;
	private JButton selectB, lineB, rectB, rectFB, curveB;
	
	public DrawingView(DrawingModel model, HashMap<String,DrawingModel> map) {
		myModel = model;
		addMapRef(map);
		myModel.addModelListener(this);
		
		Mousey mouse = new Mousey();
		mouse.setView(this);
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		MasterKey keyes = new MasterKey();
		addKeyListener(keyes);
		setFocusable(true);
		
		constructMenuBar();
		constructPalette();
		
		dragStart = null;
		oldLoc = null;
		moveable = false;
		curMode = 0;
		curColor = Color.BLACK;		// default color when view first opened
		curSels = new ArrayList<DrawObject>();
	}
	
	/*
	 *	Called by the constructor to create the menubar for this view 
	 */
	private void constructMenuBar() {
		MenuListener mListener = new MenuListener();
		mListener.setView(this);
		
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		openMI = new JMenuItem("Open...");
		openMI.addActionListener(mListener);
		fileMenu.add(openMI);
		saveMI = new JMenuItem("Save");
		saveMI.addActionListener(mListener);
		fileMenu.add(saveMI);
		saveAsMI = new JMenuItem("Save As...");
		saveAsMI.addActionListener(mListener);
		fileMenu.add(saveAsMI);
		menuBar.add(fileMenu);
		
		setMenu = new JMenu("Set");
		colorMI = new JMenuItem("Color...");
		colorMI.addActionListener(mListener);
		setMenu.add(colorMI);
		thicknessMI = new JMenuItem("Thickness...");
		thicknessMI.addActionListener(mListener);
		setMenu.add(thicknessMI);
		menuBar.add(setMenu);
	}
	
	/**
	 * 	Called by the JFrame to get the menubar of this view
	 */
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	/*
	 *	Called by the constructor to create the palette for this view 
	 */
	private void constructPalette() {
		ButtonListener bListener = new ButtonListener();
		
		palette = new JPanel();
		palette.setLayout(new BoxLayout(palette, BoxLayout.Y_AXIS));
		
		selectB = new JButton(new ImageIcon(getClass().getResource("resource/selectionbutton.png")));
		selectB.addActionListener(bListener);
		palette.add(selectB);
		
		lineB = new JButton(new ImageIcon(getClass().getResource("resource/linebutton.png")));
		lineB.addActionListener(bListener);
		palette.add(lineB);
		
		rectB = new JButton(new ImageIcon(getClass().getResource("resource/rectanglebutton.png")));
		rectB.addActionListener(bListener);
		palette.add(rectB);
		
		rectFB = new JButton(new ImageIcon(getClass().getResource("resource/rectanglefilledbutton.png")));
		rectFB.addActionListener(bListener);
		palette.add(rectFB);
		
		curveB = new JButton(new ImageIcon(getClass().getResource("resource/curvebutton.png")));
		curveB.addActionListener(bListener);
		palette.add(curveB);
		
	}
	
	/**
	 * 	Called by the JFrame to get the palette of this view
	 */
	public JPanel getPalette() {
		return palette;
	}
	
	
		
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		//g2.setPaint(new Color(125, 125, 125));
		
		//g2.fill(new Rectangle2D.Double(40,70,30,20));
		
		for (int i = 0; i < myModel.nDrawObjects(); i++) {
			
			DrawObject temp = myModel.getDrawObject(i);
			
			g2.setPaint(temp.getColor());
			
			double thick = temp.getThickness();
			
			if (thick == 0) {		// should be filled
				g2.fill(temp.getShape());
			}
			else {		// should be outline
				BasicStroke tStroke = new BasicStroke((float)thick);
				g2.setStroke(tStroke);
				g2.draw(temp.getShape());
			}

			
		}
		
		// draw selection rectangle, if applicable
		if (selRect != null) {
			
			g2.setPaint(Color.BLACK);
			
			double thick = 3;
			
			BasicStroke tStroke = new BasicStroke((float)thick);
			g2.setStroke(tStroke);
			g2.draw(selRect.getShape());
		}
		
		// draw control points of currently selected object/selectionRectangle LAST,
		// so they are always visible
		//g2.setPaint(Color.BLACK);
		BasicStroke tStroke = new BasicStroke((float)1);
		g2.setStroke(tStroke);
		DrawObject tempRef = curSel;
		if (tempRef == null) {
			tempRef = selRect;
		}
		if (tempRef != null) {
			ArrayList<Point2D.Double> cPoints = tempRef.getControlPoints();
			if (tempRef instanceof Curve) {
				g2.setPaint(Color.BLACK);
				Point2D.Double cp1 = cPoints.get(0);
				Point2D.Double cp2 = cPoints.get(1);
				g2.draw(new Line2D.Double(cp1,cp2));
				cp1 = cPoints.get(2);
				cp2 = cPoints.get(3);
				g2.draw(new Line2D.Double(cp1,cp2));
			}
			for (int i = 0; i < cPoints.size(); i++) {
				Point2D.Double cPoint = cPoints.get(i);
				// draw a circle with radius 5 with center at the control point
				g2.setPaint(Color.WHITE);
				g2.fill(new Ellipse2D.Double(cPoint.getX() - 5, cPoint.getY() - 5, 10, 10));
				g2.setPaint(Color.BLACK);
				g2.draw(new Ellipse2D.Double(cPoint.getX() - 5, cPoint.getY() - 5, 10, 10));
			}
		}
		
    }
	
	public GeometryDescriptor pointGeometry(Point mouseLoc)
	{
		//boolean found = false;
		DrawObject temp = null;
		
		// search control points before objects (you have the currently selected object already)
		// if within one, set curControl and bail out, don't need to do anything else
		
		DrawObject tempRef = curSel;
		if (tempRef == null) {
			tempRef = selRect;
		}
		if (tempRef != null) {
		
			Point2D.Double cp;
			ArrayList<Point2D.Double> cPoints = tempRef.getControlPoints(); 
			
			for (int i = cPoints.size() - 1; i >= 0; i--) {
				cp = cPoints.get(i);
				
				double dx = cp.getX() - mouseLoc.getX();
				double dy = cp.getY() - mouseLoc.getY();
				
				if ((dx*dx + dy*dy) <= 25) {		// if within 5 pixels of this control point

					curControl = i;
					
					if (selRect != null) {
						editableSel = true;
					}

				return new GeometryDescriptor(selIndex,-1,curControl);
				}
			}
		
		}
		
		// search selRect before objects (just isPoint, control points already checked)
		if (selRect != null) {
			
			AffineTransform tAt = ((SelectionRectangle)selRect).getReverseTransform();
			
			double newEX;
			double newEY;
			
			double[] points = {(double)mouseLoc.getX(),(double)mouseLoc.getY()};
			double[] tpoints = {0,0, 0,0};
			
			tAt.transform(points, 0, tpoints, 0, 1);
			
			newEX = tpoints[0];
			newEY = tpoints[1];
			
			if(selRect.isPoint(new Point((int)newEX,(int)newEY))) {
				// found = true;
				curControl = -1;
				selIndex = -1;
				
				editableSel = true;
				
				return new GeometryDescriptor(-1,-1,-1);
					
			}
		}
		
		// if you're not an active object or the selection rectangle,
		// finalize all previous transforms before manipulating further
		for (int i = myModel.nDrawObjects() - 1; i >= 0; i--) {	
			myModel.getDrawObject(i).finalizeTransform();
		}
		
		for (int i = myModel.nDrawObjects() - 1; i >= 0; i--) {
			
			temp = myModel.getDrawObject(i);
			
			AffineTransform tAt = temp.getReverseTransform();
			
			double newEX;
			double newEY;
			
			double[] points = {(double)mouseLoc.getX(),(double)mouseLoc.getY()};
			double[] tpoints = {0,0, 0,0};
			
			tAt.transform(points, 0, tpoints, 0, 1);
			
			newEX = tpoints[0];
			newEY = tpoints[1];
			
			if(temp.isPoint(new Point((int)newEX,(int)newEY))) {
				// found = true;
				curSel = temp;
				curControl = -1;
				selIndex = i;
				curColor = curSel.getColor();
				
				curThickness = curSel.getThickness();
					return new GeometryDescriptor(i,-1,curControl);
					
			}	
		}
		
		
		// searched all objects and found nothing
		selRect = new SelectionRectangle(dragStart.getX(),dragStart.getY(),0,0,
				3,255,255,255);
		curControl = 4;
		creatingSel = true;
		
		curSel = null;
		//curControl = -1;
		selIndex = -1;
		return new GeometryDescriptor(-1,-1,-1);
		
	}
	
	public class GeometryDescriptor {
		private int index;
		private int charIndex;
		private int cntrlIndex;
		
		public GeometryDescriptor(int _index, int _charIndex, int _cntrlIndex) {
			index = _index;
			charIndex = _charIndex;
			cntrlIndex = _cntrlIndex;
		}
		
		public int getIndex() {
			return index;
		}
		
		public int getCharIndex() {
			return charIndex;
		}
		
		public int getCntrlIndex() {
			return cntrlIndex;
		}
		
	}
	
	private class Mousey implements MouseListener, MouseMotionListener
	{
		private DrawingView myView;		// needed to gain focus on mouse clicks
		
		public void mousePressed (MouseEvent event) {
			
			dragStart = new Point2D.Double(event.getX(),event.getY());
			oldLoc = dragStart;
			
			myView.requestFocusInWindow();
			
			if (curMode == 0) {
				//System.out.println("Mouse click: " + event.getX() + "," + event.getY());
				GeometryDescriptor desc = pointGeometry(event.getPoint());
				String result = "Object Index: " + desc.getIndex();
				if (desc.getCharIndex() != -1) {
					result += " and Character Index: " + desc.getCharIndex();
				}
				if (curSel != null) {
					result += " and Control Point Index: " + desc.getCntrlIndex();
					selRect = null;
				}
				
				System.out.println(result);				
			}
			else {
				DrawObject temp;
					
				switch(curMode) {
				case 1:
					temp = new Line(dragStart.getX(),dragStart.getY(),dragStart.getX(),dragStart.getY(),
							curThickness,curColor.getRed(),curColor.getGreen(),curColor.getBlue());
					curControl = 1;
					break;
				case 4:
					temp = new Rectangle(dragStart.getX(),dragStart.getY(),0,0,
							curThickness,curColor.getRed(),curColor.getGreen(),curColor.getBlue());
					curControl = 4;
					break;
				case 5:
					temp = new Rectangle(dragStart.getX(),dragStart.getY(),0,0,
							0,curColor.getRed(),curColor.getGreen(),curColor.getBlue());
					curControl = 4;
					break;
				case 6:
					temp = new Curve(dragStart.getX(),dragStart.getY(),dragStart.getX(), dragStart.getY() + 20,
							dragStart.getX() + 20,dragStart.getY(),dragStart.getX(),dragStart.getY(),
							curThickness,curColor.getRed(),curColor.getGreen(),curColor.getBlue());
					curControl = 3;
					break;
				default:
					// this initialization is here to make the compiler happy,
					// but it shouldn't be possible to get here, so throw an exception to say hey,
					// and start drawing a line to not crash
					temp = new Line(dragStart.getX(),dragStart.getY(),dragStart.getX(),dragStart.getY(),
							curThickness,curColor.getRed(),curColor.getGreen(),curColor.getBlue());
					curControl = 1;
					try {
						throw new IOException("Mouse-down while in an unsupported mode.");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				
				curSel = temp;
				myModel.addDrawObject(temp);
				temp.addObjectListener(myModel);
				selIndex = myModel.nDrawObjects();
			}
			
			// need to draw or clear control points
			repaint();
			
			
			
			
		}
		public void mouseDragged (MouseEvent event) {
			
			if (curSel != null) {
				if (!moveable) {
					double dx = (double)event.getX() - dragStart.getX();
					double dy = (double)event.getY() - dragStart.getY();
					if ((dx*dx + dy*dy) > 9) {
						moveable = true;
					}
				}
				if (moveable) {
					double dx = (double)event.getX() - oldLoc.getX();
					double dy = (double)event.getY() - oldLoc.getY();
					
					// take appropriate action based on what's selected
					if (curControl > -1) {
						curSel.moveControlPoint(curControl, dx, dy);
					}
					else {
						curSel.changeLocation(dx, dy);
					}				
					
					
					oldLoc.setLocation((double)event.getX(), (double)event.getY());
				}
			}
			else if (selRect != null) {
				// special case for the first creation of the selectionRectangle
				if (creatingSel) {
					double dx = (double)event.getX() - oldLoc.getX();
					double dy = (double)event.getY() - oldLoc.getY();
					
					// special case control point for creation
					selRect.moveControlPoint(10, dx, dy);			
				//	AffineTransform ttp = ((SelectionRectangle)selRect).getTransToPerform();
				//	selRect.performTransform(ttp);
					
					oldLoc.setLocation((double)event.getX(), (double)event.getY());
					updateView();
				}
				else if (editableSel) {
					// take appropriate action based on what's selected
					if (curControl == 8) {
						AffineTransform tAt = ((SelectionRectangle)selRect).getReverseTransform();
						
						double newX;
						double newY;
						
						double[] points = {(double)event.getX(),(double)event.getY()};
						double[] tpoints = {0,0};
						
						tAt.transform(points, 0, tpoints, 0, 1);
						
						newX = tpoints[0];
						newY = tpoints[1];
						
						double dx = newX - ((SelectionRectangle)selRect).getRotateX();
						double dy = newY - ((SelectionRectangle)selRect).getRotateY();
					
						selRect.moveControlPoint(curControl, dx, dy);
						
						AffineTransform ttp = ((SelectionRectangle)selRect).getTransToPerform();
						selRect.concatenateTrans(ttp);
						for (int i = 0; i < curSels.size(); i++) {
							// concatenate rotations, perform scaling/moving
							curSels.get(i).concatenateTrans(ttp);
							//curSels.get(i).performTransform(ttp);
						}
						
					}
					else if (curControl > -1) {
						AffineTransform tAt = ((SelectionRectangle)selRect).getReverseTransform();
						
						double newEX;
						double newEY;
						double newOX;
						double newOY;
						
						double[] points = {(double)event.getX(),(double)event.getY(), oldLoc.getX(),oldLoc.getY()};
						double[] tpoints = {0,0, 0,0};
						
						tAt.transform(points, 0, tpoints, 0, 2);
						
						newEX = tpoints[0];
						newEY = tpoints[1];
						newOX = tpoints[2];
						newOY = tpoints[3];
						
						double dx = newEX - newOX;
						double dy = newEY - newOY;
						
						selRect.moveControlPoint(curControl, dx, dy);
						
						AffineTransform ttp = ((SelectionRectangle)selRect).getTransToPerform();
						selRect.performTransform(ttp);
						for (int i = 0; i < curSels.size(); i++) {
							// concatenate rotations, perform scaling/moving
							//curSels.get(i).concatenateTrans(ttp);
							curSels.get(i).performTransform(ttp);
						}
						
					}
					else {
						AffineTransform tAt = ((SelectionRectangle)selRect).getReverseTransform();
						
						double newEX;
						double newEY;
						double newOX;
						double newOY;
						
						double[] points = {(double)event.getX(),(double)event.getY(), oldLoc.getX(),oldLoc.getY()};
						double[] tpoints = {0,0, 0,0};
						
						tAt.transform(points, 0, tpoints, 0, 2);
						
						newEX = tpoints[0];
						newEY = tpoints[1];
						newOX = tpoints[2];
						newOY = tpoints[3];
						
						double dx = newEX - newOX;
						double dy = newEY - newOY;
						
						selRect.changeLocation(dx, dy);
						
						AffineTransform ttp = ((SelectionRectangle)selRect).getTransToPerform();
						selRect.performTransform(ttp);
						for (int i = 0; i < curSels.size(); i++) {
							// concatenate rotations, perform scaling/moving
							//curSels.get(i).concatenateTrans(ttp);
							curSels.get(i).performTransform(ttp);
						}
					}				
										
					oldLoc.setLocation((double)event.getX(), (double)event.getY());
					updateView();
				}
			}
		}
		public void mouseClicked (MouseEvent event) {}

		public void mouseReleased (MouseEvent event) {
			// if you just finished creating a selection rectangle
			if (selRect != null && creatingSel) {
				curSels = new ArrayList<DrawObject>();
				DrawObject temp;
				for (int i = myModel.nDrawObjects() - 1; i >= 0; i--) {
					
					temp = myModel.getDrawObject(i);
					ArrayList<Point2D.Double> cPoints = temp.getControlPoints();
					
					boolean contained = true;
					for (int j = 0; j < cPoints.size(); j++) {
						Point2D.Double cpD = cPoints.get(j);
						Point cp = new Point((int)cpD.getX(), (int)cpD.getY());
						if(!((SelectionRectangle)selRect).contains(cp)) {
							contained = false;
							break;
						}
					}
					
					if (contained) {
						curSels.add(temp);
					}
						
				}
				
				for (int k = 0; k < curSels.size(); k++) {
					System.out.println(curSels.get(k));
				}
				
				
			}
			dragStart = null;
			oldLoc = null;
			moveable = false;
			creatingSel = false;
			editableSel = false;
			
			
			
		}
		public void mouseEntered (MouseEvent event) {}
		public void mouseExited (MouseEvent event) {}
		public void mouseMoved (MouseEvent event) {}
		
		public void setView(DrawingView view) {
			myView = view;
		}

	}
	
	private class MasterKey implements KeyListener {

		public void keyPressed(KeyEvent event) {}

		public void keyReleased(KeyEvent event) {}

		public void keyTyped(KeyEvent event) {}
		
	}

	@Override
	public void modelChanged() {
		// TODO Auto-generated method stub
		updateView();
	}
	
	private void updateView() {
		repaint();
	}

 
	private class MenuListener implements ActionListener {

		private DrawingView parent;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			
			if (e.getSource() == openMI) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "SuperCoolFileExtension", "scfe");
			    chooser.setAcceptAllFileFilterUsed(false);
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(parent);
			    
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	try {
			    		//System.out.println("You chose to open this file: " +
						//	chooser.getSelectedFile().getCanonicalPath());
			    		
			    		//File oldF = new File(myModel.getFileName());
						File f = chooser.getSelectedFile();
					
			    		createNewView(f);
					
					
			    	} catch (IOException e1) {
			    		// TODO Auto-generated catch block
			    		e1.printStackTrace();
			    	}
			    }
			}
			
			
			if (e.getSource() == saveMI) {
				myModel.save();
			}
			
			
			if (e.getSource() == saveAsMI) {
				JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "SuperCoolFileExtension", "scfe");
			    chooser.setAcceptAllFileFilterUsed(false);
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showSaveDialog(parent);
			    
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	try {
			    		// old file
			    		File oldF = new File(myModel.getFileName());
			    		// new file
			    		File f = chooser.getSelectedFile();
			    		//System.out.println("You chose to save this file: " +
						//	f.getCanonicalPath());
			    		
			    		// if saving the same file already shown, just save, don't mess with the views
			    		if (oldF.getCanonicalPath() == f.getCanonicalPath()) {
			    			myModel.save();
			    		}
			    		else {
			    			// copy the old model file through name-setting and saving
				    		String temp = myModel.getFileName();
				    		myModel.setFileName(f.getCanonicalPath());
				    		myModel.save();
				    		myModel.setFileName(temp);
				    		
				    		// remove my parentView as a listener to that model
				    		myModel.removeModelListener(parent);
				    		
							// switch myModel to the new modelfile and update view
				    		myModel = new DrawingModel(f.getCanonicalPath());
				    		// important to add map to new model before added yourself as a listener
				    		myModel.addMapRef(myMapRef);
				    		myModel.addModelListener(parent);
				    		parentFrame.setTitle("Drawing - " + f.getCanonicalPath());
				    		updateView();

			    		}

			    	} catch (IOException e1) {
			    		// TODO Auto-generated catch block
			    		e1.printStackTrace();
			    	}
			    }
			}
			
			if (e.getSource() == colorMI) {
			    Color returnVal = JColorChooser.showDialog(parent,"Choose Color",curColor);
			    
			    if (returnVal != null) {
			    	curColor = returnVal;
			    	
			    	if (curSel != null) {
				    	curSel.changeColor(returnVal);
				    }			    	
			    }

			}
			
			if (e.getSource() == thicknessMI) {
				
				String inputValue = JOptionPane.showInputDialog("Please input a positive value", curThickness);
				if (inputValue != null && inputValue != "") {
					Double temp = new Double(inputValue);
					if (temp > -1) {
						curThickness = temp.doubleValue();
						if (curSel != null) {
					    	curSel.changeThickness(curThickness);
					    }	
					}
										
				}					

			}
			
		}
		
		public void setView(DrawingView view) {
			parent = view;
		}
		
		private void createNewView(File f) throws IOException {
			
			DrawingView view;
			
			// if opening a file that's already open, get the model from the map
			if (myMapRef.containsKey(f.getCanonicalPath())) {
				view = new DrawingView(myMapRef.get(f.getCanonicalPath()), myMapRef);
			}
			else {	// create a new model based on the file
				DrawingModel model = new DrawingModel(f.getCanonicalPath());
				
				view = new DrawingView(model, myMapRef);
			}

			JFrame frame = new JFrame("Drawing - " + f.getCanonicalPath());
			frame.setBounds(100,100,850,750);
			
			
			// let the window listener handle closing actions
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			// add a custom window listener to handle closing events
			frame.addWindowListener(new DVWindowAdapter(view));
						
			// add the view's menu bar to the frame
			frame.setJMenuBar(view.getMenuBar());
			
			// set up the contentpane
			frame.getContentPane().setLayout(new BorderLayout());
			// add the view's palette to the contentpane
			frame.getContentPane().add(view.getPalette(),BorderLayout.WEST);
			// add the view to the frame
			frame.getContentPane().add(view,BorderLayout.CENTER);
			
			// needed for the view to change the frame's title
			view.setParentFrame(frame);
			
			// pass along the reference to the openfile map
			//view.addMapRef(myMapRef);
			
			
			frame.setVisible(true);
		}
		
	}
	
	private class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
			if (e.getSource() == selectB) {
				curMode = 0;
			}
			else if (e.getSource() == lineB) {
				if (curThickness == 0) {
					curThickness = 5;
				}
				curMode = 1;
			}
			else if (e.getSource() == rectB) {
				if (curThickness == 0) {
					curThickness = 5;
				}
				curMode = 4;
			}
			else if (e.getSource() == rectFB) {
				curThickness = 0;
				curMode = 5;
			}
			else if (e.getSource() == curveB) {
				if (curThickness == 0) {
					curThickness = 5;
				}
				curMode = 6;
			}
			
		}
		
	}
	
	
	public void setParentFrame(JFrame parent) {
		parentFrame = parent;
	}
	
	public JFrame getParentFrame() {
		return parentFrame;
	}
	
	public void addMapRef(HashMap<String,DrawingModel> map) {
		myMapRef = map;
		myModel.addMapRef(map);
	}
	
	public DrawingModel getModel() {
		return myModel;
	}
	
}


















