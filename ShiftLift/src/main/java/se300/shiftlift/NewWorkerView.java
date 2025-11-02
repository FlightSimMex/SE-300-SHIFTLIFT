package se300.shiftlift;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import jakarta.annotation.security.RolesAllowed;


@PageTitle("New Worker")
@Route("NewWorker")
@RolesAllowed("ADMIN")
public class NewWorkerView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private HorizontalLayout layoutRow3 = new HorizontalLayout();
    private HorizontalLayout layoutRow = new HorizontalLayout();
    private VerticalLayout layoutColumn4 = new VerticalLayout();
    private VerticalLayout layoutColumn2 = new VerticalLayout();
    private H1 h1 = new H1();
    private Paragraph textMedium = new Paragraph();
    private Hr hr = new Hr();
    private VerticalLayout layoutColumn3 = new VerticalLayout();
    private EmailField emailField = new EmailField();
    private PasswordField passwordField = new PasswordField();
    private HorizontalLayout layoutRow2 = new HorizontalLayout();
    private Button button_create = new Button();
    private Button button_cancel = new Button();
    private Paragraph textSmall = new Paragraph();
    private VerticalLayout layoutColumn5 = new VerticalLayout();
    private PasswordField newWorkerPassword = new PasswordField("Confirm Worker Password:");
    private RadioButtonGroup<String> roleSelector = new RadioButtonGroup<>();
    @Autowired
    private UserService userService;


    public NewWorkerView() {
        
      
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.setHeight("min-content");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutColumn4.getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h1.setText("Adding New Worker");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h1);
        h1.setWidth("max-content");
        h1.getStyle().set("font-family", "Poppins, sans-serif");
    // Shiftlift blue title color
    h1.getStyle().set("color", "#156fabff");
        textMedium.setText(
                "Please enter new worker information. Please enter a valid ERAU email, the new users username will be their email before the '@'. The assigned password can be changed later after user logs-in.");
        textMedium.setWidth("100%");
        textMedium.getStyle().set("font-size", "var(--lumo-font-size-m)");
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn3.setAlignItems(Alignment.CENTER);
        emailField.setLabel("New Worker Email:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, emailField);
        emailField.setWidth("min-content");
        emailField.getElement().setAttribute("name", "email");
        emailField.setPlaceholder("username@my.erau.edu");
        emailField.setErrorMessage("Please enter a valid example@my.erau.edu email address");
        emailField.setClearButtonVisible(true);
        emailField.setPattern("^.+@my.erau.edu$");
        passwordField.setLabel("New Worker Password:");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, passwordField);
        passwordField.setWidth("min-content");
        layoutRow2.setWidthFull();
        layoutColumn3.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.getStyle().set("flex-grow", "1");
        layoutRow2.setAlignItems(Alignment.START);
        layoutRow2.setJustifyContentMode(JustifyContentMode.CENTER);
        button_create.setText("Create Account");
        button_create.setWidth("min-content");
        button_create.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button_create.getStyle().set("background-color", "#156fabff");
        button_cancel.setText("Cancel");
        button_cancel.setWidth("min-content");
        button_cancel.getStyle().set("color", "grey");
        textSmall.setWidth("100%");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        layoutColumn5.getStyle().set("flex-grow", "1");
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
    getContent().add(layoutRow);
        layoutRow.add(layoutColumn4);
        layoutRow.add(layoutColumn2);
        // keep title centered and with consistent bottom margin
        h1.getStyle().set("margin", "0 0 24px 0");
        layoutColumn2.add(h1);
        layoutColumn2.add(textMedium);
        layoutColumn2.add(hr);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(emailField);
        layoutColumn3.add(passwordField);
        newWorkerPassword.setWidth("min-content");
        layoutColumn3.add(newWorkerPassword);
        roleSelector.setLabel("Role:");
        roleSelector.setItems("Student", "Manager");
        roleSelector.setValue("Student");
        layoutColumn3.add(roleSelector);
        layoutColumn3.add(layoutRow2);
        layoutRow2.add(button_create);
        layoutRow2.add(button_cancel);
        layoutColumn3.add(textSmall);
        
        
        layoutRow.add(layoutColumn5);

        button_create.addClickListener(e -> {
            create_button_click_listener();
        });

        button_cancel.addClickListener(e -> {
            cancel_button_click_listener();
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn() || !Auth.isAdmin()) {
            Notification.show("Access denied: Admins only", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("");
        }
    }

    private void create_button_click_listener() {
        
        try {
            if(emailField.isInvalid() || emailField.getValue().isEmpty()) {
                emailField.setErrorMessage("Invalid Email");
                emailField.setInvalid(true);
                
            }else{
                if(newWorkerPassword.getValue().equals(passwordField.getValue()))
                {
                    //Add new student worker to database
                    try {
                        if ("Manager".equals(roleSelector.getValue())) {
                            userService.createManagerUser(emailField.getValue().toLowerCase(), passwordField.getValue());
                        } else {
                            userService.createStudentWorker(emailField.getValue().toLowerCase(), passwordField.getValue());
                        }
                    } catch (Exception e) {

                        emailField.setErrorMessage("Email already exists");
                        emailField.setInvalid(true);
                        return;
                        
                    }
                    

                    emailField.clear();
                    passwordField.clear();
                    newWorkerPassword.clear();
                    //textSmall.setText("New worker account created successfully!");
                    UI.getCurrent().navigate("list-users");
                }
                else{
                    passwordField.setErrorMessage("Passwords do not match!");
                    passwordField.setInvalid(true);
                    newWorkerPassword.setInvalid(true);
                    newWorkerPassword.setErrorMessage("Passwords do not match!");
                }
                
            }
            
        } catch (IllegalArgumentException e) {
            emailField.setErrorMessage("Invalid Data");
            passwordField.clear();
            newWorkerPassword.clear();
            
        }
    }

    private void cancel_button_click_listener() {
        emailField.clear();
        passwordField.clear();
        newWorkerPassword.clear();
        textSmall.setText("");
        UI.getCurrent().navigate("list-users");
    }
}
