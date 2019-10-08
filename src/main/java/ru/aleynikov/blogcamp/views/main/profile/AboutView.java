package ru.aleynikov.blogcamp.views.main.profile;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
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
import ru.aleynikov.blogcamp.security.SessionManager;
import ru.aleynikov.blogcamp.service.CityService;
import ru.aleynikov.blogcamp.service.CountryService;
import ru.aleynikov.blogcamp.service.UserService;
import ru.aleynikov.blogcamp.staticResources.StaticResources;
import ru.aleynikov.blogcamp.views.main.ProfileView;

import java.util.*;

@Route(value = "about", layout = ProfileView.class)
@PageTitle("Profile - About")
@StyleSheet(StaticResources.PROFILE_VIEW_STYLES)
public class AboutView extends Composite<Div> implements HasComponents, BeforeEnterObserver {

    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SessionManager sessionManager;

    private User currentUser;

    private List<Country> countryList;
    private TreeSet<String> countryNamesTree;

    private List<City> cityList;
    private TreeSet<String> cityNamesTree;

    private VerticalLayout mainLayout = new VerticalLayout();
    private VerticalLayout leftBodyAboutLayout = new VerticalLayout();
    private VerticalLayout centerBodyAboutLayout = new VerticalLayout();
    private VerticalLayout rightBodyAboutLayout = new VerticalLayout();
    private VerticalLayout footAboutLayout = new VerticalLayout();

    private HorizontalLayout bodyAboutLayout = new HorizontalLayout();
    private HorizontalLayout footerHorizontalLayout = new HorizontalLayout();

    private TextField firstNameField = new TextField();
    private TextField lastNameField = new TextField();

    private DatePicker birthdayPicker = new DatePicker();

    private Select<String> countrySelect = new Select<>();
    private Select<String> citySelect = new Select<>();

    private TextArea statusArea = new TextArea();

    private Button updateButton = new Button("Update");

    private Span successfullyUpdatedSpan = new Span("Successfully updated!");

    private H2 infoAbout = new H2("About profile");

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

        statusArea.setLabel("Status");
        statusArea.addClassName("margin-none");
        statusArea.setSizeFull();
        statusArea.setMaxLength(50);

        rightBodyAboutLayout.add(statusArea);

        bodyAboutLayout.add(leftBodyAboutLayout, centerBodyAboutLayout, rightBodyAboutLayout);

        footAboutLayout.setWidth("100%");

        footerHorizontalLayout.setWidth("100%");

        updateButton.addClassName("main-button");

        successfullyUpdatedSpan.setVisible(false);
        successfullyUpdatedSpan.addClassName("success");
        footerHorizontalLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, successfullyUpdatedSpan);

        footerHorizontalLayout.add(updateButton, successfullyUpdatedSpan);

        footAboutLayout.add(footerHorizontalLayout);

        mainLayout.add(infoAbout, bodyAboutLayout, footAboutLayout);

        add(mainLayout);

        countrySelect.addValueChangeListener(event -> {
            String countrySelectValue;

            cityNamesTree.clear();

            if (countrySelect.getValue() == null)
                countrySelectValue = countrySelect.getEmptySelectionCaption();
            else
                countrySelectValue = countrySelect.getValue();

            cityList.stream().filter((x) -> x.getCountryName().equals(countrySelectValue)).forEach((x) -> cityNamesTree.add(x.getCityName()));
            citySelect.setEmptySelectionAllowed(false);
            citySelect.setItems(cityNamesTree);
            citySelect.setEnabled(true);
        });

        updateButton.addClickListener(event -> {
            String countrySelectValue;
            String citySelectValue;
            HashMap<String, Object> infoForUpdate = new LinkedHashMap<>();
            Country country = new Country();
            City city = new City();

            infoForUpdate.put("full_name", firstNameField.getValue().strip() + " " + lastNameField.getValue().strip());
            infoForUpdate.put("birthday", birthdayPicker.getValue());

            if (countrySelect.getValue() == null)
                countrySelectValue = countrySelect.getEmptySelectionCaption();
            else
                countrySelectValue = countrySelect.getValue();

            try {
                country = countryList.stream().filter((x) -> x.getCountryName().equals(countrySelectValue)).findAny().get();
            } catch (NoSuchElementException e) {
                country.setId(-1);
            }
            infoForUpdate.put("country", country.getId());

            if (citySelect.getValue() == null)
                citySelectValue = citySelect.getEmptySelectionCaption();
            else
                citySelectValue = citySelect.getValue();

            try {
                city = cityList.stream().filter((x) -> x.getCityName().equals(citySelectValue)).findAny().get();
            } catch (NoSuchElementException e) {
                city.setId(-1);
            }

            infoForUpdate.put("city", city.getId());
            infoForUpdate.put("status", statusArea.getValue().strip());
            infoForUpdate.put("current_user_id", SecurityUtils.getPrincipal().getId());

            userService.updateUserAboutInfo(infoForUpdate);

            sessionManager.updateSessionUser();

            UI.getCurrent().navigate("profile/about");

            successfullyUpdatedSpan.setVisible(true);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        setUserCurrentInfoIntoForm();
    }

    public void setUserCurrentInfoIntoForm() {
        currentUser = SecurityUtils.getPrincipal();

        countryList = countryService.getAllCountriesList();
        cityList = cityService.getAllCities();

        if (currentUser.getFullName() != null) {
            String[] userNames = currentUser.getFullName().split(" ");
            int userFirstNameId = 0;
            String lastName = "";

            for (int i = 0; i < userNames.length; i++) {
                if (i == userFirstNameId)
                    firstNameField.setValue(userNames[i]);
                else
                    lastName += userNames[i] + " ";
            }

            lastNameField.setValue(lastName.strip());
        }

        if (currentUser.getBirthday() != null) {
            birthdayPicker.setValue(currentUser.getBirthday().toLocalDate());
        }

        if (currentUser.getCountry() != null) {
            countrySelect.setEmptySelectionAllowed(true);
            countrySelect.setEmptySelectionCaption(currentUser.getCountry());
        }

        countryNamesTree = new TreeSet<>();
        countryList.stream().filter((x) -> !x.getCountryName().equals(currentUser.getCountry())).forEach((x) -> countryNamesTree.add(x.getCountryName()));
        countrySelect.setItems(countryNamesTree);

        if (currentUser.getCity() != null) {
            citySelect.setEmptySelectionAllowed(true);
            citySelect.setEmptySelectionCaption(currentUser.getCity());
        } else
            citySelect.setEnabled(false);

        cityNamesTree = new TreeSet<>();
        cityList.stream().filter((x) -> !x.getCityName().equals(currentUser.getCity()) && x.getCountryName().equals(countrySelect.getValue())).forEach((x) -> cityNamesTree.add(x.getCityName()));
        citySelect.setItems(cityNamesTree);

        if (currentUser.getStatus() != null) {
            statusArea.setValue(currentUser.getStatus());
        }
    }
}
