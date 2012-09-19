import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;


public class DVWindowAdapter extends WindowAdapter {
	private DrawingView myView;
	
	public DVWindowAdapter(DrawingView view) {
		super();
		
		myView = view;
	}
	
	public void windowClosing(WindowEvent evt) {
		DrawingModel temp = myView.getModel();
		
		if ((temp.getListenerCount() == 1) && temp.unsavedChanges()) {
			int retVal = JOptionPane.showConfirmDialog(myView, "This model has unsaved changes, would you like to save it now?");
			//System.out.println("This is my response: " + retVal);
			
			if (retVal == 0) {		// yes to save and close
				temp.save();
				temp.removeModelListener(myView);
				myView.getParentFrame().dispose();
			}
			else if (retVal == 1) {		// no to save, but close
				temp.removeModelListener(myView);
				myView.getParentFrame().dispose();
			}
			
			// cancel save and close = do nothing
			
		}
		else {		// no need to ask, just close
			temp.removeModelListener(myView);
			myView.getParentFrame().dispose();
		}
	}
}
