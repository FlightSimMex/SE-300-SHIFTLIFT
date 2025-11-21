package se300.shiftlift;

import java.util.List;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import jakarta.annotation.security.RolesAllowed;


@PageTitle("EditWorkstationView")
@Route("edit-workstation")
@RolesAllowed("ADMIN")
public class EditWorkstationView extends Composite<VerticalLayout> implements BeforeEnterObserver, com.vaadin.flow.router.BeforeLeaveObserver {

    private final HorizontalLayout layoutRow3 = new HorizontalLayout();
    private final HorizontalLayout layoutRow5 = new HorizontalLayout();
    private final VerticalLayout layoutColumn5 = new VerticalLayout();
    private final VerticalLayout layoutColumn7 = new VerticalLayout();
    private final VerticalLayout layoutColumn3 = new VerticalLayout();
    private final H1 h12 = new H1();
    private final HorizontalLayout layoutRow6 = new HorizontalLayout();
    private final VerticalLayout layoutColumn8 = new VerticalLayout();
    private final TextField nameTextField = new TextField();
    private final ComboBox<String> openingTimeComboBox = new ComboBox<>();
    private final ComboBox<String> closingTimeComboBox = new ComboBox<>();
    private final HorizontalLayout layoutRow7 = new HorizontalLayout();
    private final Button button_save = new Button();
    private final Button button_cancel = new Button();
    private final Button button_delete = new Button();
    private final VerticalLayout layoutColumn9 = new VerticalLayout();
    private final HorizontalLayout layoutRow8 = new HorizontalLayout();
    private final VerticalLayout layoutRowButtons = new VerticalLayout();

    private Workstation workstation;
    private final WorkstationService workstationService;
    private boolean dirty = false;
    
    public EditWorkstationView(WorkstationService workstationService) {
        this.workstationService = workstationService;
        create_elements();
        // Track changes to detect unsaved edits
        nameTextField.addValueChangeListener(e -> dirty = true);
        openingTimeComboBox.addValueChangeListener(e -> dirty = true);
        closingTimeComboBox.addValueChangeListener(e -> dirty = true);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn() || !Auth.isAdmin()) {
            Notification.show("Access denied: Admins only", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("");
            return;
        }
        // TODO: Add workstation loading by name/id from query parameters
        java.util.List<String> params = event.getLocation().getQueryParameters().getParameters().get("name");
        if (params != null && !params.isEmpty()) {
            String name = params.get(0);
            if (name != null && !name.isEmpty()) {
                loadWorkstationByName(name);
            }
        }
    }

    @Override
    public void beforeLeave(com.vaadin.flow.router.BeforeLeaveEvent event) {
        if (!dirty) return;
        final com.vaadin.flow.router.BeforeLeaveEvent.ContinueNavigationAction action = event.postpone();
        Dialog confirm = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialogLayout.setAlignItems(Alignment.CENTER);
        
        Span message = new Span("You have unsaved changes. Leave without saving?");
        message.getStyle().set("margin", "16px 0");
        dialogLayout.add(message);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        
        com.vaadin.flow.component.button.Button leave = new com.vaadin.flow.component.button.Button("Leave", ev -> {
            confirm.close();
            action.proceed();
        });
        leave.getStyle()
            .set("margin-right", "16px")
            .set("color", "#666666");
        
        com.vaadin.flow.component.button.Button stay = new com.vaadin.flow.component.button.Button("Stay", ev -> {
            confirm.close();
        });
        stay.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        stay.getStyle().set("background-color", "#156fabff");
        
        buttonLayout.add(leave, stay);
        dialogLayout.add(buttonLayout);
        confirm.add(dialogLayout);
        confirm.open();
    }

    public void loadWorkstationByName(String name) {
        List<Workstation> workstations = workstationService.findByName(name);
        if (!workstations.isEmpty()) {
            this.workstation = workstations.get(0);
            setWorkstationData(this.workstation);
            dirty = false;
        }
    }

    private void setWorkstationData(Workstation workstation) {
        nameTextField.setValue(workstation.getName());
        if (workstation.getOperation_hours() != null) {
            openingTimeComboBox.setValue(formatTimeForDisplay(workstation.getOperation_hours().getStart_time()));
            closingTimeComboBox.setValue(formatTimeForDisplay(workstation.getOperation_hours().getEnd_time()));
        }
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
        String[] parts = timeStr.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 100 + minutes;
    }
    
    private java.util.List<String> generateTimeOptions() {
        java.util.List<String> timeOptions = new java.util.ArrayList<>();
        // Generate times based on Time class constants (OPENING_TIME to CLOSING_TIME) in 30-minute intervals
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

    private void create_elements() {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.setHeight("min-content");
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.getStyle().set("flex-grow", "1");
        layoutColumn5.getStyle().set("flex-grow", "1");
        layoutColumn7.setWidth("100%");
        layoutColumn7.getStyle().set("flex-grow", "1");
        layoutColumn7.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn7.setAlignItems(Alignment.CENTER);
        h12.setText("Edit Workstation Data");
        h12.setWidth("max-content");
        h12.getStyle().set("font-family", "Poppins, sans-serif");
        // Shiftlift blue title color
        h12.getStyle().set("color", "#156fabff");
        layoutRow6.setWidthFull();
        layoutRow6.addClassName(Gap.MEDIUM);
        layoutRow6.setWidth("100%");
        layoutRow6.getStyle().set("flex-grow", "1");
        layoutColumn8.setHeightFull();
        layoutColumn8.setWidth("100%");
        layoutColumn8.getStyle().set("flex-grow", "1");
        nameTextField.setLabel("Workstation Name:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, nameTextField);
        nameTextField.setWidth("min-content");
        nameTextField.setErrorMessage("Please enter a valid workstation name");
        nameTextField.setClearButtonVisible(true);
        
        // Setup opening time dropdown
        openingTimeComboBox.setLabel("Opening Time:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, openingTimeComboBox);
        openingTimeComboBox.setWidth("min-content");
        openingTimeComboBox.setAllowCustomValue(true);
        openingTimeComboBox.setItems(generateTimeOptions());
        openingTimeComboBox.setPlaceholder("Select opening time");
        openingTimeComboBox.setValue(formatTimeForDisplay(Time.OPENING_TIME)); // Default to opening time from Time class
        
        // Setup closing time dropdown
        closingTimeComboBox.setLabel("Closing Time:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, closingTimeComboBox);
        closingTimeComboBox.setWidth("min-content");
        closingTimeComboBox.setAllowCustomValue(true);
        closingTimeComboBox.setItems(generateTimeOptions());
        closingTimeComboBox.setPlaceholder("Select closing time");
        closingTimeComboBox.setValue(formatTimeForDisplay(Time.CLOSING_TIME)); // Default to closing time from Time class
        layoutRow7.setWidthFull();
        layoutRowButtons.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow7);
        layoutRow7.addClassName(Gap.MEDIUM);
        layoutRow7.setWidth("100%");
        layoutRow7.getStyle().set("flex-grow", "1");
        layoutRow7.setAlignItems(Alignment.CENTER);
        layoutRow7.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutRowButtons.addClassName(Gap.MEDIUM);
        layoutRowButtons.setWidth("100%");
        layoutRowButtons.getStyle().set("flex-grow", "1");
        layoutRowButtons.setAlignItems(Alignment.CENTER);
        layoutRowButtons.setJustifyContentMode(JustifyContentMode.CENTER);
        button_save.setText("Save Changes");
        button_save.setWidth("min-content");
        button_save.getStyle().set("background-color", "#156fabff").set("transition", "all 0.2s");
        button_save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button_save.addClickListener(e -> {
            save_button_click_listener();
        });
        button_cancel.setText("Cancel Changes");
        button_cancel.getStyle().set("color", "grey");
        button_cancel.setWidth("min-content");
        button_cancel.addClickListener(e -> {
            cancel_button_click_listener();
        });
        button_delete.setText("Delete Workstation");
        button_delete.setWidth("min-content");
        button_delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button_delete.getStyle().set("background-color", "#9b0000ff").set("transition", "all 0.2s");
        button_delete.addClickListener(e -> {
            delete_button_click_listener();
        });
        layoutColumn9.getStyle().set("flex-grow", "1");
        layoutRow8.addClassName(Gap.MEDIUM);
        layoutRow8.setWidth("100%");
        layoutRow8.setHeight("min-content");
        // Add a right-aligned top bar for Logout at the very top (to match MainMenu)
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
        getContent().add(topBar);

        getContent().add(layoutRow3);
        getContent().add(layoutRow5);
        layoutRow5.add(layoutColumn5);
        layoutRow5.add(layoutColumn7);
        // keep title centered and with consistent bottom margin
        h12.getStyle().set("margin", "0 0 24px 0");
        layoutColumn7.add(h12);
        layoutColumn7.add(layoutRow6);
        layoutRow6.add(layoutColumn8);
        layoutColumn8.add(nameTextField);
        layoutColumn8.add(openingTimeComboBox);
        layoutColumn8.add(closingTimeComboBox);
        layoutColumn8.add(layoutRowButtons);
        
        layoutRow7.add(button_save);
        layoutRow7.add(button_cancel);
        layoutRowButtons.add(button_delete);
        layoutRowButtons.add(layoutRow7);
        layoutRow5.add(layoutColumn9);
        getContent().add(layoutRow8);
    }


    private boolean validateFields() {
        // Implement field validation logic here
        if(nameTextField.isEmpty() || nameTextField.getValue().trim().isEmpty()) {
            nameTextField.setErrorMessage("Workstation name cannot be empty");
            nameTextField.setInvalid(true);
            return false;
        }
        
        if(openingTimeComboBox.isEmpty() || openingTimeComboBox.getValue() == null) {
            openingTimeComboBox.setErrorMessage("Opening time must be selected");
            openingTimeComboBox.setInvalid(true);
            return false;
        }
        
        if(closingTimeComboBox.isEmpty() || closingTimeComboBox.getValue() == null) {
            closingTimeComboBox.setErrorMessage("Closing time must be selected");
            closingTimeComboBox.setInvalid(true);
            return false;
        }
        
        // Validate time range
        try {
            int openingTime = parseTimeFromString(openingTimeComboBox.getValue());
            int closingTime = parseTimeFromString(closingTimeComboBox.getValue());
            
            if (openingTime >= closingTime) {
                closingTimeComboBox.setErrorMessage("Closing time must be after opening time");
                closingTimeComboBox.setInvalid(true);
                return false;
            }
            
            if (openingTime < Time.OPENING_TIME || closingTime > Time.CLOSING_TIME) {
                openingTimeComboBox.setErrorMessage("Operating hours must be between " + 
                    formatTimeForDisplay(Time.OPENING_TIME) + " and " + formatTimeForDisplay(Time.CLOSING_TIME));
                openingTimeComboBox.setInvalid(true);
                return false;
            }
        } catch (Exception e) {
            openingTimeComboBox.setErrorMessage("Invalid time format");
            openingTimeComboBox.setInvalid(true);
            return false;
        }
        
        return true;
    }

    private void save_button_click_listener() 
    {
        if(validateFields()) {
            if (workstation != null) {
                try {
                    workstation.setName(nameTextField.getValue().trim());
                    
                    // Set operation hours
                    int openingTime = parseTimeFromString(openingTimeComboBox.getValue());
                    int closingTime = parseTimeFromString(closingTimeComboBox.getValue());
                    
                    // Create new Time object with the specified hours
                    workstation.setOperation_hours(new Time(openingTime, closingTime));
                    
                    workstationService.save(workstation);
                    dirty = false;
                    Notification.show("Workstation saved", 2000, Notification.Position.BOTTOM_START);
                    // Navigate back to workstation list after successful save
                    UI.getCurrent().navigate("list-workstations");
                } catch (Exception e) {
                    Notification.show("Error saving workstation: " + e.getMessage(), 
                        3000, Notification.Position.MIDDLE);
                }
            } else {
                // Creating new workstation
                try {
                    workstation = new Workstation(nameTextField.getValue().trim());
                    
                    // Set operation hours for new workstation
                    int openingTime = parseTimeFromString(openingTimeComboBox.getValue());
                    int closingTime = parseTimeFromString(closingTimeComboBox.getValue());
                    
                    // Create new Time object with the specified hours
                    workstation.setOperation_hours(new Time(openingTime, closingTime));
                    
                    workstationService.save(workstation);
                    dirty = false;
                    Notification.show("Workstation created", 2000, Notification.Position.BOTTOM_START);
                    UI.getCurrent().navigate("list-workstations");
                } catch (Exception e) {
                    Notification.show("Error creating workstation: " + e.getMessage(), 
                        3000, Notification.Position.MIDDLE);
                }
            }
        }   
    }

    private void cancel_button_click_listener() 
    {
        // Clear dirty flag and navigate back
        dirty = false;
        UI.getCurrent().navigate("list-workstations");
    }

    private void delete_button_click_listener() 
    {
        if (workstation == null) return;
        Dialog confirm = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialogLayout.setAlignItems(Alignment.CENTER);
        
        Span message = new Span("Are you sure you want to delete workstation '" + workstation.getName() + "'?");
        message.getStyle()
            .set("margin", "16px 0")
            .set("font-family", "Poppins, sans-serif");
        dialogLayout.add(message);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        
        com.vaadin.flow.component.button.Button no = new com.vaadin.flow.component.button.Button("Cancel", ev -> confirm.close());
        no.getStyle()
            .set("margin-right", "16px")
            .set("color", "#666666");
        
        com.vaadin.flow.component.button.Button yes = new com.vaadin.flow.component.button.Button("Delete", ev -> {
            int deletedShifts = workstationService.delete(workstation);
            confirm.close();
            dirty = false;
            String deleteMessage = "Workstation deleted";
            if (deletedShifts > 0) {
                deleteMessage += " (" + deletedShifts + " associated shift(s) also removed)";
            }
            Notification.show(deleteMessage, 4000, Notification.Position.BOTTOM_START);
            UI.getCurrent().navigate("list-workstations");
        });
        yes.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        yes.getStyle()
            .set("background-color", "#9b0000ff")
            .set("color", "white");
        
        buttonLayout.add(no, yes);
        dialogLayout.add(buttonLayout);
        confirm.add(dialogLayout);
        confirm.open();
    }

}