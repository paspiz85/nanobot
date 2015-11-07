package it.paspiz85.nanobot.ui;

/**
 * Interface for controllers that know its application.
 *
 * @author paspiz85
 */
public interface ApplicationAwareController {

    void afterShow();

    void setApplication(Application application);
}
