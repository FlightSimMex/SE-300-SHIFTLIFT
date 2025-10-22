package se300.shiftlift;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("Login")

public class LoginView extends VerticalLayout {

    private LoginRepo repo;

    public LoginView(LoginRepo repo) {
        this.repo = repo;
        H1 loginTitle = new H1("ShiftLift");
        loginTitle.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")  //font
            .set("font-size", "100px")       //size
            .set("margin-left", "center")    //position horizontally
            .set("margin-top", "30px");
        add(loginTitle);
        setHorizontalComponentAlignment(Alignment.CENTER, loginTitle);

        H1 userInputTitle = new H1("Username:");
        userInputTitle.getStyle()
            .set("color", "#00070cff")
            .set("font-family", "Poppins, sans-serif")  //font
            .set("font-size", "25px");       //size  

        H1 userPasswordTitle = new H1("Password:");
        userPasswordTitle.getStyle()
            .set("color", "#00070cff")
            .set("font-family", "Poppins, sans-serif")  //font
            .set("font-size", "25px");       //size

        var inputUser = new TextField();
        var inputPassword = new PasswordField();
        var changePassword = new Button("Change Password");
        var loginButton = new Button("Login");
        loginButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white");
        
        VerticalLayout layout = new VerticalLayout();

        HorizontalLayout userRow = new HorizontalLayout(userInputTitle, inputUser);
        HorizontalLayout passwordRow = new HorizontalLayout(userPasswordTitle, inputPassword);
        HorizontalLayout buttonRow = new HorizontalLayout(changePassword, loginButton);

        userRow.setJustifyContentMode(JustifyContentMode.CENTER);
        passwordRow.setJustifyContentMode(JustifyContentMode.CENTER);
        buttonRow.setJustifyContentMode(JustifyContentMode.CENTER);

        userRow.setAlignItems(FlexComponent.Alignment.BASELINE);
        passwordRow.setAlignItems(FlexComponent.Alignment.BASELINE);

        layout.add(userRow, passwordRow, buttonRow);

        add(layout);

        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        layout.setWidthFull();
    }
}