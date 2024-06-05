package ch.graueenergie.energieclash.view;

import ch.graueenergie.energieclash.util.Language;
import javafx.fxml.FXML;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractSynchView {
    protected final PropertyChangeSupport support;
    protected boolean readyToMoveOn;
    protected final Language language;
    protected final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * @param language language in which the view should be displayed
     */
    public AbstractSynchView(Language language) {
        this.language = language;
        support = new PropertyChangeSupport(this);
    }

    /**
     * @return the readiness status
     */
    public boolean isReadyToMoveOn() {
        return readyToMoveOn;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    @FXML
    public abstract void initialize();

    protected void setupScreen() {
        logger.info(String.format("Setting up screen %s", this.getClass().getSimpleName()));
    }
}
