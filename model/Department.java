package model;

public class Department {
    private Integer code;
    private String name;

    public Department() {

    }

    public Department(String name) {
        this.name = name;
    }

    public Department(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}