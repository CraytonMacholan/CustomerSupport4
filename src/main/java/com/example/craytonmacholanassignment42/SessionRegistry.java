package com.example.craytonmacholanassignment42;


import jakarta.servlet.http.HttpSession;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SessionRegistry {
    private static final Map<String, HttpSession> sessions = Collections.synchronizedMap(new HashMap<>());

    public static void addSession(HttpSession session) {
        sessions.put(session.getId(), session);
    }

    public static void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public static Map<String, HttpSession> getSessions() {
        return sessions;
    }
}
