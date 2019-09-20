package ru.aleynikov.blogcamp.views.main.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.aleynikov.blogcamp.model.City;
import ru.aleynikov.blogcamp.model.Country;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.ProfileView;

import java.time.LocalDate;
import java.util.Calendar;

@Route(value = "about", layout = ProfileView.class)
@PageTitle("Profile - About")
@StyleSheet(StaticResources.PROFILE_VIEW_STYLES)
public class AboutView extends Composite<Div> implements HasComponents, BeforeEnterObserver {

    @Autowired
    private UserService userService;

    private User currentUser;

    private Calendar userCalendar = Calendar.getInstance();

    private VerticalLayout mainLayout = new VerticalLayout();
    private VerticalLayout leftBodyAboutLayout = new VerticalLayout();
    private VerticalLayout centerBodyAboutLayout = new VerticalLayout();
    private VerticalLayout rightBodyAboutLayout = new VerticalLayout();
    private VerticalLayout footAboutLayout = new VerticalLayout();

    private HorizontalLayout bodyAboutLayout = new HorizontalLayout();

    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();

    private DatePicker birthdayPicker = new DatePicker();

    private Select<Country> countrySelect = new Select<>();
    private Select<City> citySelect = new Select<>();

    private TextArea aboutArea = new TextArea();

    private Button updateButton = new Button("Update");

    private H2 infoAbout = new H2("Account information");

    public AboutView() {
        mainLayout.setSizeFull();
        mainLayout.addClassName("margin-none");

        bodyAboutLayout.setSizeFull();
        bodyAboutLayout.addClassName("margin-none");

        leftBodyAboutLayout.setWidth(null);

        firstNameField.addClassName("margin-none");
        firstNameField.setLabel("First name");

        lastNameField.addClassName("margin-none");
        lastNameField.setLabel("Last name");

        birthdayPicker.addClassName("margin-none");
        birthdayPicker.setLabel("Birthday");

        leftBodyAboutLayout.add(firstNameField, lastNameField, birthdayPicker);

        centerBodyAboutLayout.setWidth(null);

        countrySelect.addClassName("margin-none");
        countrySelect.setLabel("Country");

        citySelect.addClassName("margin-none");
        citySelect.setLabel("City");

        centerBodyAboutLayout.add(countrySelect, citySelect);

        rightBodyAboutLayout.setWidth("100%");

        aboutArea.setLabel("About");
        aboutArea.addClassName("margin-none");
        aboutArea.setSizeFull();

        rightBodyAboutLayout.add(aboutArea);

        bodyAboutLayout.add(leftBodyAboutLayout, centerBodyAboutLayout, rightBodyAboutLayout);

        footAboutLayout.setWidth("100%");

        updateButton.addClassName("main-button");
        footAboutLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, updateButton);

        footAboutLayout.add(updateButton);

        mainLayout.add(infoAbout, bodyAboutLayout, footAboutLayout);

        add(mainLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        setUserInfoIntoForm();
    }

    public void setUserInfoIntoForm() {
        if (currentUser == null) {
            currentUser = SecurityUtils.getPrincipal();
        }

        if (currentUser.getFullName() != null) {
            String[] userNames = currentUser.getFullName().split(" ");
            int userFirstNameId = 0;

            for (int i = 0; i < userNames.length; i++) {
                if (i == userFirstNameId)
                    firstNameField.setValue(userNames[i]);
                else
                    lastNameField.setValue(lastNameField.getValue() + userNames[i]);
            }
        }

        if (currentUser.getBirthday() != null) {
            userCalendar.setTime(currentUser.getBirthday());
            birthdayPicker.setValue(LocalDate.of(userCalendar.get(Calendar.YEAR), userCalendar.get(Calendar.MONTH), userCalendar.get(Calendar.DAY_OF_MONTH)));
        }

        //
        // TODO: CITY AND COUNTRY
        //

        if (currentUser.getAbout() != null) {
            aboutArea.setValue(currentUser.getAbout());
        }
    }
}
