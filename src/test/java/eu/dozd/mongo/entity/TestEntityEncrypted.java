package eu.dozd.mongo.entity;

import eu.dozd.mongo.annotation.*;

import java.util.*;

@Entity
public class TestEntityEncrypted {

    @Id
    private String id;
    @Encrypted
    private int i;
    private boolean checked;
    @Encrypted
    private String name;
    @NonNull
    private Integer j;
    @Encrypted
    private byte[] byteArray;
    private Map<String, Integer> map;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getJ() {
        return j;
    }

    public void setJ(Integer j) {
        this.j = j;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }
}
