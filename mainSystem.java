import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.awt.*;

//menu for user to choose inventory management or cashier system
class menu extends JFrame{
    private JButton jbtManage = new JButton("INVENTORY MANAGEMENT");
    private JButton jbtCashier = new JButton("CASHIER SYSTEM");

    public menu() {
        setTitle("Care4U Pharmacy" + "\nMAIN MENU");
        setSize(450, 350);
        setLayout(new GridLayout(2, 1, 10, 10));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(jbtManage);
        add(jbtCashier);

        setVisible(true);
        //Manage function
        jbtManage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new mainSystem(true);
                dispose();
            }
        });

        //Cashier Function
        jbtCashier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new mainSystem(false);
                dispose();
            }
        });
    }
}

class mainSystem extends JFrame{

    //product details
    private JTextField jtfName = new JTextField();
    private JTextField jtfCode = new JTextField();
    private JTextField jtfStock = new JTextField();
    private JTextField jtfPrice = new JTextField();
    private  JTextField jtfQuantity = new JTextField();


    //GUI button for interaction
    private JButton jbtAdd = new JButton("ADD");
    private JButton jbtDelete= new JButton("DELETE");
    private JButton jbtSearch = new JButton("SEARCH");
    private JButton jbtUpdate = new JButton("UPDATE");
    private JButton jbtCalculate= new JButton("CALCULATE");
    private JButton jbtSell = new JButton("SELL");


    //GUI Interface for interaction
    private JPanel jpInput = new JPanel(new GridLayout(5,2,40,20));
    private JPanel jpButton = new JPanel();


    //Database Detail
    String url="jdbc:mysql://localhost:3306/pharmacy";
    String name="root";
    String password = "admin@123";

    //Connection to database and execute SQL queries
    Connection con;
    Statement stmt;

private double nextCustomer = 0.0;
    public mainSystem(boolean isManager) {
        connectDBS();

        setLayout(new BorderLayout());
        jpInput.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));

        //If Inventory Management is selected
        if(isManager) {

            //Inventory Management system page
            setTitle("Care4U Pharmacy: INVENTORY MANAGEMENT");
            setSize(450,350);

            //Input Product Name
            jpInput.add(new JLabel("Product Name:", JLabel.CENTER));
            jpInput.add(jtfName);

            //Input Product Code
            jpInput.add(new JLabel("Product Code:", JLabel.CENTER));
            jpInput.add(jtfCode);

            //Input Product Price
            jpInput.add(new JLabel("Product Price:", JLabel.CENTER));
            jpInput.add(jtfPrice);

            //Input Product Stock
            jpInput.add(new JLabel("Product Stock:", JLabel.CENTER));
            jpInput.add(jtfStock);

            //Button to Add, Delete, Update and Search for Inventory Management
            jpButton.add(jbtAdd);
            jpButton.add(jbtDelete);
            jpButton.add(jbtSearch);
            jpButton.add(jbtUpdate);

        }
        //If Cashier System is selected
        else{
            //Cashier System page
            setTitle("Care4U Pharmacy: Cashier System");
            setSize(450,350);

            //Cashier System Layout
            jpInput.setLayout(new GridLayout(5,2,20,20));
            jpInput.add(new JLabel("Name:"));
            jpInput.add(jtfName);
            jpInput.add(new JLabel("Code:"));
            jpInput.add(jtfCode);
            jpInput.add(new JLabel("Price/unit: RM"));
            jpInput.add(jtfPrice);
            jpInput.add(new JLabel("Available Stock: "));
            jpInput.add(jtfStock);
            jpInput.add(new JLabel("Total Quantity: "));
            jpInput.add(jtfQuantity);

            //Button of Cashier System
           // jpButton.add(jbtCalculate);
            jpButton.add(jbtSell);
            jpButton.add(jbtSearch);

            //After searching the product code, cashier cannot edit the name, price and stock of the product
            jtfName.setEditable(false);
            jtfPrice.setEditable(false);
            jtfStock.setEditable(false);
        }

        //JFrame Panel
        add(jpInput, BorderLayout.CENTER);
        add(jpButton, BorderLayout.SOUTH);

        //Main Interface Platform
        setTitle("Care4U Pharmacy");
        setSize(450,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        jpInput.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        //Button Action Listener
        jbtAdd.addActionListener(new InsertBtnListener());
        jbtDelete.addActionListener(new DeleteBtnListener());
        jbtSearch.addActionListener(new SearchBtnListener());
        jbtUpdate.addActionListener(new UpdateBtnListener());
        jbtSell.addActionListener(new SellBtnListener());
        jbtCalculate.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if(nextCustomer==0){
                    JOptionPane.showMessageDialog(null,"READY FOR NEXT CUSTOMER.");
                    return;
                }
                JOptionPane.showMessageDialog(null,"TOTAL AMOUNT: RM"+nextCustomer);

                nextCustomer=0.0;
            }
        });
        setSize(450,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        if(isManager==false){
            setTitle("Care4U Pharmacy: CASHIER SYSTEM");
            jbtAdd.setVisible(false);
            jbtDelete.setVisible(false);
            jbtUpdate.setVisible(false);

        }else{
            setTitle("Care4U Pharmacy: INVENTORY MANAGEMENT");
        }
        setVisible(true);
    }

    //Database Connection
    private void connectDBS(){
        try{
            con=DriverManager.getConnection(url,name, password);
            stmt=con.createStatement();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Database Error:"+ex.getMessage());
        }
    }

    //Add Product Function
    private class InsertBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (stmt == null) {
                JOptionPane.showMessageDialog(null, "ERROR: DATABASE CONNECTION FAILED...");
                return;
            }
            String sql = "INSERT INTO product VALUES('" + jtfCode.getText() + "','" + jtfName.getText() + "'," + jtfPrice.getText() + "," + jtfStock.getText() + ")";
            try {
                int row = stmt.executeUpdate(sql);
                if (row > 0) JOptionPane.showMessageDialog(null, "Product Added!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database Error: " + ex.getMessage());
            }
        }
    }

    //Delete Product Function
    private class DeleteBtnListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String sql = "DELETE  FROM product WHERE CODE ='"+jtfCode.getText()+"'";

            try{
                if (stmt == null) {
                    JOptionPane.showMessageDialog(null, "ERROR: DATABASE CONNECTION FAILED...");
                    return;
                }
                int delete= JOptionPane.showConfirmDialog(null,"Deleting...");

                if(delete==JOptionPane.YES_OPTION) {
                    int row = stmt.executeUpdate(sql);

                    if (row > 0) {
                        JOptionPane.showMessageDialog(null, "Product Deleted!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Deletion Failed!");
                    }
                }
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null,"Database Error..."+ex.getMessage());
            }
        }
    }

    //Search Function
    private class SearchBtnListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String sql = "SELECT * FROM product WHERE CODE ='" + jtfCode.getText() + "'";
            try{
                ResultSet rs = stmt.executeQuery(sql);

                if(rs.next()){
                    jtfName.setText(rs.getString("NAME"));
                    jtfCode.setText(rs.getString("CODE"));
                    jtfPrice.setText(rs.getString("PRICE_RM"));
                    jtfStock.setText(rs.getString("STOCK"));
                }else{
                    JOptionPane.showMessageDialog(null,"Product Not Found!");
                }
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null,"Database Error..."+ex.getMessage());
            }

        }
    }

    //Update Function
    private class UpdateBtnListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String sql="UPDATE product SET "+"NAME ='"+jtfName.getText()+"', "+"PRICE_RM ="+jtfPrice.getText()+", "+"STOCK ="+jtfStock.getText()+" "+"WHERE CODE ='"+jtfCode.getText()+"'";

            try{
                if(stmt == null){
                    JOptionPane.showMessageDialog(null,"Database Not Connected!");
                    return;
                }
                int row =stmt.executeUpdate(sql);
                if(row>0){
                    JOptionPane.showMessageDialog(null,"Product Updated!");
                }else{
                    JOptionPane.showMessageDialog(null,"Update Failed!");
                }
            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null,"Database Error..."+ex.getMessage());
            }
        }
    }

    //Sell Product Function
    private class SellBtnListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (jtfQuantity.getText().isEmpty() || jtfPrice.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please search for a product and enter a quantity first!");
                return;
            }
            try{
                int buy=Integer.parseInt(jtfQuantity.getText());
                int currStock = Integer.parseInt(jtfStock.getText());
                double price = Double.parseDouble(jtfPrice.getText());

                if(buy<=currStock){
                    double total = buy*price;
                    int remainStock=currStock - buy;

                    stmt.executeUpdate("UPDATE product SET STOCK = "+ remainStock+" WHERE CODE = '"+jtfCode.getText()+"' ");

                    JOptionPane.showMessageDialog(null,"Total Price = RM"+total);
                    JOptionPane.showMessageDialog(null,"Stock Remaining:"+remainStock);

                    jtfName.setText("");
                    jtfCode.setText("");
                    jtfPrice.setText("");
                    jtfStock.setText("");
                    jtfQuantity.setText("");

                }else{
                    JOptionPane.showMessageDialog(null,"OUT OF STOCK! PLEASE RESTOCK");
                }

            }catch(SQLException ex){
                JOptionPane.showMessageDialog(null,"Database Error..."+ex.getMessage());
            }
        }
    }
    public static void main(String[]args){
        new menu();
    }
}