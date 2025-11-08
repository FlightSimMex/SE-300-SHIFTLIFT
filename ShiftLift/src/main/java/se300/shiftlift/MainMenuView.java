package se300.shiftlift;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route("main-menu")
public class MainMenuView extends VerticalLayout implements BeforeEnterObserver {

    public MainMenuView() {
        H2 title = new H2("ShiftLift Main Menu");
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("margin", "0 0 24px 0");
    Button logoutBtn = new Button("Logout");
    logoutBtn.getStyle().set("color", "#666666");
    logoutBtn.addClickListener(e -> Auth.logoutToLogin());
    // Keep title centered; put Logout in its own right-aligned bar
    HorizontalLayout topBar = new HorizontalLayout(logoutBtn);
    topBar.setWidthFull();
    topBar.setAlignItems(Alignment.CENTER);
    topBar.setJustifyContentMode(JustifyContentMode.END);
    topBar.setPadding(false);
    topBar.setSpacing(false);
    topBar.getStyle().set("margin", "0");

    Button manageWorkersBtn = new Button("Manage Workers");
    Button manageWorkstationsBtn = new Button("Manage Workstations");
    Button manageSchedulesBtn = new Button("Manage Schedules");
    Button changePasswordBtn = new Button("Change Password");

    manageWorkersBtn.getStyle().set("background-color", "#156fabff").set("color", "white");
    manageWorkstationsBtn.getStyle().set("background-color", "#156fabff").set("color", "white");
    manageSchedulesBtn.getStyle().set("background-color", "#156fabff").set("color", "white");
    changePasswordBtn.getStyle().set("background-color", "#156fabff").set("color", "white");

        manageWorkersBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ListUsersView.class)));
        manageWorkstationsBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ListWorkstationsView.class)));
        manageSchedulesBtn.addClickListener(e -> {
            // TODO: Implement schedule management view
        });
        changePasswordBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(ChangePasswordView.class)));

        HorizontalLayout buttonLayout = new HorizontalLayout(
            manageWorkersBtn,
            manageWorkstationsBtn,
            manageSchedulesBtn,
            changePasswordBtn
        );
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setWidthFull();

        // Hide admin-only actions for non-admin users
        boolean admin = Auth.isAdmin();
        manageWorkersBtn.setVisible(admin);
        manageWorkstationsBtn.setVisible(admin);

    add(topBar, title, buttonLayout);

        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setWidthFull();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn()) {
            event.rerouteTo("");
        }
    }
}