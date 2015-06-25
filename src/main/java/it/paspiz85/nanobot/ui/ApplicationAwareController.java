package it.paspiz85.nanobot.ui;

import javafx.application.Application;

/**
 * Interface for controllers that know its application.
 * 
 * @author v-ppizzuti
 */
public interface ApplicationAwareController {

    void setApplication(Application application);
}
