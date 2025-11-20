package se300.shiftlift;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Manage Schedules")
@Route("manage-schedules")
@RolesAllowed("ADMIN")
public class ManageSchedulesView extends VerticalLayout implements BeforeEnterObserver {

    private final ScheduleService scheduleService;
    private final ShiftService shiftService;
    private final VerticalLayout listLayout = new VerticalLayout();
    private final Button publishButton = new Button("Publish Schedule");
    private final Button discardButton = new Button("Discard Schedule");
    private Button selectedItem = null;
    private Schedule selectedSchedule = null;

    public ManageSchedulesView(ScheduleService scheduleService, ShiftService shiftService) {
        this.scheduleService = scheduleService;
        this.shiftService = shiftService;
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        // Title
        H1 title = new H1("Manage Schedules");
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "48px")
            .set("margin-bottom", "24px");

        // Logout button in top bar
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyle()
            .set("color", "#666666")
            .set("font-family", "Poppins, sans-serif");
        logoutBtn.addClickListener(e -> Auth.logoutToLogin());

        HorizontalLayout topBar = new HorizontalLayout(logoutBtn);
        topBar.setWidthFull();
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Create action buttons
        Button returnButton = new Button("Return");

        // Style the publish button
        publishButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif");
        publishButton.setEnabled(false);
        publishButton.getStyle().set("opacity", "0.5"); // Initial greyed out state

        returnButton.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("color", "#666666");

        // Style the discard button
        discardButton.getStyle()
            .set("background-color", "#dc3545")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif");
        discardButton.setEnabled(false);
        discardButton.getStyle().set("opacity", "0.5"); // Initial greyed out state

        // Add button handlers
        publishButton.addClickListener(e -> publishSelectedSchedule());
        discardButton.addClickListener(e -> confirmDiscardSchedule());
        returnButton.addClickListener(e -> UI.getCurrent().navigate(MainMenuView.class));

        // Add New Schedule button
        Button addScheduleButton = new Button("+");
        addScheduleButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "20px")
            .set("font-weight", "bold")
            .set("border", "none")
            .set("border-radius", "50%")
            .set("min-width", "40px")
            .set("width", "40px")
            .set("height", "40px")
            .set("padding", "0")
            .set("display", "flex")
            .set("align-items", "center")
            .set("justify-content", "center")
            .set("margin", "16px auto");
        addScheduleButton.addClickListener(e -> UI.getCurrent().navigate("new-schedule"));

        // Add button layout - centered
        HorizontalLayout addButtonLayout = new HorizontalLayout(addScheduleButton);
        addButtonLayout.setWidthFull();
        addButtonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addButtonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Create action buttons layout
        HorizontalLayout actionLayout = new HorizontalLayout(publishButton, discardButton, returnButton);
        actionLayout.setWidthFull();
        actionLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        actionLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        actionLayout.setSpacing(true);

        // List layout - constrain to same width as action layout
        listLayout.setWidthFull();
        listLayout.setSpacing(true);
        listLayout.setPadding(false);
        listLayout.getStyle()
            .set("gap", "16px")
            .set("margin-top", "16px");

        // Container to constrain list, add button, and actions to same width
        VerticalLayout container = new VerticalLayout(listLayout, addButtonLayout, actionLayout);
        container.setWidthFull();
        container.setMaxWidth("max-content");
        container.setPadding(false);
        container.setSpacing(true);
        container.setAlignItems(FlexComponent.Alignment.STRETCH);

        // Add components
        add(topBar, title, container);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn()) {
            Notification.show("Please Log-in", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("");
            return;
        }

        if (!Auth.isAdmin()) {
            Notification.show("Admin access required", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("main-menu");
            return;
        }

        loadSchedules();
    }

    private void loadSchedules() {
        listLayout.removeAll();
        selectedItem = null;
        selectedSchedule = null;
        publishButton.setEnabled(false);
        publishButton.getStyle().set("opacity", "0.5");

        // Clean up expired schedules before loading
        cleanupExpiredSchedules();

        List<Schedule> schedules = scheduleService.getAllSchedules();

        if (schedules.isEmpty()) {
            Span emptyMessage = new Span("No schedules found");
            emptyMessage.getStyle()
                .set("font-family", "Poppins, sans-serif")
                .set("color", "#666666")
                .set("font-size", "16px");
            listLayout.add(emptyMessage);
            return;
        }

        for (Schedule schedule : schedules) {
            Button scheduleButton = createScheduleButton(schedule);
            listLayout.add(scheduleButton);
        }
    }

    private Button createScheduleButton(Schedule schedule) {
        Button button = new Button();
        button.setWidth("100%");

        // Create content layout for text
        VerticalLayout textLayout = new VerticalLayout();
        textLayout.setPadding(false);
        textLayout.setSpacing(false);
        textLayout.getStyle().set("gap", "8px");

        // Schedule date range
        Span dateRange = new Span(formatScheduleDateRange(schedule));
        dateRange.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "18px")
            .set("font-weight", "600");

        // Schedule status
        String statusText = schedule.getApproved() != null && schedule.getApproved() 
            ? "Published" : "Unpublished";
        Span status = new Span("Status: " + statusText);
        status.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "14px");

        textLayout.add(dateRange, status);

        // Wrap in a div to properly display in button and center vertically
        Div wrapper = new Div(textLayout);
        wrapper.getStyle()
            .set("width", "100%")
            .set("padding", "20px 16px");
        button.getElement().appendChild(wrapper.getElement());

        // Set background color based on publish status
        boolean isPublished = schedule.getApproved() != null && schedule.getApproved();
        String backgroundColor = isPublished ? "#28a745" : "#156fabff"; // Green for published, blue for unpublished
        
        button.getStyle()
            .set("background-color", backgroundColor)
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif")
            .set("border", "none")
            .set("border-radius", "8px")
            .set("padding", "0")
            .set("cursor", "pointer")
            .set("transition", "all 0.2s")
            .set("min-height", "80px")
            .set("display", "flex")
            .set("align-items", "center");

        // Store schedule reference
        button.getElement().setProperty("_scheduleId", schedule.getId().toString());

        // Add click handler
        button.addClickListener(e -> selectSchedule(button, schedule));

        return button;
    }

    private void selectSchedule(Button button, Schedule schedule) {
        // Check if clicking the same item - deselect it
        if (selectedItem == button) {
            boolean isPublished = schedule.getApproved() != null && schedule.getApproved();
            String backgroundColor = isPublished ? "#28a745" : "#156fabff";
            button.getStyle()
                .set("background-color", backgroundColor)
                .set("border", "none");
            
            selectedItem = null;
            selectedSchedule = null;
            publishButton.setEnabled(false);
            publishButton.getStyle().set("opacity", "0.5");
            discardButton.setEnabled(false);
            discardButton.getStyle().set("opacity", "0.5");
            return;
        }
        
        // Deselect previous item
        if (selectedItem != null) {
            Schedule prevSchedule = scheduleService.getScheduleById(
                Long.parseLong(selectedItem.getElement().getProperty("_scheduleId"))
            ).orElse(null);
            
            if (prevSchedule != null) {
                boolean isPrevPublished = prevSchedule.getApproved() != null && prevSchedule.getApproved();
                String prevBackgroundColor = isPrevPublished ? "#28a745" : "#156fabff";
                selectedItem.getStyle()
                    .set("background-color", prevBackgroundColor)
                    .set("border", "none");
            }
        }

        // Select new item
        selectedItem = button;
        selectedSchedule = schedule;

        // Highlight selected item
        boolean isPublished = schedule.getApproved() != null && schedule.getApproved();
        String backgroundColor = isPublished ? "#28a745" : "#156fabff";
        button.getStyle()
            .set("background-color", backgroundColor)
            .set("border", "3px solid #ffc107"); // Yellow border for selection

        // Enable/disable buttons based on publish status
        if (!isPublished) {
            // Unpublished schedule - enable publish button, disable discard button
            publishButton.setEnabled(true);
            publishButton.getStyle().set("opacity", "1");
            discardButton.setEnabled(false);
            discardButton.getStyle().set("opacity", "0.5");
        } else {
            // Published schedule - disable publish button, enable discard button
            publishButton.setEnabled(false);
            publishButton.getStyle().set("opacity", "0.5");
            discardButton.setEnabled(true);
            discardButton.getStyle().set("opacity", "1");
        }
    }

    private void publishSelectedSchedule() {
        if (selectedSchedule == null) {
            Notification.show("Please select a schedule to publish", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (selectedSchedule.getApproved() != null && selectedSchedule.getApproved()) {
            Notification.show("This schedule is already published", 3000, Notification.Position.MIDDLE);
            return;
        }

        try {
            selectedSchedule.setApproved(true);
            scheduleService.save(selectedSchedule);
            Notification.show("Schedule published successfully!", 3000, Notification.Position.BOTTOM_START);
            loadSchedules(); // Reload to update colors
        } catch (Exception e) {
            Notification.show("Error publishing schedule: " + e.getMessage(), 
                4000, Notification.Position.MIDDLE);
        }
    }

    private void confirmDiscardSchedule() {
        if (selectedSchedule == null) {
            Notification.show("Please select a schedule to discard", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (selectedSchedule.getApproved() == null || !selectedSchedule.getApproved()) {
            Notification.show("Only published schedules can be discarded", 3000, Notification.Position.MIDDLE);
            return;
        }

        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Discard Published Schedule");
        dialog.setText("Are you sure you want to discard this published schedule? " +
                      "This will permanently delete all shifts within the schedule dates " +
                      "and remove the schedule from the database. This action cannot be undone.");
        dialog.setCancelable(true);
        dialog.setConfirmText("Discard");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> discardSelectedSchedule());
        dialog.open();
    }

    private void discardSelectedSchedule() {
        if (selectedSchedule == null) {
            return;
        }

        try {
            // First, remove all shifts within the schedule date range
            Date startDate = selectedSchedule.getStartDate();
            Date endDate = selectedSchedule.getEndDate();
            
            if (startDate != null && endDate != null) {
                List<Shift> allShifts = shiftService.getAllShifts();
                int startDateInt = startDate.get_Date();
                int endDateInt = endDate.get_Date();
                
                // Find and delete shifts within the schedule date range
                List<Shift> shiftsToDelete = allShifts.stream()
                    .filter(shift -> {
                        int shiftDateInt = shift.getDate().get_Date();
                        return shiftDateInt >= startDateInt && shiftDateInt <= endDateInt;
                    })
                    .toList();
                
                // Delete each shift
                for (Shift shift : shiftsToDelete) {
                    shiftService.deleteShift(shift);
                }
                
                Notification.show("Deleted " + shiftsToDelete.size() + " shifts", 
                    2000, Notification.Position.BOTTOM_START);
            }
            
            // Then delete the schedule itself
            scheduleService.delete(selectedSchedule);
            
            Notification.show("Schedule discarded successfully!", 
                3000, Notification.Position.BOTTOM_START);
            
            // Reload the schedules list
            loadSchedules();
            
        } catch (Exception e) {
            Notification.show("Error discarding schedule: " + e.getMessage(), 
                4000, Notification.Position.MIDDLE);
        }
    }

    private String formatScheduleDateRange(Schedule schedule) {
        Date startDate = schedule.getStartDate();
        Date endDate = schedule.getEndDate();
        
        if (startDate == null || endDate == null) {
            return "Invalid Date Range";
        }

        return String.format("%02d/%02d/%04d - %02d/%02d/%04d",
            startDate.get_month(), startDate.get_day(), startDate.get_year(),
            endDate.get_month(), endDate.get_day(), endDate.get_year());
    }

    private void cleanupExpiredSchedules() {
        try {
            List<Schedule> allSchedules = scheduleService.getAllSchedules();
            LocalDate today = LocalDate.now();
            List<Schedule> expiredSchedules = new ArrayList<>();
            
            // Find expired schedules
            for (Schedule schedule : allSchedules) {
                Date endDate = schedule.getEndDate();
                if (endDate != null) {
                    // Convert Date to LocalDate for comparison
                    LocalDate scheduleEndDate = LocalDate.of(
                        endDate.get_year(), 
                        endDate.get_month(), 
                        endDate.get_day()
                    );
                    
                    // If the schedule end date has passed, mark it for deletion
                    if (scheduleEndDate.isBefore(today)) {
                        expiredSchedules.add(schedule);
                    }
                }
            }
            
            // Remove expired schedules and their shifts
            int totalShiftsDeleted = 0;
            for (Schedule expiredSchedule : expiredSchedules) {
                // First, remove all shifts within the schedule date range
                Date startDate = expiredSchedule.getStartDate();
                Date endDate = expiredSchedule.getEndDate();
                
                if (startDate != null && endDate != null) {
                    List<Shift> allShifts = shiftService.getAllShifts();
                    int startDateInt = startDate.get_Date();
                    int endDateInt = endDate.get_Date();
                    
                    // Find and delete shifts within the schedule date range
                    List<Shift> shiftsToDelete = allShifts.stream()
                        .filter(shift -> {
                            int shiftDateInt = shift.getDate().get_Date();
                            return shiftDateInt >= startDateInt && shiftDateInt <= endDateInt;
                        })
                        .toList();
                    
                    // Delete each shift
                    for (Shift shift : shiftsToDelete) {
                        shiftService.deleteShift(shift);
                    }
                    
                    totalShiftsDeleted += shiftsToDelete.size();
                }
                
                // Then delete the schedule itself
                scheduleService.delete(expiredSchedule);
            }
            
            // Show notification if any cleanup was performed
            if (!expiredSchedules.isEmpty()) {
                String message = String.format("Automatically removed %d expired schedule(s) and %d associated shift(s)", 
                    expiredSchedules.size(), totalShiftsDeleted);
                Notification.show(message, 4000, Notification.Position.BOTTOM_START);
            }
            
        } catch (Exception e) {
            // Silent failure - don't interrupt the user experience
            System.err.println("Error during schedule cleanup: " + e.getMessage());
        }
    }
}
