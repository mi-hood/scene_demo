package org.example.model;

import java.util.Objects;

public class FakeModel {
    private String type;
    private Long value;

    private Long timestamp;

    public FakeModel() {
    }

    public FakeModel(String type, Long value,Long timestamp) {
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FakeModel fakeModel = (FakeModel) o;
        return getType().equals(fakeModel.getType()) && getValue().equals(fakeModel.getValue()) && getTimestamp().equals(fakeModel.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getValue(), getTimestamp());
    }

    @Override
    public String toString() {
        return "FakeModel{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                '}';
    }

    public static void main(String[] args) {
        for(int j=2;j<10;j++) {
            for (int i = 0; i < 60; i++) {
                System.out.println(i/j);
            }
            System.out.println();
            System.out.println();
            System.out.println();
        }
    }
}
