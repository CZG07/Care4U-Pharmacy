public class product {
    private String name;
    private String code;
    private int stock;
    private double price;

    public product(String name, String code, int stock, double price){
        this.name=name;
        this.code=code;
        this.stock=stock;
        this.price=price;
    }
    public String getName(){
        return name;
    }
    public String getCode(){
        return code;
    }
    public int getStock(){
        return stock;
    }
    public double getPrice(){
        return price;
    }
    public void setStock(){
        this.stock=stock;
    }
}

