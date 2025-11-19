package se300.shiftlift;

import java.time.LocalDate;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("New Schedule")
@Route("new-schedule")
@RolesAllowed("ADMIN")
public class NewScheduleView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    // UI Components
    private VerticalLayout mainContainer = new VerticalLayout();
    private H1 title = new H1("Create New Schedule");
    private Button logoutButton = new Button("Logout");
    private Button createScheduleButton = new Button("Create Schedule");
    private Button cancelButton = new Button("Cancel");
    private DatePicker startDatePicker = new DatePicker("Start Date");
    private DatePicker endDatePicker = new DatePicker("End Date");

    // Services
    private final ScheduleService scheduleService;
    private boolean dirty = false;

    public NewScheduleView(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
        createElements();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn() || !Auth.isAdmin()) {
            Notification.show("Access denied: Admins only", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("");
            return;
        }
    }

    private void createElements() {
        // Setup main layout
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setAlignItems(Alignment.CENTER);

        // Logout Button - Top Bar
        logoutButton.getStyle()
            .set("color", "#666666")
            .set("font-family", "Poppins, sans-serif");
        logoutButton.addClickListener(e -> Auth.logoutToLogin());
        HorizontalLayout topBar = new HorizontalLayout(logoutButton);
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setJustifyContentMode(JustifyContentMode.END);
        topBar.setPadding(false);
        topBar.setSpacing(false);
        topBar.getStyle().set("margin", "0");
        getContent().add(topBar);

        // Title
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "50px")
            .set("text-align", "center")
            .set("margin-top", "30px")
            .set("margin-bottom", "30px");
        getContent().add(title);
        getContent().setHorizontalComponentAlignment(Alignment.CENTER, title);

        // Main Container Setup
        mainContainer.setMaxWidth("33.33vw");
        mainContainer.setMinWidth("300px");
        mainContainer.setAlignItems(Alignment.STRETCH);
        mainContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        // Start Date Picker
        startDatePicker.setWidthFull();
        startDatePicker.setLabel("Start Date:");
        startDatePicker.setMin(LocalDate.now());
        startDatePicker.getStyle()
            .set("font-family", "Poppins, sans-serif");
        startDatePicker.addValueChangeListener(e -> {
            dirty = true;
            validateDates();
        });

        // End Date Picker
        endDatePicker.setWidthFull();
        endDatePicker.setLabel("End Date:");
        endDatePicker.setMin(LocalDate.now());
        endDatePicker.getStyle()
            .set("font-family", "Poppins, sans-serif");
        endDatePicker.addValueChangeListener(e -> {
            dirty = true;
            validateDates();
        });

        // Add date pickers to main container
        mainContainer.add(startDatePicker, endDatePicker);

        // Buttons Layout
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("gap", "12px");

        // Create Schedule Button
        createScheduleButton.setWidth("calc(50% - 6px)");
        createScheduleButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif");
        createScheduleButton.addClickListener(e -> saveButtonClickListener());

        // Cancel Button
        cancelButton.setWidth("calc(50% - 6px)");
        cancelButton.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("color", "#666666");
        cancelButton.addClickListener(e -> cancelButtonClickListener());

        buttonLayout.add(createScheduleButton, cancelButton);
        mainContainer.add(buttonLayout);
        
        getContent().add(mainContainer);
        getContent().setHorizontalComponentAlignment(Alignment.CENTER, mainContainer);
    }

    private void saveButtonClickListener() {
        if (validateFields()) {
            try {
                // Convert LocalDate to custom Date objects
                LocalDate startLocal = startDatePicker.getValue();
                LocalDate endLocal = endDatePicker.getValue();

                Date startDate = new Date(
                    startLocal.getDayOfMonth(),
                    startLocal.getMonthValue(),
                    startLocal.getYear()
                );

                Date endDate = new Date(
                    endLocal.getDayOfMonth(),
                    endLocal.getMonthValue(),
                    endLocal.getYear()
                );

                // Create and save the schedule
                Schedule schedule = scheduleService.createSchedule(startDate, endDate);
                
                dirty = false;
                Notification.show("Schedule created successfully!", 3000, Notification.Position.BOTTOM_START);
                UI.getCurrent().navigate("main-menu");

            } catch (Exception e) {
                Notification.show("Error creating schedule: " + e.getMessage(),
                    4000, Notification.Position.MIDDLE);
            }
        }
    }

    private void cancelButtonClickListener() {
        dirty = false;
        UI.getCurrent().navigate(MainMenuView.class);
    }

    private boolean validateFields() {
        if (startDatePicker.getValue() == null) {
            Notification.show("Please select a start date", 3000, Notification.Position.MIDDLE);
            return false;
        }
        if (endDatePicker.getValue() == null) {
            Notification.show("Please select an end date", 3000, Notification.Position.MIDDLE);
            return false;
        }
        return validateDates();
    }

    private boolean validateDates() {
        if (startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            return true; // Skip validation if either date is not selected
        }

        LocalDate startLocal = startDatePicker.getValue();
        LocalDate endLocal = endDatePicker.getValue();

        // Check if start date is before end date
        if (!startLocal.isBefore(endLocal)) {
            Notification.show("Start date must be before end date", 3000, Notification.Position.MIDDLE);
            return false;
        }

        // Update end date picker minimum to be after start date
        endDatePicker.setMin(startLocal.plusDays(1));

        return true;
    }
}
