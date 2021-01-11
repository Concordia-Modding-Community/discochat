package ca.concordia.b4dis;

/**
 * Void input - T output Lambda method.
 */
public interface Provider<T> {
    T invoke();
}