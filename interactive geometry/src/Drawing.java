import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;

import javax.swing.JFrame;


public class Drawing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
				
			if (args.length == 0)
				throw new IllegalArgumentException("File name required");
			
			if (args[0].equals("test")) {
				DrawingModel.Test();
			}
			else {
				HashMap<String,DrawingModel> openFiles = new HashMap<String,DrawingModel>();
				
				DrawingModel model = new DrawingModel(args[0]);
				DrawingView view = new DrawingView(model,openFiles);
				// pass the reference to the openfile map
				//view.addMapRef(openFiles);
				File f = new File(model.getFileName());
				
				
				openFiles.put(f.getCanonicalPath(), model);
				
				
				JFrame frame = new JFrame("Drawing - " + f.getCanonicalPath());
				frame.setBounds(100,100,850,750);
				
				
				/*
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				frame.addWindowListener(
						new WindowAdapter()	{
							public void windowClosing(WindowEvent evt) {
								System.exit(0);
							}
						}
				);
				*/
				
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
				
				
				frame.setVisible(true);
			}
		}
		catch (IllegalArgumentException e) {
			if (e.getMessage() != null)
				System.out.println("Illegal Argument! Message: " + e.getMessage());
			else
				System.out.println("Illegal Argument Exception!");
		}
		catch (InputMismatchException e) {
			if (e.getMessage() != null)
				System.out.println("Input Mismatch! Message: " + e.getMessage());
			else
				System.out.println("Input Mismatch Exception!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
