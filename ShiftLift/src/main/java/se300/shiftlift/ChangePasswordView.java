package se300.shiftlift;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("change-password")
public class ChangePasswordView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;

    public ChangePasswordView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        // Top bar with Logout aligned right
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyle().set("color", "#666666");
        logoutBtn.addClickListener(e -> Auth.logoutToLogin());
        HorizontalLayout topBar = new HorizontalLayout(logoutBtn);
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setJustifyContentMode(JustifyContentMode.END);
        topBar.setPadding(false);
        topBar.setSpacing(false);
        topBar.getStyle().set("margin", "0");

        H1 title = new H1("Change Password");
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("margin", "0 0 24px 0");

        PasswordField currentPassword = new PasswordField("Current Password");
        currentPassword.setWidth("300px");
        PasswordField newPassword = new PasswordField("New Password");
        newPassword.setWidth("300px");
        PasswordField confirmPassword = new PasswordField("Confirm New Password");
        confirmPassword.setWidth("300px");

        Button saveBtn = new Button("Save");
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.getStyle().set("background-color", "#156fabff").set("color", "white");
        saveBtn.setWidth("300px");

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyle().set("color", "#666666");
        cancelBtn.setWidth("300px");

        VerticalLayout form = new VerticalLayout(currentPassword, newPassword, confirmPassword, saveBtn, cancelBtn);
        form.setPadding(false);
        form.setSpacing(true);
        form.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(topBar, title, form);

        saveBtn.addClickListener(e -> {
            if (!Auth.isLoggedIn()) {
                Notification.show("Please log in first").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            String curr = currentPassword.getValue();
            String next = newPassword.getValue();
            String confirm = confirmPassword.getValue();

            if (curr == null || curr.isEmpty() || next == null || next.isEmpty() || confirm == null || confirm.isEmpty()) {
                Notification.show("All fields are required").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            if (!next.equals(confirm)) {
                Notification.show("New passwords do not match").addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            User current = Auth.getCurrentUser();
            try {
                userService.changePassword(current, curr, next);
                Notification.show("Password updated successfully").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                getUI().ifPresent(ui -> ui.navigate(MainMenuView.class));
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        cancelBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(MainMenuView.class)));
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn()) {
            event.rerouteTo("");
        }
    }
}
