package controllers;

import models.User;

/**
 * An interface class representing a contract for a controller
 * class that loads some data provided user data
 * <p>
 * All implementing classes must provide functionality for loading user data
 */
public interface Loadable {
    /**
     * Load data or complete a process that relies on the current user data.
     *
     * @param user object representing the currently logged-in user
     */
    void load(User user);
}
