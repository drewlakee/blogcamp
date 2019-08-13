package ru.aleynikov.blogcamp.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.view.LogInView;
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
        if (!LogInView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()
                && !SignUpView.class.equals(event.getNavigationTarget())) {
            event.rerouteTo(LogInView.class);
        }
    }
}
