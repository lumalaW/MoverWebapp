package View;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by william
 * A class to hold all the views. I.e contains the menu layout and one of the other views.
 */


public class MenuLayout extends HorizontalLayout {

    CssLayout contentArea = new CssLayout();
    CssLayout menuArea = new CssLayout();

    public MenuLayout(){
        setSizeFull();

        menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);
        int height = UI.getCurrent().getPage().getBrowserWindowHeight();
        menuArea.setHeight(height, Unit.PIXELS);

        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();

        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);
    }

    public ComponentContainer getContentContainer(){
        return contentArea;
    }

    public void addMenu(Component menu){
        menu.addStyleName(ValoTheme.MENU_PART);
        menuArea.addComponent(menu);
    }
}
