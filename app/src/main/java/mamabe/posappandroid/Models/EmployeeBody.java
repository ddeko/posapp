package mamabe.posappandroid.Models;

import java.io.Serializable;

/**
 * Created by DedeEko on 5/15/2017.
 */

public class EmployeeBody implements Serializable {
    private String emp_id;
    private String username;
    private String password;
    private String emp_name;
    private String address;
    private String phone;
    private String role_name;
    private String emp_status;
    private String change_username;

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getChange_username() {
        return change_username;
    }

    public void setChange_username(String change_username) {
        this.change_username = change_username;
    }

    public String getEmp_status() {

        return emp_status;
    }

    public void setEmp_status(String emp_status) {
        this.emp_status = emp_status;
    }
}
