package com.rebeau.technology.android.rxjava.base;

public class Man {
    public String name;
    public int age;
    public String sex;

    public Man(String name, int age, String sex) {
        super();
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "Man [name=" + name + ", age=" + age + ", sex=" + sex + "]";
    }
}
