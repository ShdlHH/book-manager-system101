package com.todo.todomanagementsystem.util;

public class ThreadLocalContext {
    public static ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    public static ThreadLocal<String> currentUserName = new ThreadLocal<>();

    public static void setCurrentUserId(Long userId) {
        currentUserId.set(userId);
    }

    public static Long getCurrentUserId() {
        return currentUserId.get();
    }

    public static void setCurrentUserName(String userName) {
        currentUserName.set(userName);
    }

    public static String getCurrentUserName() {
        return currentUserName.get();
    }

    public static void remove() {
        currentUserId.remove();
        currentUserName.remove();
    }
}