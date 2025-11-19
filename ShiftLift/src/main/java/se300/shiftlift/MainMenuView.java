package se300.shiftlift;

import java.time.Duration;
import java.time.LocalTime;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("main-menu")
public class MainMenuView extends AppLayout implements BeforeEnterObserver {
    private static final String[] days = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    };
    private static final String[] dates = {
        "11/2/25", "11/3/25", "11/4/25", "11/5/25", "11/6/25"
        //
    };
    public MainMenuView() {
        boolean admin = Auth.isAdmin();
        
        // Create styled drawer menu
        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.setPadding(true);
        drawerLayout.setSpacing(true);
        
        if(admin){
            // Routes that will be in the hamburger for navigation
            RouterLink manageWorkersLink = new RouterLink("Manage Workers", ListUsersView.class);
            RouterLink manageWorkstationsLink = new RouterLink("Manage Workstations", ListWorkstationsView.class);
            RouterLink manageSchedulesLink = new RouterLink("Manage Schedules", ManageSchedulesView.class);
            RouterLink changePasswordLink = new RouterLink("Change Password", ChangePasswordView.class);
            
            // Apply styling to each link
            styleRouterLink(manageWorkersLink);
            styleRouterLink(manageWorkstationsLink);
            styleRouterLink(manageSchedulesLink);
            styleRouterLink(changePasswordLink);
            
            drawerLayout.add(manageWorkersLink, manageWorkstationsLink, manageSchedulesLink, changePasswordLink);
        }
        else{
            RouterLink changePasswordLink = new RouterLink("Change Password", ChangePasswordView.class);
            styleRouterLink(changePasswordLink);
            drawerLayout.add(changePasswordLink);
        }
        
        addToDrawer(drawerLayout);

        // Creates a hamburger for navigation to other tabs
        DrawerToggle toggle = new DrawerToggle();
        toggle.getStyle()
            .set("color", "#156fabff")
            .set("background-color", "#f5f5f5")
            .set("border-radius", "4px");

        // Logout Button
        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyle()
            .set("color", "#666666")
            .set("font-family", "Poppins, sans-serif")
            .set("margin-right", "20px");
        logoutBtn.addClickListener(e -> Auth.logoutToLogin());

        // Navbar layout (this is the header)
        var header = new HorizontalLayout(toggle, logoutBtn);
        header.setWidthFull();
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setPadding(true);
        header.setSpacing(true);
        header.getStyle()
            .set("background-color", "white")
            .set("padding", "16px 20px");
        addToNavbar(header);

        // Title (moved below navbar)
        H1 title = new H1("Pending Schedule");
        title.getStyle()
             .set("color", "#156fabff")
             .set("font-family", "Poppins, sans-serif")
             .set("margin", "20px 0 30px 0")
             .set("text-align", "center");

        // week navigation
        Button prevWeek = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
        Button nextWeek = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));

        H3 weekLabel = new H3("11/02/2025 - 11/08/2025");
        weekLabel.getStyle()
                 .set("color", "#156fabff")
                 .set("font-family", "Poppins, sans-serif")
                 .set("margin", "0 0 24px 0");

        HorizontalLayout weekHeader = new HorizontalLayout(prevWeek, weekLabel, nextWeek);
        weekHeader.setAlignItems(Alignment.CENTER);
        weekHeader.setJustifyContentMode(JustifyContentMode.BETWEEN);
        weekHeader.setWidthFull();

        // calendar header (below weekHeader)
        HorizontalLayout calendarHeader = new HorizontalLayout();
        calendarHeader.getStyle().set("padding-left", "40px");
        calendarHeader.setWidthFull();
        calendarHeader.setSpacing(false);
        calendarHeader.setPadding(false);
        calendarHeader.setMargin(false);
        calendarHeader.setJustifyContentMode(JustifyContentMode.START);

        //Creates columns for each date
        for (int i = 0; i < 5; i++) {
            VerticalLayout dayCol = new VerticalLayout();
            dayCol.setWidth("20%");
            
            H4 dayName = new H4(days[i]);
            Span date = new Span(dates[i]);
            
            dayCol.add(dayName, date);
            dayCol.setAlignItems(Alignment.CENTER);

            dayCol.setPadding(false);
            dayCol.setSpacing(false);
            dayCol.setMargin(false);

            dayCol.getStyle().set("border", "1px solid #d9d9d9");
            dayCol.getStyle().set("padding", "8px");
            dayCol.getStyle().set("box-sizing", "border-box");

            calendarHeader.add(dayCol);
        }   

        Component scheduleGrid = createScheduleGrid();

        // Place title and headers into the AppLayout content area
        VerticalLayout content = new VerticalLayout(title, weekHeader, calendarHeader, scheduleGrid);
        content.setWidthFull();
        content.setAlignItems(Alignment.CENTER);
        // top 10px, right 60px, bottom 0px, left 60px
        content.getStyle().set("padding", "10px 60px 0 60px");
        content.getStyle().set("box-sizing", "border-box");
        content.setPadding(false); //we've set padding via CSS
        content.setSpacing(true);
        setContent(content);
    }

    private Component createScheduleGrid() {
    // Whole grid area under the day labels
    HorizontalLayout grid = new HorizontalLayout();
    grid.setWidthFull();
    grid.setHeight("500px"); // can tweak
    grid.getStyle()
        .set("border", "1px solid #e0e0e0")
        .set("box-sizing", "border-box")
        .set("overflow", "hidden");          //clip anything outside
    grid.setSpacing(false);
    grid.setPadding(false);

    //Left time axis
    VerticalLayout timeColumn = new VerticalLayout();
    timeColumn.setWidth("40px");
    timeColumn.setPadding(false);
    timeColumn.setSpacing(false);
    timeColumn.setHeightFull();              //same height as grid
    timeColumn.getStyle().set("border-right", "1px solid #e0e0e0");

    LocalTime startTime = LocalTime.of(5, 0);   //first label
    LocalTime endTime   = LocalTime.of(17, 0);  //last label
    int slotMinutes = 60;                       //1-hour steps
    int pxPerSlot = 40;                         //must match addShiftBlock

    for (LocalTime t = startTime; !t.isAfter(endTime); t = t.plusMinutes(slotMinutes)) {
        Span label = new Span(t.toString());    //05:00, 06:00, etc...
        label.getStyle()
             .set("font-size", "11px")
             .set("height", pxPerSlot + "px")
             .set("display", "flex")
             .set("align-items", "flex-start");
        timeColumn.setAlignItems(Alignment.CENTER);
        timeColumn.add(label);
    }

    grid.add(timeColumn);

    //day columns (5 days)
    for (int dayIndex = 0; dayIndex < 5; dayIndex++) {
        Div dayCol = new Div();
        dayCol.getStyle()
              .set("flex", "1")
              .set("position", "relative")
              .set("border-left", "1px solid #f0f0f0")
              .set("overflow", "hidden"); // 👈 clip bars inside each column
        dayCol.setHeightFull();           // 👈 same height as grid

    //Example shifts (just to see them)
    addShiftBlock(dayCol, LocalTime.of(6, 0), LocalTime.of(8, 0), 0);  // blue
    addShiftBlock(dayCol, LocalTime.of(9, 0), LocalTime.of(12, 0), 2); // orange

        grid.add(dayCol);
    }

    return grid;
}


    private void addShiftBlock(Div dayCol,
                           LocalTime shiftStart,
                           LocalTime shiftEnd,
                           int workstationIndex) {

    LocalTime gridStart = LocalTime.of(5, 0); // 👈 match startTime above
    int slotMinutes = 60;
    int pxPerSlot   = 40;

    int minutesFromStart = (int)Duration.between(gridStart, shiftStart).toMinutes();
    int durationMinutes  = (int)Duration.between(shiftStart, shiftEnd).toMinutes();

    int topPx    = (int)(minutesFromStart/(double) slotMinutes * pxPerSlot);
    int heightPx = (int)(durationMinutes/(double) slotMinutes * pxPerSlot);

    int wsCount = 5;
    double wsWidth = 100.0 / wsCount;
    double leftPercent = workstationIndex * wsWidth;

    Div block = new Div();
    block.getStyle()
         .set("position", "absolute")
         .set("top", topPx + "px")
         .set("left", leftPercent + "%")
         .set("width", wsWidth + "%")
         .set("height", heightPx + "px")
         .set("border-radius", "6px")
         .set("background-color", getWorkstationColor(workstationIndex));

    dayCol.add(block);
}

private String getWorkstationColor(int idx) {
    switch (idx) {
        case 0: return "#156fabff";
        case 1: return "#4CAF50";   
        case 2: return "#FF9800";   
        case 3: return "#9C27B0";   
        default: return "#F44336";  
    }
}

private void styleRouterLink(RouterLink link) {
    link.getStyle()
        .set("color", "#156fabff")
        .set("font-family", "Poppins, sans-serif")
        .set("text-decoration", "none")
        .set("padding", "8px 0")
        .set("display", "block")
        .set("font-size", "16px");
}


    @Override
        public void beforeEnter(BeforeEnterEvent event) {
        if (!Auth.isLoggedIn()) {
            event.rerouteTo(LoginView.class);
        }
        }
}