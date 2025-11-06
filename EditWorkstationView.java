// This will be the source code for the Workstation class view from vaadin

package se300.shiftlift;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import jakarta.annotation.security.RolesAllowed;


@PageTitle("EditWorkstationView")
@Route("EditWorkstationView")
@RolesAllowed("ADMIN")

public class EditWorkstationView extends Composite<VerticalLayout> {

    HorizontalLayout layoutRow = new HorizontalLayout();
    private    H1 h1 = new H1();
    private    HorizontalLayout layoutRow2 = new HorizontalLayout();
    private    VerticalLayout layoutColumn2 = new VerticalLayout();
    private    H4 h4 = new H4();
    private    TextField textField = new TextField();
    private    NumberField numberField = new NumberField();
    private    HorizontalLayout layoutRow3 = new HorizontalLayout();
    private    Button button_save = new Button();
    private    Button button_delete = new Button();
    private    Button button_cancelchanges = new Button();

    private Workstation workstation;

    public EditWorkstationView() {
        this.workstation = workstation; 
        create_elements();
        if (workstation != null) {
            setWorkstationData(workstation);
        }
    }

    private void setWorkstationData(Workstation workstation) {
        textField.setValue(workstation.getName());
        numberField.setValue((double) workstation.getNumberofEmployees());
    }


    private void create_elements() {

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");

        h1.setText("Workstation");
        h1.setWidth("max-content");

        //Layout for Adding Workstation
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn2.setAlignItems(Alignment.CENTER);

        h4.setText("Add Workstation");
        h4.setWidth("max-content");

        textField.setLabel("Workstation Name:");
        textField.setWidth("min-content");
        numberField.setLabel("# of Employees");
        numberField.setWidth("min-content");

        //Delete Button
        button_delete.setText("Delete");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, button_delete);
        button_delete.setWidth("min-content");
        button_delete.getStyle().set("background-color", "#9b0000ff");
        button_delete.addClickListener (e -> delete_button_click_listener());
        button_delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        layoutRow3.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");
        layoutRow3.setAlignItems(Alignment.START);
        layoutRow3.setJustifyContentMode(JustifyContentMode.CENTER);

        //Save Button
        button_save.setText("Save");
        button_save.setWidth("min-content");
        button_save.getStyle().set("background-color", "#156fabff");
        button_save.addClickListener(e -> save_button_click_listener());
        button_save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //Cancel Changes Button
        button_cancelchanges.setText("Cancel Changes");
        button_cancelchanges.getStyle().set("background-color", "grey");
        button_cancelchanges.setWidth("min-content");
        button_cancelchanges.addClickListener(e -> cancel_button_click_listener());
        button_cancelchanges.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        //Adding Elements to the View
        getContent().add(layoutRow);
        layoutRow.add(h1);
        getContent().add(layoutRow2);
        layoutRow2.add(layoutColumn2);
        layoutColumn2.add(h4);
        layoutColumn2.add(textField);
        layoutColumn2.add(numberField);
        getContent().add(button_delete);
        getContent().add(layoutRow3);
        layoutRow3.add(button_save);
        layoutRow3.add(button_cancelchanges);
    }


    private boolean validateFields() {
        // Implement field validation logic here
        if (textField.isEmpty()) {
            System.out.println("Workstation name cannot be empty.");
            return false;
        }

        if (numberField.isEmpty()) {
            System.out.println("Number of employees cannot be empty.");
            return false;
        }

        return true;
    }


    //Click Listeners for Buttons
    private void save_button_click_listener() {
        //save_button_click_listener logic
        if (!validateFields()) {
            return;
        }

        String name = textField.getValue();
        int employees = numberField.getValue().intValue();

        if (workstation == null) {
            workstation = new Workstation();
        }

        workstation.setName(name);
        workstation.setNumberofEmployees(employees);

        // Logic to save the workstation
        System.out.println("Workstation saved: " + workstation);
    }

    private void delete_button_click_listener() {
        //delete_button_click_listener logic
        if (workstation != null) {
            // Logic to delete the workstation
            System.out.println("Workstation deleted: " + workstation);
            workstation = null;
        } else {
            System.out.println("No workstation to delete");
        }

    }

    private void cancel_button_click_listener() {
        //cancel_button_click_listener logic
        textField.clear();
        numberField.clear();
        System.out.println("Edit cancelled");
    }

    
/*

    private void saveWorkstation() {
        if (textField.isEmpty() || numberField.isEmpty()) {
            System.out.println("Please fill in all fields before saving.");
            return;
        }

        String name = textField.getValue();
        int employees = numberField.getValue().intValue();

        if (workstation == null) {
            workstation = new Workstation(name, employees);
        } else {
            workstation.setName(name);
            workstation.setNumberofEmployees(employees);
        }

        System.out.println("Workstation saved: " + workstation);
    }
    
    private void deleteWorkstation() {
        if workstation != null {
            // Logic to delete the workstation
            System.out.println("Workstation deleted: " + workstation);
            workstation = null;
        } else {
            System.out.println("No workstation to delete");
        }
    }  
    
    
 */
    
    
}