package pe.interseguro.dbtest.entity;

import java.time.LocalDateTime;

public class Entity {

    private Integer id;
    private Integer intField;
    private Float floatField;
    private String stringField;
    private LocalDateTime localDateTimeField;

    public Integer getId () {
        return id;
    }
    public void setId (Integer id) {
        this.id = id;
    }

    public Integer getIntField () {
        return intField;
    }
    public void setIntField (Integer intField) {
        this.intField = intField;
    }

    public Float getFloatField () {
        return floatField;
    }
    public void setFloatField (Float floatField) {
        this.floatField = floatField;
    }

    public String getStringField () {
        return stringField;
    }
    public void setStringField (String stringField) {
        this.stringField = stringField;
    }

    public LocalDateTime getLocalDateTimeField () {
        return localDateTimeField;
    }
    public void setLocalDateTimeField (LocalDateTime localDateTimeField) {
        this.localDateTimeField = localDateTimeField;
    }

}
