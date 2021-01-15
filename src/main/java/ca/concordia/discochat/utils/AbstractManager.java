package ca.concordia.discochat.utils;

public abstract class AbstractManager implements IModProvider {
    private IMod mod;

    public AbstractManager(IMod mod) {
        this.mod = mod;
    }

    @Override
    public IMod getMod() {
        return mod;
    }
}
