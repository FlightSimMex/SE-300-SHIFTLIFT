package se300.shiftlift;

import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
    private final VerticalLayout listLayout = new VerticalLayout();
    private final Button publishButton = new Button("Publish Schedule");
    private Button selectedItem = null;
    private Schedule selectedSchedule = null;

    public ManageSchedulesView(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
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

        // Add button handlers
        publishButton.addClickListener(e -> publishSelectedSchedule());
        returnButton.addClickListener(e -> UI.getCurrent().navigate(MainMenuView.class));

        // Create action buttons layout
        HorizontalLayout actionLayout = new HorizontalLayout(publishButton, returnButton);
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

        // Container to constrain both list and actions to same width
        VerticalLayout container = new VerticalLayout(listLayout, actionLayout);
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

        // Enable publish button only if schedule is unpublished
        if (!isPublished) {
            publishButton.setEnabled(true);
            publishButton.getStyle().set("opacity", "1");
        } else {
            publishButton.setEnabled(false);
            publishButton.getStyle().set("opacity", "0.5");
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
}
