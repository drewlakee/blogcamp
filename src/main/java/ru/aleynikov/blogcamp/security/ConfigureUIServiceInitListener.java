package ru.aleynikov.blogcamp.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.view.LoginView;
import ru.aleynikov.blogcamp.view.MainView;
import ru.aleynikov.blogcamp.view.PasswordRestoreView;
import ru.aleynikov.blogcamp.view.SignUpView;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event
     *            before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        if (!LoginView.class.equals(event.getNavigationTarget())
                && !SignUpView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()
                && !MainView.class.equals(event.getNavigationTarget())
                && !PasswordRestoreView.class.equals(event.getNavigationTarget())) {
            event.rerouteTo(LoginView.class);
        }
    }
}
