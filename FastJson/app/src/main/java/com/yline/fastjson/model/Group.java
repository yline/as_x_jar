package com.yline.fastjson.model;


import java.io.Serializable;
import java.util.List;

/**
 * @author yline 2017/10/24 -- 15:02
 * @version 1.0.0
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 5203133474097884832L;
    private long id;
    private String name;
    private List<User> users;

    public static class User implements Serializable {
        private static final long serialVersionUID = 6942947276410579400L;
        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
