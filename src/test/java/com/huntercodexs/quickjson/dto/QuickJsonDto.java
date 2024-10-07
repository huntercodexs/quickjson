package com.huntercodexs.quickjson.dto;

import java.util.HashMap;
import java.util.List;

public class QuickJsonDto {
    public Object type;
    public Integer age;
    public String name;
    public String lastname;
    public String fullname;
    public String reference;
    public List<Object> address;
    public List<Object> contacts;
    public List<Object> numbers;
    public List<Object> family;
    public HashMap<Object,Object> map;

    public QuickJsonDto() {
    }

    public QuickJsonDto(
            Object type,
            Integer age,
            String name,
            String lastname,
            String fullname,
            String reference,
            List<Object> address,
            List<Object> contacts,
            List<Object> numbers,
            List<Object> family,
            HashMap<Object, Object> map
    ) {
        this.type = type;
        this.age = age;
        this.name = name;
        this.lastname = lastname;
        this.fullname = fullname;
        this.reference = reference;
        this.address = address;
        this.contacts = contacts;
        this.numbers = numbers;
        this.family = family;
        this.map = map;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public List<Object> getAddress() {
        return address;
    }

    public void setAddress(List<Object> address) {
        this.address = address;
    }

    public List<Object> getContacts() {
        return contacts;
    }

    public void setContacts(List<Object> contacts) {
        this.contacts = contacts;
    }

    public List<Object> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Object> numbers) {
        this.numbers = numbers;
    }

    public List<Object> getFamily() {
        return family;
    }

    public void setFamily(List<Object> family) {
        this.family = family;
    }

    public HashMap<Object, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<Object, Object> map) {
        this.map = map;
    }

    public String toString() {
        return "QuickJsonDto(" +
                "type=" + type +
                ", age=" + age +
                ", name=" + name +
                ", lastname=" + lastname +
                ", fullname=" + fullname +
                ", reference=" + reference +
                ", address=" + address +
                ", contacts=" + contacts +
                ", numbers=" + numbers +
                ", family=" + family +
                ", map=" + map +
                ')';
    }
}
