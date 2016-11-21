package View.Login;

import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import DataAccess.FetchData;
import Application.Encryption;
import DataAccess.UserLogin.UserAuthentication;
import View.MainView;

import java.util.ArrayList;

/**
 * Created by william
 * A java class to create the login view and authenticate users
 */
public class LoginView extends VerticalLayout implements View {

    private Navigator navigator;
    private String mainView = "mainView";
    private String name;


    public LoginView(){

        setSizeFull();
        Component loginForm = createLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);

    }

    // A method to create layout holding text fields and button
    private Component createTextfield(){
        final HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("username");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField passwordField = new PasswordField("Password");
        passwordField.setIcon(FontAwesome.LOCK);
        passwordField.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("SIGN IN");
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signin.focus();

        signin.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent){
                String user = username.getValue();
                String pass = passwordField.getValue();
                Notification notification;

                ArrayList<String> users = FetchData.getAdminUsers();
                if(users.contains(user)){
                    if(isUserValid(user,pass)){ // successful login
                        notification = new Notification("Successful sign in!", Notification.Type.HUMANIZED_MESSAGE);
                        notification.setPosition(Position.BOTTOM_CENTER);
                        notification.setStyleName("tray dark small closable login-help");
                        notification.setDelayMsec(2000);
                        notification.show(Page.getCurrent());

                        // get the name
                        name = userNames(user, pass);

                        // navigator
                        navigator.addView(mainView, new MainView(name));

                        // navigate to the next
                        navigator.navigateTo(mainView);

                    }else{ // unsuccessful login
                        notification = new Notification("Wrong credentials entered. Re-enter credentials", Notification.Type.ERROR_MESSAGE);
                        notification.setPosition(Position.BOTTOM_CENTER);
                        notification.setStyleName("tray dark small closable login-help");
                        notification.setDelayMsec(2000);
                        notification.show(Page.getCurrent());
                        signin.setComponentError(new UserError("Wrong credentials entered."));
                    }

                }

                else{

                }
            }
        });

        fields.addComponents(username,passwordField,signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
        return fields;
    }

    /**
     * Method to create the welcome label
     */
    private Component createLabels(){
        CssLayout labels = new CssLayout();
        labels.addStyleName("labels");

        Label welcome = new Label("Welcome");
        welcome.setSizeUndefined();
        welcome.addStyleName(ValoTheme.LABEL_H4);
        welcome.addStyleName(ValoTheme.LABEL_COLORED);
        labels.addComponent(welcome);

        Label title = new Label("M.O.V.E.R");
        title.setSizeUndefined();
        title.addStyleName(ValoTheme.LABEL_H3);
        title.addStyleName(ValoTheme.LABEL_LIGHT);

        return labels;


    }

    // A method to create panel to hold the two above components
    private Component createLoginForm(){
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setSpacing(true);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(createLabels());
        loginPanel.addComponent(createTextfield());
        loginPanel.addComponent(new CheckBox("Remember me", true));
        return loginPanel;
    }


    // A method to check whether the user is valid
    private boolean isUserValid(String username, String password){

        String finalPassword = Encryption.getFinalPassword(username,password); // encryption
        System.out.println(finalPassword);
        String finalPassword1 = UserAuthentication.authentication(username); // Get password from database
        System.out.println(finalPassword1);

        if(finalPassword.equals(finalPassword1)){
            System.out.println("Same");
            return true;
        }else {
            System.out.println("Not same");
            return false;
        }
    }


    // A method to get user names
    private String userNames(String username, String password){
        if(isUserValid(username, password)){
            String names = UserAuthentication.getNames(username);
            System.out.println(names);
            return names;
        }else{
            return null;
        }
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        navigator = viewChangeEvent.getNavigator();

    }

}
