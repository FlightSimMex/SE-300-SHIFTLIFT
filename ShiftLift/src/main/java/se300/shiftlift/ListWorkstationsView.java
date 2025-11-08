package se300.shiftlift;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;


@PageTitle("List Workstations")
@Route("list-workstations")
@RolesAllowed("ADMIN")
public class ListWorkstationsView extends VerticalLayout implements BeforeEnterObserver {

    private final WorkstationService workstationService;
    private final VerticalLayout listLayout = new VerticalLayout();
    private final TextField searchField = new TextField();
    private final Button prevButton = new Button("Previous");
    private final Button nextButton = new Button("Next");
    private final Button editButton = new Button("Edit Workstation");
    private int currentPage = 0;
    private String currentQuery = "";
    private Button selectedItem = null;


    public ListWorkstationsView(WorkstationService workstatioService)
    {
        this.workstationService = workstatioService;
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setPadding(true);
        setSpacing(true);

        //Create Tittle and match styling
        H1 title = new H1("Workstations");
        title.getStyle()
            .set("color", "#156fabff")
            .set("font-family", "Poppins, sans-serif")
            .set("font-size", "48px")
            .set("margin-bottom", "24px");

        //Create logout button and add to top bar to the right
        Button logoutButton = new Button("Logout");
        logoutButton.getStyle().set("color", "#666666");
        logoutButton.addClickListener(e -> Auth.logoutToLogin());//Auth.logoutToLogin() is a static method for logging out users found in AAuth.java
        HorizontalLayout topBar = new HorizontalLayout(logoutButton);
        topBar.setWidthFull();
        topBar.setAlignItems(FlexComponent.Alignment.CENTER);
        topBar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        
        //Search field
        searchField.setPlaceholder("Search workstations...");
        searchField.setClearButtonVisible(true);
        searchField.setWidth("400");
        searchField.getStyle().set("font-family", "Poppins, sans-serif");
        searchField.addValueChangeListener(e -> {
            this.currentQuery = e.getValue() == null ? "" : e.getValue();
            this.currentPage = 0;
            loadWorkstations(currentQuery, currentPage);
        });

        //Navigation buttons
        Button newWorkstationButton = new Button("Create New Workstation");
        Button returnButton = new Button("Return");

        //Set button styles
        prevButton.getStyle().set("font-family", "Poppins, sans-serif");
        nextButton.getStyle().set("font-family", "Poppins, sans-serif");
        editButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif")
            .set("opacity", "0.5");
        editButton.setEnabled(false);
        newWorkstationButton.getStyle()
            .set("background-color", "#156fabff")
            .set("color", "white")
            .set("font-family", "Poppins, sans-serif"); 
        returnButton.getStyle()
            .set("font-family", "Poppins, sans-serif")
            .set("color", "grey");
        
        //Navigation button handlers
        newWorkstationButton.addClickListener(e -> UI.getCurrent().navigate("new-workstation"));
        returnButton.addClickListener(e -> UI.getCurrent().navigate(MainMenuView.class));

        //User url parameter to pass workstation name to edit workstaion view
        editButton.addClickListener(e ->{
            if(selectedItem != null) {
                String workstationName = selectedItem.getElement().getProperty("_workstation");
                java.util.Map<String, java.util.List<String>> parameters = Collections.singletonMap("name", 
                    java.util.Collections.singletonList(workstationName));
                QueryParameters qp = new QueryParameters(parameters);
                UI.getCurrent().navigate("edit-workstation", qp);
            }
        });

        //Add Search layout at the top with page navigation buttons
        HorizontalLayout searchLayout = new HorizontalLayout(searchField);
        searchLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        searchLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        searchLayout.setWidthFull();

        prevButton.addClickListener(e -> {
            if(currentPage > 0)
            {
                currentPage--;
                loadWorkstations(currentQuery, currentPage);
            }
        });

        nextButton.addClickListener(e -> {
            currentPage++;
            loadWorkstations(currentQuery, currentPage);
        });

        //Set list layout styling
        listLayout.setWidth("600px");
        listLayout.setPadding(false);
        listLayout.setSpacing(true);
        listLayout.setAlignItems(FlexComponent.Alignment.START);
        listLayout.getStyle()
            .set("gap", "24px")
            .set("Margin-top", "16px");

        //Create page navigation layout
        HorizontalLayout pageLayout = new HorizontalLayout(prevButton, nextButton);
        pageLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        pageLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        pageLayout.setSpacing(true);

        //Create buttons layout
        HorizontalLayout buttonLayout = new HorizontalLayout(newWorkstationButton, editButton, returnButton);
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonLayout.setSpacing(true);

        //Create Navigation layout for buttons set at bottom
        VerticalLayout bottomLayout = new VerticalLayout(pageLayout, buttonLayout);
        bottomLayout.setAlignItems(Alignment.CENTER);
        bottomLayout.setSpacing(true);
        bottomLayout.setPadding(true);
        bottomLayout.getStyle().set("margin-top", "24px");

        //Backgorund div for to allow for deselection of items
        Div backgroundDiv = new Div();
        backgroundDiv.setSizeFull();
        backgroundDiv.getStyle()
            .set("position", "fixed")
            .set("top", "0")
            .set("left", "0")
            .set ("z-index", "-1");
        backgroundDiv.addClickListener(e -> {
            if(selectedItem != null) {
               selectedItem.getStyle().set("background", "none");
               selectedItem = null;
               editButton.setEnabled(false);
               editButton.getStyle().set("opacity", "0.5");
            }
        });

        //Create man content container
        VerticalLayout contentLayout = new VerticalLayout(title, searchLayout, listLayout, bottomLayout);
        contentLayout.setSizeFull();
        contentLayout.setSpacing(true);
        contentLayout.setPadding(true);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        //Top bar needs to be added first to be at the top and match styling of other views
        add(topBar, backgroundDiv, contentLayout);

        loadWorkstations(currentQuery, currentPage);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn() || !Auth.isAdmin()) {
            Notification.show("Access denied: Admins only", 2000, Notification.Position.MIDDLE);
            event.rerouteTo("main-menu");
        }
    }
    
    private void loadWorkstations(String query, int page)
    {
        listLayout.removeAll();
        Slice<Workstation> slice;
        //Fetch workstations from database based on search query
        if(query == null || query.isEmpty())
        {
            slice = workstationService.searchByName("", PageRequest.of(page, 20));
        }else{
            slice = workstationService.searchByName(query, PageRequest.of(page, 20));
        }

        List<Workstation> workstations = slice.toList();
        
        //For each workstation create a button item in the list
        for(Workstation ws : workstations)
        {
            VerticalLayout workstationInfo = new VerticalLayout();
            workstationInfo.setSpacing(false);
            workstationInfo.setPadding(false);
            workstationInfo.setHeight("50px");
            workstationInfo.setAlignItems(FlexComponent.Alignment.START);
            workstationInfo.setJustifyContentMode(JustifyContentMode.CENTER);

            Span nameSpan = new Span(ws.getName());
            nameSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "#00070cff")
                .set("font-family", "Poppins, sans-serif")
                .set("font-size", "20px")
                .set("margin-bottom", "4px");
            
            Span hoursSpan = new Span((ws.getOperation_hours() != null ? ws.getOperation_hours().toString() : "Not Set"));
            hoursSpan.getStyle()
                .set("color", "#666666")
                .set("font-family", "Poppins, sans-serif")
                .set("font-size", "16px");

            workstationInfo.add(nameSpan, hoursSpan);

            Button item = new Button(workstationInfo);
            item.getStyle()
                .set("width", "100%")
                .set("text-align", "left")
                .set("padding", "0")
                .set("background", "white")
                .set("cursor", "pointer")
                .set("border", "none")
                .set("margin", "0 auto"); 
            
            item.addClickListener(e -> {
                if(selectedItem == item){
                    //Deselect if already selected
                    selectedItem.getStyle().set("background", "none");
                    selectedItem = null;
                    editButton.setEnabled(false);
                    editButton.getStyle().set("opacity", "0.5");
                }else{
                    //Select the workstation
                    if(selectedItem != null) {
                        selectedItem.getStyle().set("background", "none");
                    }
                    selectedItem = item;
                    selectedItem.getStyle().set("background", "#156fab22");
                    editButton.setEnabled(true);
                    
                    editButton.getStyle().set("opacity", "1")
                        .set("background-color", "#156fabff")
                        .set("color", "white");
                }
                e.getSource().getElement().executeJs("event.stopPropagation()"); //Force this action handler to run before background click
            });
            //Store workstaion name in element property for retrieval on edit
            item.getElement().setProperty("_workstation", ws.getName());
            listLayout.add(item);
        }

        //Enable/disable navigation buttons dynamically based on database slice info
        prevButton.setEnabled(slice.hasPrevious());
        nextButton.setEnabled(slice.hasNext());
    }   
}
