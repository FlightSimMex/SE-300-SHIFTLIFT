package se300.shiftlift;

import java.util.Collections;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;

@PageTitle("List Users")
@Route("list-users")
@RolesAllowed("ADMIN")
public class ListUsersView extends VerticalLayout {

    private final UserService userService;
    private final VerticalLayout listLayout = new VerticalLayout();
    private final com.vaadin.flow.component.textfield.TextField searchField = new com.vaadin.flow.component.textfield.TextField();
    private final Button prevButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button editButton = new Button("Edit User");
    private int currentPage = 0;
    private String currentQuery = "";
    private Button selectedItem = null;

    public ListUsersView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Users");
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "48px")
            .set("margin-bottom", "24px");

        searchField.setPlaceholder("Search by username...");
        searchField.setClearButtonVisible(true);
        searchField.setWidth("400px");
        searchField.getStyle()
            .set("font-family", "Poppins, sans-serif");
        searchField.addValueChangeListener(e -> {
            currentQuery = e.getValue() == null ? "" : e.getValue();
            currentPage = 0;
            loadUsers(currentQuery, currentPage);
        });

        // Create navigation buttons
        Button newUserButton = new Button("Create New User");
        Button returnButton = new Button("Return");

        // Style the buttons
        prevButton.getStyle()
            .set("font-family", "Poppins, sans-serif");
        
        nextButton.getStyle()
            .set("font-family", "Poppins, sans-serif");
        
        editButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif");
        editButton.setEnabled(false);
        editButton.getStyle().set("opacity", "0.5"); // Initial greyed out state
        
        newUserButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif");
            
        returnButton.getStyle()
            .set("font-family", "Poppins, sans-serif");

        // Add navigation handlers
        newUserButton.addClickListener(e -> UI.getCurrent().navigate("NewWorker"));
        returnButton.addClickListener(e -> UI.getCurrent().navigate(""));
        
        editButton.addClickListener(e -> {
            if (selectedItem != null) {
                String username = selectedItem.getElement().getProperty("_user");
                java.util.Map<String, java.util.List<String>> params = Collections.singletonMap("username", 
                    java.util.Collections.singletonList(username));
                QueryParameters qp = new QueryParameters(params);
                UI.getCurrent().navigate("EditUserView", qp);
            }
        });

        // Create search layout at the top
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        searchLayout.setWidthFull();

        prevButton.addClickListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                loadUsers(currentQuery, currentPage);
            }
        });
        nextButton.addClickListener(e -> {
            currentPage++;
            loadUsers(currentQuery, currentPage);
        });

        listLayout.setWidth("600px");
        listLayout.setSpacing(true);
        listLayout.setPadding(false);
        listLayout.setAlignItems(FlexComponent.Alignment.START);
        listLayout.getStyle()
            .set("gap", "24px")  // Increased gap between rows
            .set("margin-top", "16px");
            
        // Create pagination layout
        HorizontalLayout paginationLayout = new HorizontalLayout(prevButton, nextButton);
        paginationLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        paginationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        paginationLayout.setSpacing(true);

        // Create action buttons layout
        HorizontalLayout actionLayout = new HorizontalLayout(newUserButton, editButton, returnButton);
        actionLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        actionLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        actionLayout.setSpacing(true);
        
        // Update return button style to grey
        returnButton.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("color", "#666666");  // Grey text color

        // Create navigation layout at the bottom to hold both button groups
        VerticalLayout bottomLayout = new VerticalLayout(paginationLayout, actionLayout);
        bottomLayout.setAlignItems(Alignment.CENTER);
        bottomLayout.setSpacing(true);
        bottomLayout.setPadding(true);
        bottomLayout.getStyle().set("margin-top", "24px");
        
        // Create background div for deselection
        Div background = new Div();
        background.setSizeFull();
        background.getStyle()
            .set("position", "fixed")
            .set("top", "0")
            .set("left", "0")
            .set("z-index", "-1");

        background.addClickListener(e -> {
            if (selectedItem != null) {
                selectedItem.getStyle().set("background", "none");
                selectedItem = null;
                editButton.setEnabled(false);
                editButton.getStyle().set("opacity", "0.5");
            }
        });

        // Create main content container
        VerticalLayout contentLayout = new VerticalLayout(title, searchLayout, listLayout, bottomLayout);
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);
        contentLayout.setPadding(true);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Add both background and content
        add(background, contentLayout);

        loadUsers(currentQuery, currentPage);
    }

    private void loadUsers(String query, int page) {
        listLayout.removeAll();
        org.springframework.data.domain.Slice<User> slice;
        if (query == null || query.isEmpty()) {
            slice = userService.searchByUsername("", org.springframework.data.domain.PageRequest.of(page, 20));
        } else {
            slice = userService.searchByUsername(query, org.springframework.data.domain.PageRequest.of(page, 20));
        }

        List<User> users = slice.toList();

        for (User u : users) {
            Avatar avatar = new Avatar(u.getUsername());
            avatar.setAbbreviation(u.getInitials());
            avatar.setHeight("50px");
            avatar.setWidth("50px");

            VerticalLayout userInfo = new VerticalLayout();
            userInfo.setSpacing(false);
            userInfo.setPadding(false);
            userInfo.setHeight("50px"); // Match avatar height
            userInfo.setAlignItems(Alignment.START);
            userInfo.setJustifyContentMode(com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.CENTER);

            Span username = new Span(u.getUsername());
            username.getStyle()
                .set("font-weight", "600")
                .set("color", "#00070cff")
                .set("font-family", "Poppins, sans-serif")
                .set("font-size", "16px")
                .set("margin-bottom", "4px");

            Span email = new Span(u.getEmail());
            email.getStyle()
                .set("color", "#666666")
                .set("font-family", "Poppins, sans-serif")
                .set("font-size", "14px");

            userInfo.add(username, email);

            // Create a seniority label to the right of the avatar
            String seniorityText = "";
            try {
                int s = u.getSeniority();
                if (s > 0) seniorityText = String.valueOf(s);
            } catch (Exception ex) {
                seniorityText = "";
            }
            Span senioritySpan = new Span(seniorityText);
            senioritySpan.getStyle()
                .set("font-weight", "600")
                .set("color", "#000000")
                .set("font-family", "Poppins, sans-serif")
                .set("font-size", "16px");

            // Place avatar, then expanding userInfo, then seniority at the far right
            HorizontalLayout row = new HorizontalLayout(avatar, userInfo, senioritySpan);
            row.setWidth("560px"); // Slightly narrower than container
            row.setAlignItems(Alignment.CENTER);
            row.setHeight("90px"); // Increased height to match avatar circle
            row.setJustifyContentMode(com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode.BETWEEN);
            // Make the middle column expand so the seniority stays aligned to the far right
            row.expand(userInfo);
            row.getStyle()
                .set("padding", "20px 24px")  // Increased vertical padding
                .set("border-radius", "8px")
                .set("margin", "4px 0")  // Added extra vertical margin
                .set("min-height", "90px");
            avatar.getStyle()
                .set("margin-right", "16px")
                .set("flex-shrink", "0"); // Prevent avatar from shrinking

            Button item = new Button(row);
            item.getStyle()
                .set("width", "100%")
                .set("text-align", "left")
                .set("padding", "0")
                .set("background", "white")
                .set("cursor", "pointer")
                .set("border", "none")
                .set("margin", "0 auto"); // Center the narrower row in container

            item.addClickListener(ev -> {
                if (selectedItem == item) {
                    // Clicking the same item again deselects it
                    selectedItem.getStyle().set("background", "none");
                    selectedItem = null;
                    editButton.setEnabled(false);
                    editButton.getStyle().set("opacity", "0.5");
                } else {
                    // Selecting a new item
                    if (selectedItem != null) {
                        selectedItem.getStyle().set("background", "none");
                    }
                    selectedItem = item;
                    item.getStyle().set("background", "#156fab22");
                    editButton.setEnabled(true);
                    editButton.getStyle().set("opacity", "1");
                }
                // Prevent the event from reaching the background
                ev.getSource().getElement().executeJs("event.stopPropagation()");
            });

            // Store user object in button's element data
            item.getElement().setProperty("_user", u.getUsername());

            listLayout.add(item);
        }

        prevButton.setEnabled(slice.hasPrevious());
        nextButton.setEnabled(slice.hasNext());
    }
}
