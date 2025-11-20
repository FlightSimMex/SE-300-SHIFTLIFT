package se300.shiftlift;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("Manage Schedule")
@Route("manage-schedule")
@RolesAllowed("ALL")
public class ManageScheduleView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    
    private final ShiftService shiftService;
    private final ScheduleService scheduleService;
    private final VerticalLayout dayButtonsLayout = new VerticalLayout();
    private final VerticalLayout shiftsLayout = new VerticalLayout();
    private final VerticalLayout shiftDetailsLayout = new VerticalLayout();
    
    private final Map<String, Day> dayMap = new HashMap<>();
    private Day selectedDay = null;
    private Button selectedShiftButton = null;
    private Shift selectedShift = null;
    private Schedule currentSchedule = null;

    public ManageScheduleView(ShiftService shiftService, ScheduleService scheduleService) {
        this.shiftService = shiftService;
        this.scheduleService = scheduleService;
        createElements();
        loadShiftsFromDatabase();
    }

    private void createElements() {
        VerticalLayout layout = getContent();
        layout.setSizeFull();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setPadding(true);
        layout.setSpacing(true);

        // Title
        H1 title = new H1("Manage Schedule");
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "48px")
            .set("margin-bottom", "24px");

        // Logout button
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyle().set("color", "#666666");
        logoutBtn.addClickListener(e -> Auth.logoutToLogin());

        HorizontalLayout topBar = new HorizontalLayout(logoutBtn);
        topBar.setWidthFull();
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        // Main content area - horizontal layout with three sections
        HorizontalLayout mainContent = new HorizontalLayout();
        mainContent.setSizeFull();
        mainContent.setSpacing(true);
        
        // Day buttons section
        VerticalLayout daySection = new VerticalLayout();
        daySection.setWidth("200px");
        daySection.setSpacing(true);
        daySection.setPadding(false);
        
        H2 dayTitle = new H2("Select Day");
        dayTitle.getStyle().set("margin", "0");
        daySection.add(dayTitle);
        
        dayButtonsLayout.setSpacing(true);
        dayButtonsLayout.setPadding(false);
        daySection.add(dayButtonsLayout);
        
        // Shifts section
        VerticalLayout shiftsSection = new VerticalLayout();
        shiftsSection.setWidth("300px");
        shiftsSection.setSpacing(true);
        shiftsSection.setPadding(false);
        
        H2 shiftsTitle = new H2("Shifts");
        shiftsTitle.getStyle().set("margin", "0");
        shiftsSection.add(shiftsTitle);
        
        shiftsLayout.setSpacing(true);
        shiftsLayout.setPadding(false);
        shiftsSection.add(shiftsLayout);
        
        // Shift details section
        VerticalLayout detailsSection = new VerticalLayout();
        detailsSection.setWidth("400px");
        detailsSection.setSpacing(true);
        detailsSection.setPadding(false);
        
        H2 detailsTitle = new H2("Shift Details");
        detailsTitle.getStyle().set("margin", "0");
        detailsSection.add(detailsTitle);
        
        shiftDetailsLayout.setSpacing(true);
        shiftDetailsLayout.setPadding(false);
        detailsSection.add(shiftDetailsLayout);
        
        mainContent.add(daySection, shiftsSection, detailsSection);
        
        // Back button
        Button backButton = new Button("Back to Main Menu");
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backButton.addClickListener(e -> UI.getCurrent().navigate("main-menu"));



        layout.add(topBar, title, mainContent, backButton);
        
        // Initialize with empty message
        showEmptyShiftsMessage();
        showEmptyDetailsMessage();
    }

    private void loadShiftsFromDatabase() {
        try {
            // Get the latest unpublished schedule
            var scheduleOpt = scheduleService.getLatestUnpublishedSchedule();
            
            if (scheduleOpt.isEmpty()) {
                Notification.show("No unpublished schedule found. Create a new schedule first.", 
                    3000, Notification.Position.MIDDLE);
                return;
            }
            
            currentSchedule = scheduleOpt.get();
            
            // Load and organize shifts for this schedule
            currentSchedule.loadShifts(shiftService);
            
            List<Shift> scheduleShifts = currentSchedule.getShifts();
            dayMap.clear();
            
            // Group shifts by date
            for (Shift shift : scheduleShifts) {
                String dateKey = formatDateKey(shift.getDate());
                
                Day day = dayMap.get(dateKey);
                if (day == null) {
                    day = new Day(shift.getDate());
                    dayMap.put(dateKey, day);
                }
                day.addShift(shift);
            }
            
            // Create day buttons based on schedule dates
            createDayButtonsFromSchedule();
            
        } catch (Exception e) {
            Notification.show("Error loading shifts: " + e.getMessage(), 
                3000, Notification.Position.MIDDLE);
        }
    }

    private void createDayButtons() {
        dayButtonsLayout.removeAll();
        
        LocalDate today = LocalDate.now();
        
        // Create buttons for the next 5 days
        for (int i = 0; i < 5; i++) {
            LocalDate date = today.plusDays(i);
            Date shiftDate = new Date(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
            String dateKey = formatDateKey(shiftDate);
            
            Button dayButton = new Button(formatDateForDisplay(shiftDate));
            dayButton.setWidthFull();
            
            // Check if there are shifts for this day
            Day day = dayMap.get(dateKey);
            if (day != null && !day.getShifts().isEmpty()) {
                dayButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                dayButton.getStyle().set("background-color", "#156fabff");
            } else {
                dayButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                dayButton.getStyle().set("background-color", "#f5f5f5");
                dayButton.getStyle().set("color", "#666");
            }
            
            dayButton.addClickListener(e -> selectDay(dateKey, dayButton));
            dayButtonsLayout.add(dayButton);
        }
    }

    private void createDayButtonsFromSchedule() {
        dayButtonsLayout.removeAll();
        
        if (currentSchedule == null) {
            createDayButtons();
            return;
        }
        
        Date startDate = currentSchedule.getStartDate();
        Date endDate = currentSchedule.getEndDate();
        
        LocalDate start = LocalDate.of(startDate.get_year(), startDate.get_month(), startDate.get_day());
        LocalDate end = LocalDate.of(endDate.get_year(), endDate.get_month(), endDate.get_day());
        
        // Create buttons for each day in the schedule range (max 30 days for UI purposes)
        LocalDate current = start;
        int dayCount = 0;
        while (!current.isAfter(end) && dayCount < 30) {
            Date shiftDate = new Date(current.getDayOfMonth(), current.getMonthValue(), current.getYear());
            String dateKey = formatDateKey(shiftDate);
            
            Button dayButton = new Button(formatDateForDisplay(shiftDate));
            dayButton.setWidthFull();
            
            // Check if there are shifts for this day
            Day day = dayMap.get(dateKey);
            if (day != null && !day.getShifts().isEmpty()) {
                dayButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                dayButton.getStyle().set("background-color", "#156fabff");
            } else {
                dayButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                dayButton.getStyle().set("background-color", "#f5f5f5");
                dayButton.getStyle().set("color", "#666");
            }
            
            dayButton.addClickListener(e -> selectDay(dateKey, dayButton));
            dayButtonsLayout.add(dayButton);
            
            current = current.plusDays(1);
            dayCount++;
        }
    }

    private void selectDay(String dateKey, Button dayButton) {
        // Reset previous selection
        if (selectedShiftButton != null) {
            selectedShiftButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        }
        selectedShiftButton = null;
        
        // Highlight selected day button
        dayButtonsLayout.getChildren().forEach(child -> {
            if (child instanceof Button button) {
                button.getStyle().remove("border");
            }
        });
        dayButton.getStyle().set("border", "2px solid #156fabff");
        
        selectedDay = dayMap.get(dateKey);
        displayShiftsForDay(selectedDay);
    }

    private void displayShiftsForDay(Day day) {
        shiftsLayout.removeAll();
        
        if (day == null || day.getShifts().isEmpty()) {
            showEmptyShiftsMessage();
            showEmptyDetailsMessage();
            return;
        }
        
        List<Shift> shifts = day.getShifts();
        for (Shift shift : shifts) {
            Button shiftButton = createShiftButton(shift);
            shiftsLayout.add(shiftButton);
        }
    }

    private Button createShiftButton(Shift shift) {
        String timeString = formatTime(shift.getTime().getStart_time()) + 
                           " - " + formatTime(shift.getTime().getEnd_time());
        
        String buttonText = shift.getStudentWorker().getUsername() + "\n" +
                           shift.getWorkstation().getName() + "\n" +
                           timeString;
        
        Button shiftButton = new Button(buttonText);
        shiftButton.setWidthFull();
        shiftButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        shiftButton.getStyle()
            .set("white-space", "pre-line")
            .set("text-align", "left")
            .set("height", "80px");
        
        shiftButton.addClickListener(e -> selectShift(shift, shiftButton));
        
        return shiftButton;
    }

    private void selectShift(Shift shift, Button shiftButton) {
        // Reset previous selection
        if (selectedShiftButton != null) {
            selectedShiftButton.removeThemeVariants(ButtonVariant.LUMO_SUCCESS);
        }
        
        // Highlight selected shift
        shiftButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        selectedShiftButton = shiftButton;
        selectedShift = shift;
        
        displayShiftDetails(shift);
    }

    private void displayShiftDetails(Shift shift) {
        shiftDetailsLayout.removeAll();
        
        if (shift == null) {
            showEmptyDetailsMessage();
            return;
        }
        
        // Worker info
        H3 workerTitle = new H3("Worker");
        Span workerName = new Span("Name: " + shift.getStudentWorker().getUsername());
        Span workerEmail = new Span("Email: " + shift.getStudentWorker().getEmail());
        Span workerInitials = new Span("Initials: " + shift.getStudentWorker().getInitials());
        
        // Workstation info
        H3 workstationTitle = new H3("Workstation");
        Span workstationName = new Span("Name: " + shift.getWorkstation().getName());
        
        // Time info
        H3 timeTitle = new H3("Schedule");
        Span date = new Span("Date: " + formatDateForDisplay(shift.getDate()));
        Span time = new Span("Time: " + formatTime(shift.getTime().getStart_time()) + 
                            " - " + formatTime(shift.getTime().getEnd_time()));
        
        // Shift ID
        H3 shiftTitle = new H3("Shift Info");
        Span shiftId = new Span("ID: " + shift.getId());
        
        // Edit button
        Button editButton = new Button("Edit Shift");
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        editButton.getStyle()
            .set("background-color", "#156fabff")
            .set("margin-top", "20px");
        editButton.addClickListener(e -> navigateToEditShift(shift));
        
        shiftDetailsLayout.add(
            workerTitle, workerName, workerEmail, workerInitials,
            workstationTitle, workstationName,
            timeTitle, date, time,
            shiftTitle, shiftId,
            editButton
        );
    }

    private void showEmptyShiftsMessage() {
        shiftsLayout.removeAll();
        Div emptyMessage = new Div(new Span("Select a day to view shifts"));
        emptyMessage.getStyle()
            .set("text-align", "center")
            .set("color", "#666")
            .set("font-style", "italic")
            .set("padding", "20px");
        shiftsLayout.add(emptyMessage);
    }

    private void showEmptyDetailsMessage() {
        shiftDetailsLayout.removeAll();
        Div emptyMessage = new Div(new Span("Select a shift to view details"));
        emptyMessage.getStyle()
            .set("text-align", "center")
            .set("color", "#666")
            .set("font-style", "italic")
            .set("padding", "20px");
        shiftDetailsLayout.add(emptyMessage);
    }

    private String formatDateKey(Date date) {
        return date.get_year() + "-" + String.format("%02d", date.get_month()) + "-" + String.format("%02d", date.get_day());
    }

    private String formatDateForDisplay(Date date) {
        String[] months = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                          "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[date.get_month()] + " " + date.get_day();
    }

    private String formatTime(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        return String.format("%02d:%02d", hours, minutes);
    }

    private void navigateToEditShift(Shift shift) {
        if (shift == null || shift.getId() == null) {
            Notification.show("Error: Invalid shift", 3000, Notification.Position.MIDDLE);
            return;
        }
        
        HashMap<String, java.util.List<String>> params = new HashMap<>();
        params.put("shiftId", java.util.List.of(shift.getId().toString()));
        QueryParameters qp = new QueryParameters(params);
        UI.getCurrent().navigate("edit-shift", qp);
    }



    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn()) {
            Notification.show("Please Log-in", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("");
        }
    }
}