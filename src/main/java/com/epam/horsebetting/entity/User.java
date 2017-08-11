package com.epam.horsebetting.entity;

import com.epam.horsebetting.type.RoleType;
import com.epam.horsebetting.util.DateFormatter;
import com.epam.horsebetting.util.HashManager;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class User extends Entity {

    private int id;
    private int roleId;

    private BigDecimal balance;

    private String name;
    private String email;
    private String password;
    private Timestamp createdAt;

    /**
     * Default constructor role type by default.
     */
    public User() {
        this.setBalance(new BigDecimal(0));
        this.setRole(RoleType.CLIENT);
    }

    /**
     * Constructor with credentials.
     *
     * @param email
     * @param password
     */
    public User(String email, String password) {
        this();
        this.setEmail(email);
        this.setPassword(password);
    }

    /**
     * Get id of the User.
     *
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Get Role id of a User.
     *
     * @return role id
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * Get role type.
     *
     * @return role type
     */
    public String getRoleName() {
        return RoleType.findById(roleId).getRoleName();
    }

    /**
     * Get name of the User.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get email of a User.
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get User password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get user balance.
     *
     * @return balance
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Get user created time.
     *
     * @return user created time
     */
    public Timestamp getRegistrationDate() {
        return createdAt;
    }

    /**
     * Set id of the Role.
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Set Role of user.
     *
     * @param role
     */
    public void setRole(RoleType role) {
        this.roleId = role.getRoleId();
    }

    /**
     * Set name of the Role.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set user email.
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set password.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = HashManager.make(password);
    }

    /**
     * Set user date registration.
     *
     * @param createdAt
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Set user balance.
     *
     * @param balance
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Check if the user is Administrator.
     *
     * @return boolean
     */
    public boolean isAdministrator() {
        return getRoleId() == RoleType.ADMINISTRATOR.getRoleId();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", createdAt='" + createdAt + '\'' + "}";
    }
}
