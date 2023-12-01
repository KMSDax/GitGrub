package com.example.gitgrub;

import java.util.HashMap;
import java.util.Map;

public class UserRoleManager {
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUser_id(), user);
    }

    public void promoteToAdministrator(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.promoteToAdministrator();
        } else {
            System.out.println("User with ID " + userId + " not found.");
        }
    }

    public void banUser(String adminId, String userId) {
        User admin = users.get(adminId);
        User userToBan = users.get(userId);

        if (admin != null && userToBan != null) {
            admin.banUser(userToBan);
        } else {
            System.out.println("Admin or user not found.");
        }
    }

    public void displayUsers() {
        System.out.println("Users:");
        for (User user : users.values()) {
            System.out.println(user.getUser_id());
        }
    }
}
