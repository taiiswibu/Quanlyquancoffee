/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Dao {

    Connection con; //con: Đây là một đối tượng của lớp Connection, được sử dụng để kết nối với cơ sở dữ liệu. Trong trường hợp này, nó được khởi tạo bằng MyConnection.getConnection().
    PreparedStatement ps; //ps: Đây là một đối tượng của lớp PreparedStatement, được sử dụng để thực hiện các truy vấn SQL đã được chuẩn bị trước. Chúng ta có thể sử dụng ps để thêm tham số vào truy vấn và thực hiện các thao tác với cơ sở dữ liệu.
    Statement st;//st: Đây là một đối tượng của lớp Statement, được sử dụng để thực hiện các truy vấn SQL không có tham số. Trong ví dụ này, st được sử dụng để thực hiện truy vấn “SELECT MAX(id) FROM admin”.
    ResultSet rs;//rs: Đây là một đối tượng của lớp ResultSet, được sử dụng để lưu trữ kết quả của truy vấn. Trong ví dụ này, rs chứa kết quả của truy vấn “SELECT MAX(id) FROM admin”.

    public boolean insertProduct(Product p) {//Đây là phương thức được sử dụng để chèn một sản phẩm mới vào cơ sở dữ liệu. Phương thức này nhận một đối tượng Product làm đối số, đại diện cho thông tin về sản phẩm cần chèn.
        String sql = "INSERT INTO product (name, price, image) VALUES (?, ?, ?)";
        try {
            Connection con = MyConnection.getConnection();//tạo một đối tượng kết nối Connection đến cơ sở dữ liệu bằng cách gọi phương thức getConnection() của lớp MyConnection. Đây là một phương thức static trong lớp MyConnection, nó trả về một đối tượng kết nối được thiết lập trước đó.
            PreparedStatement ps = con.prepareStatement(sql);//tạo một đối tượng PreparedStatement từ đối tượng kết nối đã được thiết lập trước đó (con). Đối tượng PreparedStatement này được sử dụng để thực hiện một câu lệnh SQL đã được chuẩn bị trước, trong trường hợp này là câu lệnh sql.
            //Việc sử dụng PreparedStatement thường được ưa chuộng hơn so với Statement vì nó cho phép sử dụng tham số định vị (placeholders), giúp ngăn chặn các cuộc tấn công SQL Injection và tăng hiệu suất thực thi câu lệnh SQL.

            ps.setString(1, p.getName());
            ps.setDouble(2, p.getPrice());
            ps.setBytes(3, p.getImage());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void getallProducts(JTable table) {
        String sql = "SELECT * FROM product ORDER BY id DESC";
        try {
            con = MyConnection.getConnection(); // Khởi tạo đối tượng Connection
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            Object[] row;

            while (rs.next()) {
                row = new Object[4];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getDouble(3);
                row[3] = rs.getBytes(4);

                model.addRow(row); // Thêm dòng vào mô hình của bảng

            }

            model.fireTableDataChanged(); // Cập nhật mô hình của bảng

        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean update(Product product) {
        String sql = "update product set name = ?,price = ? where id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setInt(3, product.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean delete(Product product) {
        String sql = "delete from product where id = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, product.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public int getMaxRowOrderTable() {
        int row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(cid) FROM cart");//Kết quả của truy vấn được lưu trong đối tượng rs (là một đối tượng của lớp ResultSet). Trong vòng lặp while, chúng ta lấy giá trị tối đa của cột id và gán cho biến row.
            while (rs.next()) {//rs.next(): Phương thức này di chuyển con trỏ đến hàng tiếp theo trong
                row = rs.getInt(1);//Trong vòng lặp, chúng ta sử dụng rs.getInt(1) để lấy giá trị của cột đầu tiên (cột id). Giá trị này được gán cho biến row.
            }

            //SQLException ex: Đây là đối tượng của lớp SQLException, chứa thông tin về lỗi xảy ra.
        } catch (Exception ex) {//Trong đoạn mã trên, chúng ta sử dụng khối catch để xử lý ngoại lệ (exception) trong trường hợp có lỗi xảy ra khi thực hiện truy vấn SQL. Cụ thể:
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);// Dòng này ghi log lỗi (error log) bằng cách sử dụng Logger. Nó sẽ hiển thị thông báo lỗi và stack trace (danh sách các phương thức đã gọi) trong console hoặc file log.
        }
        return row + 1;
    }

    public boolean isProductExist(int cid, int pid) {
        try {
            ps = con.prepareStatement("select * from cart where cid = ? and pid = ?");
            ps.setInt(1, cid);
            ps.setInt(2, pid);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean insertCart(Cart cart) {
        String sql = "insert into cart (cid, pid, pName, qty, price,total) values(?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cart.getId());
            ps.setInt(2, cart.getPid());
            ps.setString(3, cart.getpName());
            ps.setInt(4, cart.getQty());
            ps.setDouble(5, cart.getPrice());
            ps.setDouble(6, cart.getTotal());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public int getMaxRowPaymantTable() {
        int row = 0;
        try {
            con = MyConnection.getConnection(); // Initialize the connection object
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(pid) FROM payment");//Kết quả của truy vấn được lưu trong đối tượng rs (là một đối tượng của lớp ResultSet). Trong vòng lặp while, chúng ta lấy giá trị tối đa của cột id và gán cho biến row.
            while (rs.next()) {//rs.next(): Phương thức này di chuyển con trỏ đến hàng tiếp theo trong
                row = rs.getInt(1);//Trong vòng lặp, chúng ta sử dụng rs.getInt(1) để lấy giá trị của cột đầu tiên (cột id). Giá trị này được gán cho biến row.
            }

            //SQLException ex: Đây là đối tượng của lớp SQLException, chứa thông tin về lỗi xảy ra.
        } catch (Exception ex) {//Trong đoạn mã trên, chúng ta sử dụng khối catch để xử lý ngoại lệ (exception) trong trường hợp có lỗi xảy ra khi thực hiện truy vấn SQL. Cụ thể:
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);// Dòng này ghi log lỗi (error log) bằng cách sử dụng Logger. Nó sẽ hiển thị thông báo lỗi và stack trace (danh sách các phương thức đã gọi) trong console hoặc file log.
        }
        return row + 1;
    }

    public int getMaxRowCartTable() {
        int row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(cid) FROM cart");//Kết quả của truy vấn được lưu trong đối tượng rs (là một đối tượng của lớp ResultSet). Trong vòng lặp while, chúng ta lấy giá trị tối đa của cột id và gán cho biến row.
            while (rs.next()) {//rs.next(): Phương thức này di chuyển con trỏ đến hàng tiếp theo trong
                row = rs.getInt(1);//Trong vòng lặp, chúng ta sử dụng rs.getInt(1) để lấy giá trị của cột đầu tiên (cột id). Giá trị này được gán cho biến row.
            }

            //SQLException ex: Đây là đối tượng của lớp SQLException, chứa thông tin về lỗi xảy ra.
        } catch (Exception ex) {//Trong đoạn mã trên, chúng ta sử dụng khối catch để xử lý ngoại lệ (exception) trong trường hợp có lỗi xảy ra khi thực hiện truy vấn SQL. Cụ thể:
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);// Dòng này ghi log lỗi (error log) bằng cách sử dụng Logger. Nó sẽ hiển thị thông báo lỗi và stack trace (danh sách các phương thức đã gọi) trong console hoặc file log.
        }
        return row;
    }

    public double subTotal() {
        double subTotal = 0.0;
        int cid = getMaxRowCartTable();

        try {
            st = con.createStatement();
            rs = st.executeQuery("select sum(total) as 'total' from cart where cid = '" + cid + "'");
            if (rs.next()) {
                subTotal = rs.getDouble(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return subTotal;
    }

    public void getProductsFromCart(JTable table) {
        int cid = getMaxRowCartTable();
        String sql = "SELECT * FROM cart where cid = ?";
        try {
            con = MyConnection.getConnection(); // Khởi tạo đối tượng Connection
            ps = con.prepareStatement(sql);
            ps.setInt(1, cid);
            rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            Object[] row;

            while (rs.next()) {
                row = new Object[6];
                row[0] = rs.getInt(1);
                row[1] = rs.getInt(2);
                row[2] = rs.getString(3);
                row[3] = rs.getInt(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getDouble(6);

                model.addRow(row); // Thêm dòng vào mô hình của bảng

            }

            model.fireTableDataChanged(); // Cập nhật mô hình của bảng

        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertPayment(Payment payment) {
        String sql = "insert into payment (pid, cName, proid, pName, total, pdate) values(?,?,?,?,?,?)";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, payment.getPid());
            ps.setString(2, payment.getcName());
            ps.setString(3, payment.getProId());
            ps.setString(4, payment.getProName());
            ps.setDouble(5, payment.getTotal());
            ps.setString(6, payment.getDate());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteCart(int cid) {
        String sql = "delete from cart where cid = ?";

        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, cid);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            return false;
        }
    }

    public void getPaymentDetails(JTable table) {

        String sql = "SELECT * FROM payment order by pid desc";
        try {
            con = MyConnection.getConnection(); // Khởi tạo đối tượng Connection
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            Object[] row;

            while (rs.next()) {
                row = new Object[6];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getDouble(5);
                row[5] = rs.getString(6);

                model.addRow(row); // Thêm dòng vào mô hình của bảng

            }

            model.fireTableDataChanged(); // Cập nhật mô hình của bảng

        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int totalProducts() {
        int total = 0;
        try {
            con = MyConnection.getConnection(); // Khởi tạo kết nối
            st = con.createStatement();
            rs = st.executeQuery("select count(*) from product");

            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
    


    public double todayRevenue(String date) {
        double total = 0.0;
        try {
            con = MyConnection.getConnection(); // Khởi tạo kết nối
            st = con.createStatement();
            rs = st.executeQuery("select sum(total) as total from payment where pdate = '" + date + "'");
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }
    
    
      public double totalRevenue() {
        double total = 0.0;
        try {
            con = MyConnection.getConnection(); // Khởi tạo kết nối
            st = con.createStatement();
            rs = st.executeQuery("select sum(total) as total from payment ");
            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (Exception ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

}
