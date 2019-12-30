package ru.aleynikov.blogcamp.ui.views.profile;

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
import ru.aleynikov.blogcamp.domain.models.City;
import ru.aleynikov.blogcamp.domain.models.Country;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.security.SessionState;
import ru.aleynikov.blogcamp.services.CityService;
import ru.aleynikov.blogcamp.services.CountryService;
import ru.aleynikov.blogcamp.services.UserService;
import ru.aleynikov.blogcamp.ui.statics.StaticContent;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = "about", layout = ProfileLayout.class)
@PageTitle("Profile - About")
@StyleSheet(StaticContent.PROFILE_STYLES)
public class AboutView extends Composite<Div> implements HasComponents, BeforeEnterObserver {

    @Autowired
    private UserService userService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private SessionState sessionState;

    private User userInSession = SecurityUtils.getPrincipal();

    private HashMap<String, Country> countriesMap = new HashMap<>();

    private HashMap<String, City> citiesMap = new HashMap<>();

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
            citySelect.clear();
            citySelect.setEmptySelectionCaption("");

            TreeSet cities = new TreeSet();
            for (City city : citiesMap.values()) {
                if (city.getCountry().getName().equals((countrySelect.getValue() == null) ? countrySelect.getEmptySelectionCaption() : countrySelect.getValue()))
                    cities.add(city.getName());
            }

            citySelect.setItems(cities);
        });

        updateButton.addClickListener(event -> {
            HashMap<String, Object> infoForUpdate = new LinkedHashMap<>();
            infoForUpdate.put("full_name", firstNameField.getValue().strip() + " " + lastNameField.getValue().strip());
            infoForUpdate.put("birthday", birthdayPicker.getValue());
            int countryId;
            try {
                countryId = (countrySelect.getValue() != null) ? countriesMap.get(countrySelect.getValue()).getId()
                        : countriesMap.get(countrySelect.getEmptySelectionCaption()).getId();
            } catch (NullPointerException e) {
                countryId = -1;
            }
            infoForUpdate.put("country", countryId);
            int cityId;
            try {
                cityId = (citySelect.getValue() != null) ? citiesMap.get(citySelect.getValue()).getId()
                        : citiesMap.get(citySelect.getEmptySelectionCaption()).getId();
            } catch (NullPointerException e) {
                cityId = -1;
            }
            infoForUpdate.put("city", cityId);
            infoForUpdate.put("status", statusArea.getValue().strip());
            infoForUpdate.put("current_user_id", SecurityUtils.getPrincipal().getId());
            userService.updateUserAboutInfo(infoForUpdate);
            sessionState.updateUserPrincipals();

            UI.getCurrent().navigate("profile/about");
            successfullyUpdatedSpan.setVisible(true);
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        setCurrentUserInfo(userInSession);
        setLocationSelectors();
    }

    private void setCurrentUserInfo(User user) {
        if (!user.getFullName().strip().isEmpty()) {
            ArrayList<String> userNames = new ArrayList<>(Arrays.asList(user.getFullName().split(" ")));
            Iterator namesIterator = userNames.iterator();

            firstNameField.setValue(namesIterator.next().toString());

            String secondNames = "";
            while (namesIterator.hasNext())
                secondNames += namesIterator.next().toString();

            lastNameField.setValue(secondNames);
        }

        if (user.getBirthday() != null) {
            birthdayPicker.setValue(user.getBirthday().toLocalDate());
        }

        if (!user.getStatus().strip().isEmpty()) {
            statusArea.setValue(user.getStatus());
        }

    }

    private void setLocationSelectors() {
        List<Country> countryList = countryService.getAllCountriesList();
        List<City> cityList = cityService.getAllCitiesList();

        countryList.forEach(country -> countriesMap.put(country.getName(), country));
        cityList.forEach(city -> citiesMap.put(city.getName(), city));

        countrySelect.setEmptySelectionAllowed(true);
        if (userInSession.getCountry() != null) {
            countrySelect.setEmptySelectionCaption(userInSession.getCountry().getName());
            countrySelect.setItems(
                    countriesMap.keySet().stream().filter(country -> !country.equals(userInSession.getCountry().getName())).collect(Collectors.toSet())
            );
        } else
            countrySelect.setItems(countriesMap.keySet());

        citySelect.setEmptySelectionAllowed(true);
        if (userInSession.getCity().isPresent()) {
            citySelect.setEmptySelectionCaption(userInSession.getCity().map(City::getName).get());
            citySelect.setItems(
                    citiesMap.keySet().stream().filter(city -> !city.equals(userInSession.getCity().map(City::getName).get()) && citiesMap.get(city).getCountry().getName().equals(countrySelect.getEmptySelectionCaption())).collect(Collectors.toSet())
            );
        } else
            citySelect.setItems(citiesMap.keySet().stream().filter(city -> citiesMap.get(city).getCountry().getName().equals(countrySelect.getEmptySelectionCaption())));
    }
}
