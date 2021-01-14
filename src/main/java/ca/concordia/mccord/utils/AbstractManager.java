package ca.concordia.mccord.utils;

public abstract class AbstractManager {
    private IMod mod;

    public AbstractManager(IMod mod) {
        this.mod = mod;
    }

    public IMod getMod() {
        return mod;
    }
}
