package shopGui;

public class CartItem {
    private String itemName;
    private int quantity;
    private double price;
    private String id;

    public CartItem(String itemName, int quantity, double price, String id) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
    }

    // Getter method for itemName
    public String getItemName() {
        return itemName;
    }

    // Getter method for quantity
    public int getQuantity() {
        return quantity;
    }

    // Getter method for price
    public double getPrice() {
        return price;
    }
    
    public String getId() {
    	return id;
    }
}

