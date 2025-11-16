package se300.shiftlift; 

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.security.RolesAllowed;



@PageTitle("EditWorkstationView")
@Route("EditWorkstationView")
@RolesAllowed("ADMIN")

public class EditShiftViewView extends Composite<VerticalLayout> {

    public EditShiftViewView() {
        private    HorizontalLayout layoutRow = new HorizontalLayout();
        private    H1 h1 = new H1();
        private    VerticalLayout layoutColumn2 = new VerticalLayout();
        private    DatePicker datePicker = new DatePicker();
        private    Select select_Worker = new Select();
        private    HorizontalLayout layoutRow2 = new HorizontalLayout();
        private    Select select2_Section = new Select();
        private    TimePicker timePicker = new TimePicker();
        private    TimePicker timePicker2 = new TimePicker();
        private    HorizontalLayout layoutRow3 = new HorizontalLayout();
        private    Button button_save = new Button();
        private    Button button_cancel = new Button();
        private    Button button_delete = new Button();
        
        private Shift shift;

        public EditShiftViewView() {
            create_elements();
        }

        private void setShiftData(Shift shift) {
            datePicker.setValue(shift.getDate());

            // Additional code to set other fields based on shift data
        }



        private void create_elements() 
        {
            getContent().setWidth("100%");
            getContent().getStyle().set("flex-grow", "1");
            layoutRow.addClassName(Gap.MEDIUM);
            layoutRow.setWidth("100%");
            layoutRow.setHeight("min-content");
            layoutRow.setAlignItems(Alignment.START);
            layoutRow.setJustifyContentMode(JustifyContentMode.CENTER);
            h1.setText("Add Shift");
            h1.setWidth("max-content");

            layoutColumn2.setWidth("100%");
            layoutColumn2.getStyle().set("flex-grow", "1");

            datePicker.setLabel("Date");
            layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, datePicker);
            datePicker.setWidth("min-content");

            //Worker Select
            select.setLabel("Worker");
            select.setWidth("min-content");
            setSelectSampleData(select);

            layoutRow2.setWidthFull();
            layoutColumn2.setFlexGrow(1.0, layoutRow2);
            layoutRow2.addClassName(Gap.MEDIUM);
            layoutRow2.setWidth("100%");
            layoutRow2.getStyle().set("flex-grow", "1");

            //Workstation Section
            select2.setLabel("Section");
            select2.setWidth("min-content");
            setSelectSampleData(select2);

            //Time Pickers
            timePicker.setLabel("Start Time");
            timePicker.setWidth("min-content");
            timePicker2.setLabel("End Time");
            timePicker2.setWidth("min-content");

            layoutRow3.setWidthFull();
            layoutColumn2.setFlexGrow(1.0, layoutRow3);
            layoutRow3.addClassName(Gap.MEDIUM);
            layoutRow3.setWidth("100%");
            layoutRow3.getStyle().set("flex-grow", "1");
            layoutRow3.setAlignItems(Alignment.CENTER);
            layoutRow3.setJustifyContentMode(JustifyContentMode.CENTER);

            //Save Button
            button_save.setText("Save");
            button_save.setWidth("min-content");
            button_save.getStyle().set("background-color", "#156fabff");
            button_save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button_save.addClickListener(e -> {
            save_button_click_listener();
            });

            //Cancel Button
            buttonSecondary.setText("Cancel");
            buttonSecondary.setWidth("min-content");
            button_cancel.getStyle().set("color", "grey");
            button_cancel.addClickListener(e -> {
            cancel_button_click_listener();
            });

            //Delete Button
            buttonPrimary2.setText("Delete");
            layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary2);
            buttonPrimary2.setWidth("min-content");
            buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            button_delete.getStyle().set("background-color", "#9b0000ff");
            button_delete.addClickListener(e -> {
            delete_button_click_listener();
            });

            //Adding Elements to the View
            getContent().add(layoutRow);
            layoutRow.add(h1);
            getContent().add(layoutColumn2);
            layoutColumn2.add(datePicker);
            layoutColumn2.add(select);
            layoutColumn2.add(layoutRow2);
            layoutRow2.add(select2);
            layoutColumn2.add(timePicker);
            layoutColumn2.add(timePicker2);
            layoutColumn2.add(layoutRow3);
            layoutRow3.add(save);
            layoutRow3.add(cancel);
            layoutColumn2.add(delete);
            getContent().add(layoutRow3);
        }
    }

    p

    

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setSelectSampleData(Select select) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));

        select.setItems(sampleItems);
        select.setItemLabelGenerator(item -> ((SampleItem) item).label());
        select.setItemEnabledProvider(item -> !Boolean.TRUE.equals(((SampleItem) item).disabled()));
    }
}
