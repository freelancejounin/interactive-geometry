import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;


/**
 * Objects of this class contain information about a list of drawing objects.  The objects are
 * arranged in sequential order that they should be drawn in.
 */
public class DrawingModel implements IObjectListener {
	
	private ArrayList<DrawObject> drawObjects;
	private String fileName;
	private ArrayList<IDrawingView> listeners;
	private HashMap<String,DrawingModel> myMapRef;
	private boolean unsaved;
	
	/**
	 * Creates an empty drawing model that has a unique default file name and no contents
	 */
	public DrawingModel () {
		drawObjects = new ArrayList<DrawObject>();
		listeners = new ArrayList<IDrawingView>();
	}
	
	/**
	 * Reads the specific file and creates a new DrawingModel object that contains all of the 
	 * information in the file. If there is no such file then an exception should be thrown.
	 * @param fileName the name of the file to be read.
	 */
	public DrawingModel(String fileName) {
		
		drawObjects = new ArrayList<DrawObject>();
		setFileName(fileName);
		listeners = new ArrayList<IDrawingView>();
		
		try {
			//read file and put chars into Arraylist
			BufferedReader in = new BufferedReader(new FileReader(fileName));
		
			String temp;
			while ((temp = in.readLine() )!= null) {
				DrawObject obj = parseFileLine(temp);
				obj.addObjectListener(this);
				drawObjects.add(obj);
			}
			
			in.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}
		catch(IOException e) {
			System.out.println("I/O error on file read.");
		}
		
	}
	
	/*
	 * Parses the given fileLine into a Line, Rectangle, Ellipse, or Curve
	 */
	private DrawObject parseFileLine(String fileLine) {
		
		Scanner scan = new Scanner(fileLine);
		
		//try {
			String token = scan.next();
			
			//System.out.println(token);
			
			if (token.equals("L"))
			{
				double x1, y1, x2, y2, thickness;
				int red, green, blue;
				
				x1 = scan.nextDouble();
				y1 = scan.nextDouble();
				x2 = scan.nextDouble();
				y2 = scan.nextDouble();
				thickness = scan.nextDouble();
				if (thickness < 0)
					throw new InputMismatchException();
				red = scan.nextInt();
				green = scan.nextInt();
				blue = scan.nextInt();
				if (!isValidColor(red, green, blue))
					throw new InputMismatchException("Input Mismatch error on read Line.");
				
				return new Line(x1, y1, x2, y2, thickness, red, green, blue);
			}
			else if (token.equals("R"))
			{
				double leftX, topY, width, height, thickness;
				int red, green, blue;
				
				leftX = scan.nextDouble();
				topY = scan.nextDouble();
				width = scan.nextDouble();
				height = scan.nextDouble();
				thickness = scan.nextDouble();
				if (width < 0 || height < 0 || thickness < 0)
					throw new InputMismatchException("Input Mismatch error on read Rectangle/Ellipse.");

				red = scan.nextInt();
				green = scan.nextInt();
				blue = scan.nextInt();
				if (!isValidColor(red, green, blue))
					throw new InputMismatchException("Input Mismatch error on read Rectangle/Ellipse.");
				
				return new Rectangle(leftX, topY, width, height, thickness, red, green, blue);
			}
			else if (token.equals("E")) {
				throw new InputMismatchException("Ellipse no longer supported.");
			}
			else if (token.equals("B"))
			{
				double x1, y1, x2, y2, x3, y3, x4, y4, thickness;
				int red, green, blue;
				
				x1 = scan.nextDouble();
				y1 = scan.nextDouble();
				x2 = scan.nextDouble();
				y2 = scan.nextDouble();
				x3 = scan.nextDouble();
				y3 = scan.nextDouble();
				x4 = scan.nextDouble();
				y4 = scan.nextDouble();
				thickness = scan.nextDouble();
				
				red = scan.nextInt();
				green = scan.nextInt();
				blue = scan.nextInt();
				if (!isValidColor(red, green, blue) || thickness < 0)
					throw new InputMismatchException("Input Mismatch error on read Curve.");
				
				return new Curve(x1, y1, x2, y2, x3, y3, x4, y4, thickness, red, green, blue);
			}
			else if (token.equals("T"))
			{
				throw new InputMismatchException("Text no longer supported.");
			}
			else
				throw new InputMismatchException("Input Mismatch error - unrecognized object description.");
			
		/*	
		}
		catch (InputMismatchException e) {
			System.out.println("Input Mismatch error on read line.");
		}

		return null;
		*/
		
	}
	
	private boolean isValidColor(int red, int green, int blue) {
		
		if (red > 255 || red < 0 || green > 255 || green < 0 || blue > 255 || blue < 0)
			return false;
		
		return true;
	}
	
	
	
	
	/**
	 * Returns the name of the file associated with this model.
	 */
	public String getFileName()	{
		return fileName;
	}
	
	/**
	 * Changes the file name associated with this model
	 * @param newFileName the new file name
	 */
	public void setFileName(String newFileName) {
		fileName = newFileName;
	}
	
	/**
	 * Saves the contents of this model to its file.
	 */
	public void save() {
		
		try {
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			//out.println(drawObjects.size());

			for (int i = 0; i < drawObjects.size(); i++)
				out.println(drawObjects.get(i));
			
			out.close();
			
			unsaved = false;
		}
		catch (IOException e) {
			System.out.println("I/O error on save.");
		}
		
	}
	
	/**
	 * Returns true if there are unsaved changes.
	 */
	public boolean unsavedChanges()	{
		return unsaved;
	}
	
	/**
	 * Adds the specified DrawObject to the end of the list of drawable objects
	 * @param newDrawObject
	 */
	public void addDrawObject(DrawObject newDrawObject) {
		drawObjects.add(newDrawObject);
	}
	
	/**
	 * Returns the number of drawable objects in this model.
	 */
	public int nDrawObjects() {
		return drawObjects.size();
	}
	
	/**
	 * Returns the specified DrawObject. Indexes begin at zero.
	 * @param i index of the desired object. Must be less than nDrawObjects()
	 */
	public DrawObject getDrawObject(int i) {
		return drawObjects.get(i);
	}
	
	/**
	 * Removes the specified object from the list of drawable objects.
	 * @param i the index of the object to be removed.
	 */
	public void removeDrawObject(int i)	{
		drawObjects.remove(i);
	}
	
	/**
	* This method is a regression test to verify that this class is
	* implemented correctly. It should test all of the methods including
	* the exceptions. It should be completely self checking. This 
	* should write "testing DrawingModel" to System.out before it
	* starts and "DrawingModel OK" to System.out when the test
	* terminates correctly. Nothing else should appear on a correct
	* test. Other messages should report any errors discovered.
	**/
	public static void Test() {
		System.out.println("testing DrawingModel");
		
		boolean ok = true;
		int errorCount = 0;
		
		// test DrawingModel()
		DrawingModel temp = new DrawingModel();
		if (temp.nDrawObjects() != 0) {
			System.out.println("DrawModel() test failed.");
			errorCount++;
		}
		
		// create test input file
		try {
			FileWriter fw = new FileWriter("testInputFile.txt");
			BufferedWriter bw = new BufferedWriter (fw);
			PrintWriter out = new PrintWriter (bw);

			out.print("L -50.0 40.0 20.0 50.0 2.0 123 245 123\n" +
					"R 20.0 70.0 20.0 30.0 0.0 255 255 100\n" +
					"B 80.0 80.0 100.0 120.0 140.0 90.0 160.0 130.0 1.0 156 200 33\n");
			
			out.close();
		}
		catch (IOException e) {
			System.out.println("I/O error on creating test input file.");
		}
		
		// test DrawingModel(String fileName)
		try {
			temp = new DrawingModel("testInputFile.txt");
		}
		catch (Exception e) {
			System.out.println("DrawModel(String fileName) test failed with uncaught exception: " + e.getMessage());
			temp = new DrawingModel();
			errorCount++;
		}
		
		// test nDrawObjects(), addDrawObject(DrawObject newDrawObject),
		// getDrawObject(int i), removeDrawObject(int i)
		
		if (temp.nDrawObjects() != 5) {
			System.out.println("nDrawObjects() test failed.");
			errorCount++;
		}
		
		DrawObject dobj = new Rectangle(14, 175, 30, 40, 0, 50, 50, 50);
		
		temp.addDrawObject(dobj);
		
		if (temp.nDrawObjects() != 6) {
			System.out.println("nDrawObjects() test after add failed.");
			errorCount++;
		}
		
		try {
			
			if (!(temp.getDrawObject(5).equals(dobj))) {
				System.out.println("getDrawObject(int i) test failed.");
				errorCount++;
			}
			
			
			temp.removeDrawObject(3);
			if (temp.nDrawObjects() != 5) {
				System.out.println("nDrawObjects() test after remove failed.");
				errorCount++;
			}
		
		}
		catch(Exception e) {
			System.out.println("Indexing tests failed.");
			errorCount++;
		}
		
		// test getFileName(), setFileName(String newFileName)
		if (temp == null || !(temp.getFileName().equals("testInputFile.txt"))) {
			System.out.println("getFileName() test failed.");
			errorCount++;
		}
		
		temp.setFileName("asfliejaflseijffilenaem");
		if (!(temp.getFileName().equals("asfliejaflseijffilenaem"))) {
			System.out.println("setFileName() test failed.");
			errorCount++;
		}
		
		// test parseFileLine(String fileLine)
		boolean caught = false;
		try {
			temp.parseFileLine("L -50 40 20 50 2 123 245 123");
			temp.parseFileLine("R 20 70 20 30 0 255 255 100");
			temp.parseFileLine("B 80 80 100 120 140 90 160 130 1 156 200 33");
		}
		catch (InputMismatchException e) {
			System.out.println("parseFileLine() on valid lines should not have thrown exception - test failed.");
			errorCount++;
		}
		
		try {	// Line
			temp.parseFileLine("L -50 40 20 50 -4 123 245 123"); // thickness < 0
			temp.parseFileLine("L -50 40 20 50 2 123 -8 123");	// invalid color
		}
		catch (InputMismatchException e) {
			caught = true;
		}
		
		try {	// Rectangle
			temp.parseFileLine("R 20 70 -8 30 0 255 255 100");	// width < 0
			temp.parseFileLine("R 20 70 20 -5 0 255 255 100");	// height < 0
			temp.parseFileLine("R 20 70 20 30 -7 255 255 100");	// thickness < 0
			temp.parseFileLine("R 20 70 20 30 0 255 300 100");	// invalid color
		}
		catch (InputMismatchException e) {
			caught = true;
		}
		
		try {	// Curve
			temp.parseFileLine("B 80 80 100 120 140 90 160 130 1 156 200 -3");	// invalid color
			temp.parseFileLine("B 80 80 100 120 140 90 160 130 -1 156 200 3");	// invalid thickness
		}
		catch (InputMismatchException e) {
			caught = true;
		}
		
		if(!caught) {
			System.out.println("parseFileLine() did not throw exception one or move times it should have - test failed.");
			errorCount++;
		}
		
		
		// test isValidColor
		if (!temp.isValidColor(0,0,0)) {
			System.out.println("isValidColor() 0's test failed.");
			errorCount++;
		}
		if (!temp.isValidColor(255,255,255)) {
			System.out.println("isValidColor() 255's test failed.");
			errorCount++;
		}
		if (temp.isValidColor(256,255,255)) {
			System.out.println("isValidColor() red bounds test failed.");
			errorCount++;
		}
		if (temp.isValidColor(-1,0,0)) {
			System.out.println("isValidColor() red bounds test failed.");
			errorCount++;
		}
		if (temp.isValidColor(255,256,255)) {
			System.out.println("isValidColor() green bounds test failed.");
			errorCount++;
		}
		if (temp.isValidColor(0,-1,0)) {
			System.out.println("isValidColor() green bounds test failed.");
			errorCount++;
		}
		if (temp.isValidColor(255,255,256)) {
			System.out.println("isValidColor() blue bounds test failed.");
			errorCount++;
		}
		if (temp.isValidColor(0,0,-1)) {
			System.out.println("isValidColor() blue bounds test failed.");
			errorCount++;
		}
		
		// test save()
		// reset Model from testInputFile to compare with testOutputFile
		temp = new DrawingModel("testInputFile.txt");
		// output different file to compare against original
		temp.setFileName("testOutputFile.txt");
		temp.save();
		
		try {
			//read file and put chars into Arraylist
			BufferedReader in1 = new BufferedReader(new FileReader("testInputFile.txt"));
			BufferedReader in2 = new BufferedReader(new FileReader("testOutputFile.txt"));

			boolean equal = true;
			String str1, str2;
			
			while(equal) {
				str1 = in1.readLine();
				str2 = in2.readLine();
				
				if (str1 == null && str2 == null)
					break;
				
				if (!str1.equals(str2)) {
					equal = false;
					break;
				}
			}

			if (!equal) {
				System.out.println("save() test failed.");
				errorCount++;
			}
				
			
			in1.close();
			in2.close();
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found.");
		}
		catch(IOException e) {
			System.out.println("I/O error on file read.");
		}
		
		
		
		
		
		
		
		// test unsavedChanges()
		if (temp.unsavedChanges()) {
			System.out.println("unsavedChanges() test failed.");
			errorCount++;
		}
		
		
		// finish test, output results
		if (ok)
			System.out.println("DrawingModel OK");
		else {
			System.out.println("Testing failed with " + errorCount + " errors.");
		}
	}
	
	
	/**
	 * This method takes a ViewListener and adds it as a listener
	 */
	public void addModelListener(IDrawingView iView) {
		listeners.add(iView);
		
		// if this model isn't in the openfile map, put it there because it's being listened to
		if (!(myMapRef.containsKey(fileName))) {
			myMapRef.put(fileName, this);
		}
	}
	
	/**
	 * This method takes a ViewListener and removes it as a listener
	 */
	public void removeModelListener(IDrawingView iView) {
		for (int i = 0; i < listeners.size(); i++) {
			if (listeners.get(i) == iView) {
				listeners.remove(i);
			}
		}
		
		if (listeners.size() == 0) {
			// TODO ask about saving!!!
			
			// remove this model from the map if it isn't listened to anymore
			myMapRef.remove(fileName);
		}
		
	}
	
	/**
	 * This method notifies the ViewListeners that the model has changed
	 */
	public void modelChanged() {
		unsaved = true;
		
		for (int i = 0; i < listeners.size(); i++)
			listeners.get(i).modelChanged();
		
	}
	
	/**
	 * This is called by the objects as they change
	 */
	public void objectChanged() {
		// then notify things listening to the model
		modelChanged();
		
	}
	
	public void addMapRef(HashMap<String,DrawingModel> map) {
		myMapRef = map;
	}
	
	public int getListenerCount() {
		return listeners.size();
	}
	
}











