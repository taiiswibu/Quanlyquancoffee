/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
//import com.sun.jdi.connect.spi.Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class AdminDao {

    Connection con; //con: Đây là một đối tượng của lớp Connection, được sử dụng để kết nối với cơ sở dữ liệu. Trong trường hợp này, nó được khởi tạo bằng MyConnection.getConnection().
    PreparedStatement ps; //ps: Đây là một đối tượng của lớp PreparedStatement, được sử dụng để thực hiện các truy vấn SQL đã được chuẩn bị trước. Chúng ta có thể sử dụng ps để thêm tham số vào truy vấn và thực hiện các thao tác với cơ sở dữ liệu.
    Statement st;//st: Đây là một đối tượng của lớp Statement, được sử dụng để thực hiện các truy vấn SQL không có tham số. Trong ví dụ này, st được sử dụng để thực hiện truy vấn “SELECT MAX(id) FROM admin”.
    ResultSet rs;//rs: Đây là một đối tượng của lớp ResultSet, được sử dụng để lưu trữ kết quả của truy vấn. Trong ví dụ này, rs chứa kết quả của truy vấn “SELECT MAX(id) FROM admin”.

    public AdminDao() throws SQLException {
        con = MyConnection.getConnection();
    }

    /*Trả về số hàng tối đa trong bảng admin tăng thêm 1.
     * @return Số hàng tối đa + 1
     */
    public int getMaxRowAdminTable() {
        int row = 0;
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT MAX(id) FROM admin");//Kết quả của truy vấn được lưu trong đối tượng rs (là một đối tượng của lớp ResultSet). Trong vòng lặp while, chúng ta lấy giá trị tối đa của cột id và gán cho biến row.
            while (rs.next()) {//rs.next(): Phương thức này di chuyển con trỏ đến hàng tiếp theo trong
                row = rs.getInt(1);//Trong vòng lặp, chúng ta sử dụng rs.getInt(1) để lấy giá trị của cột đầu tiên (cột id). Giá trị này được gán cho biến row.
            }

            //SQLException ex: Đây là đối tượng của lớp SQLException, chứa thông tin về lỗi xảy ra.
        } catch (Exception ex) {//Trong đoạn mã trên, chúng ta sử dụng khối catch để xử lý ngoại lệ (exception) trong trường hợp có lỗi xảy ra khi thực hiện truy vấn SQL. Cụ thể:
            Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);// Dòng này ghi log lỗi (error log) bằng cách sử dụng Logger. Nó sẽ hiển thị thông báo lỗi và stack trace (danh sách các phương thức đã gọi) trong console hoặc file log.
        }
        return row + 1;
    }

    public boolean isAdimNameExist(String username) {
        try {
            ps = con.prepareStatement("SELECT * FROM admin WHERE username = ?");
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (Exception ex) {
            Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean insert(Admin admin) {
        String sql = "insert into admin (id, username, password,  s_ques, ans) values (?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, admin.getId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getsQues());
            ps.setString(5, admin.getAns());

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            //Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean login(String username, String password) {
        try {
            ps = con.prepareStatement("SELECT * FROM admin WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(AdminDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
