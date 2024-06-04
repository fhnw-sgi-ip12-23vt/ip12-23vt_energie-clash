package ch.graueenergie.energieclash.util;

import java.beans.PropertyChangeEvent;

public enum PropertyName {
    START,
    ENERGIE_CLASH_ROUND_ANSWER,
    ENERGIE_CLASH_ROUND_TIMER_ELAPSED,
    EXPLANATION,
    END,
    ADMIN_DASHBOARD_ADD,
    ADMIN_DASHBOARD_EDIT,
    ADMIN_DASHBOARD_EXIT,
    ADMIN_DETAIL_SAVE,
    ADMIN_DETAIL_DELETE,
    ADMIN_DETAIL_EXIT,
    GAME_STARTER_START,
    GAME_STARTER_ADMIN,
    GAME_STARTER_GAME_MODE,
    GAME_STARTER_DIFFICULTY,
    GAME_STARTER_TUTORIAL,
    GAME_RESTART;

    /**
     * @param source the source.
     * @return a {@link PropertyChangeEvent} with the enums name and {@code null} as values.
     */
    public PropertyChangeEvent getPropertyChangeEvent(Object source) {
        return new PropertyChangeEvent(source, name(), null, null);
    }
}
