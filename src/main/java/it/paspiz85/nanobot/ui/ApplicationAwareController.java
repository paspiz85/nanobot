package it.paspiz85.nanobot.ui;

import javafx.application.Application;

/**
 * Interface for controllers that know its application.
 *
 * @author paspiz85
 */
public interface ApplicationAwareController {

    void setApplication(Application application);
}
