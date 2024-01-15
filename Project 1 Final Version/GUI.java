//-------------------+
//-Anthony Webb      |
//-Project 1	     |
//-CNT 4714	         |
//-Language: Java 	 |
//-Due: Sep 17th	 |
//-------------------+

package shopGui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GUI extends JFrame implements ActionListener {
	
	JButton exitApp, findItem, addtoCart,emptyCart, viewCart, checkout;
	JTextField idEntry, quantEntry, details, subtotal;
	
	GUI(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(0,2));
		this.setTitle("Bailey Webb Store");
		
		idEntry = new JTextField();
		idEntry.setPreferredSize(new Dimension(40,40));
		JLabel idLabel = new JLabel("Enter the ID for Item #1");
		
		quantEntry = new JTextField();
		idEntry.setPreferredSize(new Dimension(40,40));
		JLabel quantLabel = new JLabel("Enter the Quantity for Item #1 ");
		
		details = new JTextField();
		idEntry.setPreferredSize(new Dimension(40,40));
		JLabel detailsLabel = new JLabel("Details for Item #1");
		
		subtotal = new JTextField();
		idEntry.setPreferredSize(new Dimension(40,40));
		JLabel subtotalLabel = new JLabel("Subtotal for 0 item(s)");

		
		//GUI Customization
		idEntry.setFont(new Font("Consolas",Font.PLAIN,27));
		idLabel.setFont(new Font("Consolas",Font.BOLD,16));
		quantEntry.setFont(new Font("Consolas",Font.PLAIN,27));
		quantLabel.setFont(new Font("Consolas",Font.BOLD,16));
		details.setFont(new Font("Consolas",Font.PLAIN,27));
		detailsLabel.setFont(new Font("Consolas",Font.BOLD,16));
		subtotal.setFont(new Font("Consolas",Font.PLAIN,27));
		subtotalLabel.setFont(new Font("Consolas",Font.BOLD,16));
		
		//Button 1
		findItem = new JButton("Find Item");
		findItem.addActionListener(this);
		//Button 2
		addtoCart = new JButton("Add to Cart");
		addtoCart.addActionListener(this);
		//Button 3
		viewCart = new JButton("View Cart");
		viewCart.addActionListener(this);
		//Button 4
		checkout = new JButton("Checkout");
		checkout.addActionListener(this);
		//Button 5
		emptyCart = new JButton("Empty Cart - Start a New Order");
		emptyCart.addActionListener(this);
		//Button 6
		exitApp = new JButton("Exit (Close App)");
		exitApp.addActionListener(this);
		
		this.add(idLabel);
		this.add(idEntry);
		this.add(quantLabel);
		this.add(quantEntry);
		this.add(detailsLabel);
		this.add(details);
		this.add(subtotalLabel);
		this.add(subtotal);
		this.add(findItem);
		this.add(addtoCart);
		this.add(viewCart);
		this.add(checkout);
		this.add(emptyCart);
		this.add(exitApp);
		this.pack();
		this.setVisible(true);
	}
	
	//Helper methods
	@Override
	public void actionPerformed(ActionEvent e) {	//awaits button presses then does an action
		if(e.getSource()==findItem) {

			
			String finditemInput = idEntry.getText();
			System.out.println("Searching for item ID# " + finditemInput);
			readinventroyFile(finditemInput);
		}
		
		if(e.getSource()==exitApp) { 
			System.exit(0);
		}
	}
	
	public static void readinventroyFile(String searchTermID) {				//reads the inventory file and searches for input entered in to argument
		File inputFile = new File("inventory.csv");
		FileReader inputfileReader 		  = null;
		BufferedReader inputbufferReader  = null;
		String invLine;
		
		try {
			inputfileReader = new FileReader(inputFile);
			inputbufferReader = new BufferedReader(inputfileReader);
			
			invLine = inputbufferReader.readLine();
			
			whileloop:while(invLine != null) {
				invLine = inputbufferReader.readLine();
			}
		}
		
		catch(FileNotFoundException fileNotFoundException) {
			JOptionPane.showMessageDialog(null,"Error: File not found", "Shop - ERROR",JOptionPane.ERROR_MESSAGE);
		}
		catch(IOException ioException) {
			JOptionPane.showMessageDialog(null,"Error: Problem reading from file", "Shop - ERROR",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	
	
}//end of GUI class
