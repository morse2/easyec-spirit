package com.googlecode.easyec.spirit.web.controller.sorts;

/**
 * 排序实现类。
 *
 * @author JunJie
 */
public class DefaultSort implements Sort {

    private static final long serialVersionUID = -1117967718354134718L;
    private String name;
    private SortTypes type;

    public DefaultSort(String name) {
        this(name, SortTypes.ASC);
    }

    public DefaultSort(String name, SortTypes type) {
        this.name = name;
        this.type = (type != null ? type : SortTypes.ASC);
    }

    public SortTypes getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultSort that = (DefaultSort) o;

        if (type != that.type) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DefaultSort{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}
