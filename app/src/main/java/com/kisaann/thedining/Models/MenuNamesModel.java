package com.kisaann.thedining.Models;

public class MenuNamesModel {
    private String menuName;
    private String menuCount;

    public MenuNamesModel() {
    }

    public MenuNamesModel(String menuName, String menuCount) {
        this.menuName = menuName;
        this.menuCount = menuCount;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCount() {
        return menuCount;
    }

    public void setMenuCount(String menuCount) {
        this.menuCount = menuCount;
    }
}
