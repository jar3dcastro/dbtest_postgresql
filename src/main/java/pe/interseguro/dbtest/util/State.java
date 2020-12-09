package pe.interseguro.dbtest.util;

public enum State {

    ACTIVE((short)1), INACTIVE ((short)0);

    public final short code;

    State(short code) {
        this.code = code;
    }

    public State valueOf (short code) {
        switch (code) {
            case 0:
                return INACTIVE;
            case 1:
                return ACTIVE;
            default:
                throw new IllegalArgumentException("Estado con codigo incorrecto " + code);
        }
    }
}
