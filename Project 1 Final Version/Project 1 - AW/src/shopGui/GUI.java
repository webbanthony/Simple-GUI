/* Name: Anthony Webb
 Course: CNT 4714 – Fall 2023
 Assignment title: Project 1 – Event-driven Enterprise Simulation
 Date: Sunday September 17, 2023
*/

package shopGui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.Color;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GUI extends JFrame implements ActionListener {

	JButton exitApp, findItem, addToCart, emptyCart, viewCart, checkout;
	JTextField idEntry, quantEntry;
	JTextArea details, subtotal;
	JLabel idLabel, quantLabel, detailsLabel, subtotalLabel;
	
	public static int count = 1;
	int numberOfItems = 0;
	double subtotalSum = 0;
	
	public static final Color VERY_DARK_GREEN = new Color(51,153,200);
	NumberFormat formatter = NumberFormat.getCurrencyInstance();

	File outputFile = new File("transaction.csv");
	
	List<CartItem> cartItems = new ArrayList<>();

	int discountQuantThreshold1 = 5;
	int discountQuantThreshold2 = 10;
	int discountQuantThreshold3 = 15;

	double discountPercentage1 = 0.10;
	double discountPercentage2 = 0.15;
	double discountPercentage3 = 0.20;

	GUI(){
		
		Font font = new Font("Tahoma", Font.BOLD, 13);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(0,2,10,10));
		this.setTitle("Nile Shop - Fall 2023");
		
		idEntry = new JTextField();
		idEntry.setPreferredSize(new Dimension(100,50));
		idLabel = new JLabel("Enter the ID for Item #" + count);
		idLabel.setForeground(Color.BLUE);
		this.add(idLabel);
		this.add(idEntry);
		
		quantEntry = new JTextField();
		quantEntry.setPreferredSize(new Dimension(100,50));
		quantLabel = new JLabel("Enter the Quantity for Item #" + count);
		quantLabel.setForeground(Color.BLUE);
		this.add(quantLabel);
		this.add(quantEntry);
		
		details = new JTextArea(); 
		details.setPreferredSize(new Dimension(100,50));
		detailsLabel = new JLabel("Details for Item #" + count);
		detailsLabel.setForeground(Color.RED);
		this.add(detailsLabel);
		this.add(details);
		
		subtotal = new JTextArea();
		subtotal.setPreferredSize(new Dimension(100,50));
		subtotalLabel = new JLabel("Subtotal for 0 item(s)");
		subtotalLabel.setForeground(Color.RED);
		this.add(subtotalLabel);
		this.add(subtotal);
		
		

		//Button 1
		findItem = new JButton("Find Item #" + count);
		this.findItem.addActionListener(this);
		this.add(findItem);
		findItem.setFont(font);
        findItem.setBackground(VERY_DARK_GREEN);
        findItem.setForeground(Color.WHITE);
        findItem.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//Button 2
		addToCart = new JButton("Add item #" + count + " to Cart");
		addToCart.addActionListener(this);
		this.add(addToCart);
		addToCart.setFont(font);
        addToCart.setBackground(VERY_DARK_GREEN);
        addToCart.setForeground(Color.WHITE);
        addToCart.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//Button 3
		viewCart = new JButton("View Cart");
		this.viewCart.addActionListener(this);
		this.add(viewCart);
		viewCart.setFont(font);
        viewCart.setBackground(VERY_DARK_GREEN);
        viewCart.setForeground(Color.WHITE);
        viewCart.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//Button 4
		checkout = new JButton("Checkout");
		this.checkout.addActionListener(this);
		this.add(checkout);
		checkout.setFont(font);
        checkout.setBackground(VERY_DARK_GREEN);
        checkout.setForeground(Color.WHITE);
        checkout.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//Button 5
		emptyCart = new JButton("Empty Cart (Start a New Order)");
		this.emptyCart.addActionListener(this);
		this.add(emptyCart);
		emptyCart.setFont(font);
        emptyCart.setBackground(VERY_DARK_GREEN);
        emptyCart.setForeground(Color.WHITE);
        emptyCart.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//Button 6
		exitApp = new JButton("Exit (Close App)");
		this.exitApp.addActionListener(this);
		this.add(exitApp);
		exitApp.setFont(font);
        exitApp.setBackground(VERY_DARK_GREEN);
        exitApp.setForeground(Color.WHITE);
        exitApp.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		this.pack();
		this.setVisible(true);
		
		viewCart.setEnabled(false);
		addToCart.setEnabled(false);
		checkout.setEnabled(false);
		
	}

	// - Button actions
	@Override
	public void actionPerformed(ActionEvent e) {

		// find item and update count(s)
		if (e.getSource() == findItem) {
			String finditemInput = idEntry.getText();
			String quant = quantEntry.getText();

			details.setText(searchInventroyFile(finditemInput,findItem,addToCart));

			idLabel.setText("Enter the ID for Item #" + count);
			quantLabel.setText("Enter the Quantity for Item #" + count);
			detailsLabel.setText("Details for Item #" + (count));
		}

		// exit
		if (e.getSource() == exitApp) {
			System.exit(0);
		}

		// reset counter and text(s)
		if (e.getSource() == emptyCart) {
			count = 1;
			idLabel.setText("Enter the ID for Item #" + count);
			quantLabel.setText("Enter the Quantity for Item #" + count);
			detailsLabel.setText("Details for Item #" + (count));
			dispose();
			new GUI();
		}

		if (e.getSource() == addToCart) {
			String quant = quantEntry.getText();
			String enteredID = idEntry.getText();

			File readFile = new File("inventory.csv");
			String ID = "";
			String description = "";
			String inStock = "";
			String quantity = "";
			String price = "";

			Boolean found = false;
			try {
				Scanner x = new Scanner(readFile);
				x.useDelimiter("[,\n]");

				while (x.hasNext() && !found) {
					ID = x.next();
					description = x.next();
					inStock = x.next();
					quantity = x.next();
					price = x.next();

					if (ID.equals(enteredID))
						found = true;
				}

				quantity = quantity.trim();
				quant = quant.trim();
				price = price.trim();

				int enteredQuantity = Integer.parseInt(quant);
				int availableQuantity = Integer.parseInt(quantity);

				

				if (enteredQuantity > availableQuantity) {
					JOptionPane.showMessageDialog(null,
							"Insufficient Stock. Please Enter a Quantity that is Less than the Available Stock: \n "
									+ "(" + availableQuantity + " Items in Stock)",
							"Nile Shop - Add To Cart - ERROR", JOptionPane.ERROR_MESSAGE);
					quantEntry.setText("");
					addToCart.setEnabled(true);
				} else {
					
					CartItem cartItem = new CartItem(description, enteredQuantity, Double.parseDouble(price), ID);
		            cartItems.add(cartItem);
		            count++;
		            idEntry.setText("");
		            quantEntry.setText("");
		            details.setText("");
		            idLabel.setText("Enter the ID for Item #" + count);
		            quantLabel.setText("Enter the Quantity for Item #" + count);
		            detailsLabel.setText("Details for Item #" + count);
		            findItem.setText("Find Item #" + count);
		            addToCart.setText("Add Item #" + count +" To Cart");

		    
		            numberOfItems += enteredQuantity;
		            
		            double itemTotal = calculateItemTotal(cartItem);
	                subtotalSum += itemTotal;
	                subtotal.setText(formatter.format(subtotalSum));
	                subtotalLabel.setText("Subtotal for " + numberOfItems + " item(s)");
	                
	                JOptionPane.showMessageDialog(null, "Item #" + (count - 1) + " accepted. Added to your cart.", "Nile Shop - Item Confirmed", JOptionPane.INFORMATION_MESSAGE);

				}

			} catch (FileNotFoundException fileNotFoundException) {
				JOptionPane.showMessageDialog(null, "Error: File not Found", "Shop - ERROR", JOptionPane.ERROR_MESSAGE);
			}
			findItem.setEnabled(true);
			checkout.setEnabled(true);
			viewCart.setEnabled(true);
			addToCart.setEnabled(false);
		}
		
	
		if (e.getSource() == viewCart) {
		    StringBuilder viewCartMessage = new StringBuilder("Items in cart:\n");

		    for (int i = 0; i < cartItems.size(); i++) {
		        CartItem cartItem = cartItems.get(i);
		        String itemName = cartItem.getItemName();
		        int itemCartQuantity = cartItem.getQuantity();
		        double price = cartItem.getPrice();

		        viewCartMessage.append("Item #").append(i + 1).append(": ")
		                       .append(itemName).append(", ")
		                       .append("Quantity: ").append(itemCartQuantity).append(", ")
		                       .append("Price: ").append(formatter.format(itemCartQuantity * price)).append("\n");
		    }

		    JOptionPane.showMessageDialog(null, viewCartMessage.toString(), "Nile Shop - Current Cart Status", JOptionPane.INFORMATION_MESSAGE);
		}
		
		
		if (e.getSource() == checkout) {
		    
			String total = subtotal.getText();
			
			double tax = 0.06;
			
			Date now = new Date();
		    String formattedDate = new SimpleDateFormat("EEEEE, MMMMM dd, yyyy, HH:mm:ss").format(now);
		    
		    StringBuilder checkoutMessage = new StringBuilder("Date: " + formattedDate + "\n\n");
		    
		    checkoutMessage.append("Number of line items: " + cartItems.size()).append("\n\n");
		    
		    for (int i = 0; i < cartItems.size(); i++) {
		        CartItem cartItem = cartItems.get(i);
		        String itemName = cartItem.getItemName();
		        int itemCartQuantity = cartItem.getQuantity();
		        double price = cartItem.getPrice();

		        checkoutMessage.append("#").append(i + 1).append(": ")
		                	   .append("Name: ").append(itemName).append(", ")
		                       .append("Quantity: " + itemCartQuantity).append(", ")
		                       .append("Price (per item): ").append(formatter.format(price)).append(", ")
		                       .append("Total Price: " + formatter.format(itemCartQuantity * price) + "\n");
		        
		        if (itemCartQuantity >= discountQuantThreshold3) {
		            checkoutMessage.append("Discount Applied: 20%").append("\n");
		        } else if (itemCartQuantity >= discountQuantThreshold2) {
		        	checkoutMessage.append("Discount Applied: 15%").append("\n");
		        } else if (itemCartQuantity >= discountQuantThreshold1) {
		        	checkoutMessage.append("Discount Applied: 10%").append("\n");
		        }
		    }
		    
		    double taxAmount = subtotalSum * tax;
	        double totalAmount = subtotalSum + taxAmount;
		    
		    checkoutMessage.append("\nOrder subtotal: " + total + "\n\n")
		    			   .append("Tax: " + (tax * 100) + "%\n\n")
		    			   .append("Sales tax amount: " + formatter.format(taxAmount) + "\n\n")
		    			   .append("ORDER TOTAL: " + formatter.format(totalAmount) + "\n\n")
		            	   .append("Thank you for shopping with Nile!");
		    
		    writeCheckoutToCSV(cartItems, subtotalSum, tax, totalAmount);

		    JOptionPane.showMessageDialog(null, checkoutMessage.toString(), "Checkout", JOptionPane.DEFAULT_OPTION);
		    
			findItem.setEnabled(false);
			checkout.setEnabled(false);
			viewCart.setEnabled(false);
			addToCart.setEnabled(false);
		}
	}



	//find item
	public static String searchInventroyFile(String searchTermID, JButton button, JButton button2) {
		File inputFile = new File("inventory.csv");
		String ID = "";
		String description = "";
		String inStock = "";
		String quantity = "";
		String price = "";

		Boolean found = false;
		Boolean stock = true;
		String detailsMessage = "";

		try {
			Scanner x = new Scanner(inputFile);
			x.useDelimiter("[,\n]");

			while (x.hasNext() && !found) {
				ID = x.next();
				description = x.next();
				inStock = x.next();
				quantity = x.next();
				price = x.next();

				if (ID.equals(searchTermID))
					found = true;
			}

			if (inStock.equalsIgnoreCase(" false"))
				stock = false;

			if (found && stock == false) {
				detailsMessage = ("ID: " + ID + "\n" + "Item: " + description + "\n" + "Price: $" + price + "\n"
							   + "In stock: " + inStock);
				
				JOptionPane.showMessageDialog(null, "Item not in stock, please check later.", "Shop - ERROR",
						JOptionPane.ERROR_MESSAGE);
				
				button.setEnabled(true);
				button2.setEnabled(false);
			}

			else if (found && stock == true) {
				detailsMessage = ("ID: " + ID + "\n" + "Item: " + description + "\n" + "Price: $" + price + "\n"
						       + "In stock: " + inStock);
				
				button.setEnabled(false);
				button2.setEnabled(true);
			}

			else {
				JOptionPane.showMessageDialog(null, "Item not found. Make sure to enter a valid ID.", "Shop - ERROR",
						JOptionPane.ERROR_MESSAGE);
				
				button.setEnabled(true);
				button2.setEnabled(false);
			}
		}

		catch (FileNotFoundException fileNotFoundException) {
			JOptionPane.showMessageDialog(null, "Error: File not found", "Shop - ERROR", JOptionPane.ERROR_MESSAGE);
		}

		return detailsMessage;

	}
	
	//to deal with the instance if there is a discount involved
	private double calculateItemTotal(CartItem cartItem) {
	    int quantity = cartItem.getQuantity();
	    double price = cartItem.getPrice();
	    double total = quantity * price;

	    if (quantity >= discountQuantThreshold1) {
	        total -= total * discountPercentage1;
	    } else if (quantity >= discountQuantThreshold2) {
	        total -= total * discountPercentage2;
	    } else if (quantity >= discountQuantThreshold3) {
	        total -= total * discountPercentage3;
	    }

	    return total;
	}
	
	private void writeCheckoutToCSV(List<CartItem> cartItems, double subtotal, double tax, double total) {		
		try (FileWriter writer = new FileWriter(outputFile, true)) {
	        
			SimpleDateFormat uniqueIdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		    String transactionUniqueId = uniqueIdFormat.format(new Date());
		    
		    SimpleDateFormat readableDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy, HH:mm:ss");
		    String formattedDate = readableDateFormat.format(new Date());
		    
			for (int i = 0; i < cartItems.size(); i++) {
	            CartItem cartItem = cartItems.get(i);
	            String itemName = cartItem.getItemName();
	            int itemCartQuantity = cartItem.getQuantity();
	            double price = cartItem.getPrice();
	            String ID = cartItem.getId();

	            writer.append(transactionUniqueId + ", ")
	            	  .append(ID)
	            	  .append(", ")
	            	  .append(itemName)           
	            	  .append(", ")
	            	  .append(Integer.toString(itemCartQuantity) + ", ")
	            	  .append(formattedDate)
	            	  .append("\n");
	        }
	            
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "Error writing to file", "Checkout Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
}
	



