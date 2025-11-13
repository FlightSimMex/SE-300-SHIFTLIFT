package se300.shiftlift;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeLeaveEvent;
import com.vaadin.flow.router.BeforeLeaveObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("New Shift")
@Route("new-shift")
@RolesAllowed("ALL")
public class NewShiftView extends Composite<VerticalLayout> implements BeforeEnterObserver, BeforeLeaveObserver {

    //Add Attribute Components
    private VerticalLayout mainContainer = new VerticalLayout();
    private H1 title = new H1("Create New Shift");
    private Button logoutButton = new Button("Logout");
    private Button addShiftButton = new Button("Add Shift");
    private Button cancelButton = new Button("Cancel");
    private DatePicker shiftDatePicker = new DatePicker("Shift Date");
    private ComboBox<User> workerComboBox = new ComboBox<>("Select Worker");
    private ComboBox<Workstation> workstationComboBox = new ComboBox<>("Select Workstation");
    private ComboBox<String> startTimeComboBox = new ComboBox<>("Start Time");
    private ComboBox<String> endTimeComboBox = new ComboBox<>("End Time");



    //Attribute Objects
    private User currentUser;
    private final UserService userService;
    private final WorkstationService workstationService;
    private final ShiftService shiftService;
    private boolean dirty = false;




    public NewShiftView(UserService userService, WorkstationService workstationService, ShiftService shiftService) {
        this.userService = userService;
        this.workstationService = workstationService;
        this.shiftService = shiftService;
        currentUser = Auth.getCurrentUser();//Get Current user from Vaadin Session

        createElements();

    }

    public void createElements()
    {
        //Setup
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setAlignItems(Alignment.CENTER); // Center all content horizontally

        //Logout Button
        logoutButton.getStyle()
            .set("color", "#666666")
            .set("font-faminly", "Poppins, sans-serif");
        logoutButton.addClickListener(e -> Auth.logoutToLogin());
        HorizontalLayout topBar = new HorizontalLayout(logoutButton);
        topBar.setWidthFull();
        topBar.setAlignItems(Alignment.CENTER);
        topBar.setJustifyContentMode(JustifyContentMode.END);
        topBar.setPadding(false);
        topBar.setSpacing(false);
        topBar.getStyle().set("margin", "0");
        getContent().add(topBar);

        //Title
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")  //font
            .set("font-size", "50px")       //size
            .set("text-align", "center")    //center the text
            .set("margin-top", "30px")
            .set("margin-bottom", "30px");
        getContent().add(title);
        getContent().setHorizontalComponentAlignment(Alignment.CENTER, title); // Center the title component
        
        //Main Container Setup - limit to 1/3 of screen width and center
        mainContainer.setMaxWidth("33.33vw"); // Max 1/3 of screen width
        mainContainer.setMinWidth("300px"); // Minimum width for mobile/small screens
        mainContainer.setAlignItems(Alignment.STRETCH);
        mainContainer.setJustifyContentMode(JustifyContentMode.CENTER);

        //Elements setup
        shiftDatePicker.setWidthFull();
        shiftDatePicker.setLabel("Date:");
        shiftDatePicker.setMin(LocalDate.now());//TODO: change to schedule start date
        shiftDatePicker.setMax(LocalDate.now().plusDays(30));//TODO: change to schedule end date
        shiftDatePicker.getStyle()
            .set("font-family", "Poppins, sans-serif");


        
        //Populate Worker ComboBox based on user role
        workerComboBox.setWidthFull();
        if (Auth.isAdmin()) {
            // Admin can select any user
            workerComboBox.setItems(userService.list(Pageable.unpaged()));
        } else {
            // Non-admin can only select themselves
            workerComboBox.setItems(Collections.singletonList(currentUser));
        }
        workerComboBox.setItemLabelGenerator(User::getUsername); // Display only username
        workerComboBox.setValue(currentUser); // Preselect the current user
        workerComboBox.setLabel("Worker: ");
        workerComboBox.setAllowCustomValue(false); // Disable text editing
        workerComboBox.getStyle()
            .set("font-family", "Poppins, sans-serif");
        
        //Populate Workstation ComboBox
        workstationComboBox.setWidthFull();
        workstationComboBox.setItems(workstationService.list(Pageable.unpaged()));
        workstationComboBox.setItemLabelGenerator(Workstation::getName); // Display only workstation name
        workstationComboBox.setAllowCustomValue(false); // Disable text editing
        workstationComboBox.getStyle()
            .set("font-family", "Poppins, sans-serif");
        workstationComboBox.setLabel("Workstation: ");
        
        // Add listener to update time options when workstation changes
        workstationComboBox.addValueChangeListener(e -> updateTimeOptions());

        //Setup Start time ComboBox
        startTimeComboBox.setWidthFull();
        startTimeComboBox.setLabel("Start Time:");
        startTimeComboBox.setItems(generateTimeOptions());
        startTimeComboBox.setAllowCustomValue(true); // Allow manual time entry
        startTimeComboBox.setPlaceholder("Select or enter start time (HH:MM)");
        startTimeComboBox.getStyle()
            .set("font-family", "Poppins, sans-serif");
        startTimeComboBox.addValueChangeListener(e -> validateTimes());

        //Setup End time ComboBox
        endTimeComboBox.setWidthFull();
        endTimeComboBox.setLabel("End Time:");
        endTimeComboBox.setItems(generateTimeOptions());
        endTimeComboBox.setAllowCustomValue(true); // Allow manual time entry
        endTimeComboBox.setPlaceholder("Select or enter end time (HH:MM)");
        endTimeComboBox.getStyle()
            .set("font-family", "Poppins, sans-serif");
        endTimeComboBox.addValueChangeListener(e -> validateTimes());

        //Add components to main container
        mainContainer.add(shiftDatePicker, workerComboBox, workstationComboBox, startTimeComboBox, endTimeComboBox);

        //Add buttons and routes - make buttons together as wide as combo boxes
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("gap", "12px"); // Add spacing between buttons
        
        // Style buttons to each take half the available width
        addShiftButton.setWidth("calc(50% - 6px)"); // Subtract half the gap
        addShiftButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif");
        addShiftButton.addClickListener(e -> save_button_click_listener());
        
        cancelButton.setWidth("calc(50% - 6px)"); // Subtract half the gap
        cancelButton.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("color", "#666666");
        cancelButton.addClickListener(e -> cancel_button_click_listener());
        
        buttonLayout.add(addShiftButton, cancelButton);
        mainContainer.add(buttonLayout);
        getContent().add(mainContainer);
        getContent().setHorizontalComponentAlignment(Alignment.CENTER, mainContainer); // Center the main container

        
    }

  
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn()) {
            Notification.show("Please Log-in", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("");
            return;
        }
    
    }

    @Override
    public void beforeLeave(BeforeLeaveEvent event) {
        // TODO Auto-generated method stub
        
    }

    private void save_button_click_listener()
    {
        if(validateFields())
        {
            try {
                Date shiftDate = new Date(
                    shiftDatePicker.getValue().getDayOfMonth(), 
                    shiftDatePicker.getValue().getMonthValue(),
                    shiftDatePicker.getValue().getYear());
                Time shiftTime = new Time(
                    parseTimeFromString(startTimeComboBox.getValue()),
                    parseTimeFromString(endTimeComboBox.getValue())
                );
                
                // Use ShiftService to save the shift to database
                shiftService.addShift(
                    shiftDate,
                    workerComboBox.getValue(),
                    workstationComboBox.getValue(),
                    shiftTime
                );
                
                dirty = false;
                Notification.show("Shift created successfully!", 3000, Notification.Position.BOTTOM_START);
                UI.getCurrent().navigate("main-menu");
                
            } catch (Exception e) {
                Notification.show("Error creating shift: " + e.getMessage(), 
                    4000, Notification.Position.MIDDLE);
            }
        }
    }

    private void cancel_button_click_listener()
    {
        dirty = false;
        UI.getCurrent().navigate(MainMenuView.class);
    }


    private boolean validateFields()
    {
        if (shiftDatePicker.getValue() == null) {
            Notification.show("Please select a date", 3000, Notification.Position.MIDDLE);
            return false;
        }
        if (workerComboBox.getValue() == null) {
            Notification.show("Please select a worker", 3000, Notification.Position.MIDDLE);
            return false;
        }
        if (workstationComboBox.getValue() == null) {
            Notification.show("Please select a workstation", 3000, Notification.Position.MIDDLE);
            return false;
        }
        if (startTimeComboBox.getValue() == null) {
            Notification.show("Please select a start time", 3000, Notification.Position.MIDDLE);
            return false;
        }
        if (endTimeComboBox.getValue() == null) {
            Notification.show("Please select an end time", 3000, Notification.Position.MIDDLE);
            return false;
        }
        return validateTimes();
    }
    
    private List<String> generateTimeOptions() {
        List<String> timeOptions = new ArrayList<>();
        // Generate times from 8:00 AM to 5:00 PM in 30-minute intervals
        int startHour = Time.OPENING_TIME / 100; // Extract hour from OPENING_TIME (800 -> 8)
        int endHour = Time.CLOSING_TIME / 100;   // Extract hour from CLOSING_TIME (1700 -> 17)
        
        for (int hour = startHour; hour <= endHour; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
            if (hour < endHour) { // Don't add half-hour past closing time
                timeOptions.add(String.format("%02d:30", hour));
            }
        }
        return timeOptions;
    }
    
    private List<String> generateTimeOptionsForWorkstation(int startTime, int endTime) {
        List<String> timeOptions = new ArrayList<>();
        
        // Generate time options in 30-minute intervals within workstation hours
        int currentTime = startTime;
        while (currentTime <= endTime) {
            int hours = currentTime / 100;
            int minutes = currentTime % 100;
            timeOptions.add(String.format("%02d:%02d", hours, minutes));
            
            // Add 30 minutes
            minutes += 30;
            if (minutes >= 60) {
                hours++;
                minutes = 0;
            }
            currentTime = hours * 100 + minutes;
            
            // Break if we've reached the end time
            if (currentTime > endTime) {
                break;
            }
        }
        
        return timeOptions;
    }
    
    private void updateTimeOptions() {
        Workstation selectedWorkstation = workstationComboBox.getValue();
        if (selectedWorkstation != null && selectedWorkstation.getOperation_hours() != null) {
            // Generate time options based on workstation operating hours
            Time operatingHours = selectedWorkstation.getOperation_hours();
            List<String> workstationTimeOptions = generateTimeOptionsForWorkstation(
                operatingHours.getStart_time(), 
                operatingHours.getEnd_time()
            );
            
            // Update combo box items with workstation-specific times
            startTimeComboBox.setItems(workstationTimeOptions);
            endTimeComboBox.setItems(workstationTimeOptions);
            
            // Set default times based on workstation operating hours
            startTimeComboBox.setValue(formatTimeForDisplay(operatingHours.getStart_time()));
            endTimeComboBox.setValue(formatTimeForDisplay(operatingHours.getEnd_time()));
        } else {
            // Use default time options and values
            List<String> defaultTimeOptions = generateTimeOptions();
            startTimeComboBox.setItems(defaultTimeOptions);
            endTimeComboBox.setItems(defaultTimeOptions);
            startTimeComboBox.setValue(formatTimeForDisplay(Time.OPENING_TIME));
            endTimeComboBox.setValue(formatTimeForDisplay(Time.CLOSING_TIME));
        }
    }
    
    private boolean validateTimes() {
        if (startTimeComboBox.getValue() == null || endTimeComboBox.getValue() == null) {
            return true; // Skip validation if either time is not selected
        }
        
        Workstation selectedWorkstation = workstationComboBox.getValue();
        if (selectedWorkstation == null) {
            return true; // Skip validation if no workstation selected
        }
        
        int startTime = parseTimeFromString(startTimeComboBox.getValue());
        int endTime = parseTimeFromString(endTimeComboBox.getValue());
        
        // Check if start time is before end time
        if (startTime >= endTime) {
            Notification.show("Start time must be before end time", 3000, Notification.Position.MIDDLE);
            return false;
        }
        
        // Check if times are within workstation operating hours
        if (selectedWorkstation.getOperation_hours() != null) {
            Time operatingHours = selectedWorkstation.getOperation_hours();
            int workstationStart = operatingHours.getStart_time();
            int workstationEnd = operatingHours.getEnd_time();
            
            if (startTime < workstationStart || endTime > workstationEnd) {
                Notification.show("Selected times must be within workstation operating hours: " + 
                    formatTimeForDisplay(workstationStart) + " - " + formatTimeForDisplay(workstationEnd), 
                    4000, Notification.Position.MIDDLE);
                return false;
            }
        }
        
        return true;
    }
    
    private String formatTimeForDisplay(int time) {
        int hours = time / 100;
        int minutes = time % 100;
        return String.format("%02d:%02d", hours, minutes);
    }
    
    private int parseTimeFromString(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return Time.OPENING_TIME; // Default to opening time from Time class
        }
        
        try {
            String[] parts = timeStr.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid time format");
            }
            
            int hours = Integer.parseInt(parts[0].trim());
            int minutes = Integer.parseInt(parts[1].trim());
            
            // Validate time ranges
            if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
                throw new IllegalArgumentException("Invalid time values");
            }
            
            return hours * 100 + minutes;
        } catch (IllegalArgumentException e) {
            // Show error notification for invalid manual input
            Notification.show("Invalid time format. Please use HH:MM format (e.g., 09:30)", 
                4000, Notification.Position.MIDDLE);
            return Time.OPENING_TIME; // Return default time
        }
    }
}
