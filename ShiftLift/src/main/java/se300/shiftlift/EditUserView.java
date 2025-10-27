package se300.shiftlift;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import jakarta.annotation.security.RolesAllowed;


@PageTitle("EditUserView")
@Route("EditUserView")
@RolesAllowed("ADMIN")
public class EditUserView extends Composite<VerticalLayout> {


    private HorizontalLayout layoutRow3 = new HorizontalLayout();
    private HorizontalLayout layoutRow5 = new HorizontalLayout();
    private VerticalLayout layoutColumn5 = new VerticalLayout();
    private VerticalLayout layoutColumn7 = new VerticalLayout();
    private VerticalLayout layoutColumn3 = new VerticalLayout();
    private H1 h12 = new H1();
    private HorizontalLayout layoutRow6 = new HorizontalLayout();
    private VerticalLayout layoutColumn8 = new VerticalLayout();
    private TextField emailTextField = new TextField();
    private TextField usernameTextField = new TextField();
    private TextField initialsTextField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private HorizontalLayout layoutRow7 = new HorizontalLayout();
    private Button button_save = new Button();
    private Button button_cancel = new Button();
    private Button button_delete = new Button();
    private VerticalLayout layoutColumn9 = new VerticalLayout();
    private HorizontalLayout layoutRow8 = new HorizontalLayout();
    private VerticalLayout layoutRowButtons = new VerticalLayout();

    private User user;

    
    public EditUserView() {
        
        
        create_elements();
        

    }



    private void setUserData(User user) {
        emailTextField.setValue(user.getEmail());
        usernameTextField.setValue(user.getUsername());
        initialsTextField.setValue(user.getInitials());
        passwordField.setValue(user.getPassword());
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
        h12.setText("Edit User Data");
        h12.setWidth("max-content");
        h12.getStyle().set("font-family", "Poppins, sans-serif");
        layoutRow6.setWidthFull();
        //layoutColumn2.setFlexGrow(1.0, layoutRow6);
        layoutRow6.addClassName(Gap.MEDIUM);
        layoutRow6.setWidth("100%");
        layoutRow6.getStyle().set("flex-grow", "1");
        layoutColumn8.setHeightFull();
        //layoutRow2.setFlexGrow(1.0, layoutColumn8);
        layoutColumn8.setWidth("100%");
        layoutColumn8.getStyle().set("flex-grow", "1");
        emailTextField.setLabel("Email:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, emailTextField);
        emailTextField.setWidth("min-content");
        emailTextField.setErrorMessage("Please enter a valid example@my.erau.edu email address");
        emailTextField.setClearButtonVisible(true);
        emailTextField.setPattern("^.+@my.erau.edu$");
        usernameTextField.setLabel("Username:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, usernameTextField);
        usernameTextField.setWidth("min-content");
        initialsTextField.setLabel("Initials:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, initialsTextField);
        initialsTextField.setWidth("min-content");
        passwordField.setLabel("Password:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, passwordField);
        passwordField.setWidth("min-content");
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
        button_save.getStyle().set("background-color", "#156fabff");
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
        button_delete.setText("Delete User");
        button_delete.setWidth("min-content");
        button_delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button_delete.getStyle().set("background-color", "#9b0000ff");
        button_delete.addClickListener(e -> {
            delete_button_click_listener();
        });
        layoutColumn9.getStyle().set("flex-grow", "1");
        layoutRow8.addClassName(Gap.MEDIUM);
        layoutRow8.setWidth("100%");
        layoutRow8.setHeight("min-content");
        getContent().add(layoutRow3);
        getContent().add(layoutRow5);
        layoutRow5.add(layoutColumn5);
        layoutRow5.add(layoutColumn7);
        layoutColumn7.add(h12);
        layoutColumn7.add(layoutRow6);
        layoutRow6.add(layoutColumn8);
        layoutColumn8.add(emailTextField);
        layoutColumn8.add(usernameTextField);
        layoutColumn8.add(initialsTextField);
        layoutColumn8.add(passwordField);
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
        if(emailTextField.isInvalid() || emailTextField.getValue().isEmpty()) {
            emailTextField.setErrorMessage("Invalid Email");
            emailTextField.setInvalid(true);
            return false;
        }
        if(usernameTextField.getValue().isEmpty()) {
            usernameTextField.setErrorMessage("Username cannot be empty");
            usernameTextField.setInvalid(true);
            return false;
        }
        if(initialsTextField.getValue().isEmpty() || initialsTextField.getValue().length() > 3) {
            initialsTextField.setErrorMessage("Initials cannot be empty or longer than 3 characters");
            initialsTextField.setInvalid(true);
            return false;
        }
        if(passwordField.getValue().isEmpty())
        {
            passwordField.setErrorMessage("Password cannot be empty");
            passwordField.setInvalid(true);
            return false;
        }
        return true;
    }

    private void save_button_click_listener() 
    {
        if(validateFields()) {
            // Save changes to user data
            //Submit user data
            User tempUser = new StudentWorker(
                emailTextField.getValue().toLowerCase(),
                passwordField.getValue()
            );
            // tempUser.setUsername(usernameTextField.getValue());
            // tempUser.setInitials(initialsTextField.getValue().toUpperCase());
        }   
    }

    private void cancel_button_click_listener() 
    {
        // Revert changes to user data
        emailTextField.clear();
        usernameTextField.clear();
        initialsTextField.clear();
        passwordField.clear();
    }

    private void delete_button_click_listener() 
    {
        //Delete User
        
    }
}
