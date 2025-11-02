package se300.shiftlift;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public final class Auth {
    private Auth() {}

    public static void setCurrentUser(User user) {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(User.class, user);
        }
    }

    public static User getCurrentUser() {
        VaadinSession session = VaadinSession.getCurrent();
        return session != null ? session.getAttribute(User.class) : null;
    }

    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public static boolean isAdmin() {
        return getCurrentUser() instanceof ManagerUser;
    }

    public static void logoutToLogin() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute(User.class, null);
            session.close();
        }
        UI ui = UI.getCurrent();
        if (ui != null) ui.navigate("");
    }
}