package org.acme;

public class MyPojo {
    public Long id;
    public String title;
    public MyEnum myEnum;

    @Override
    public String toString() {
        return "MyPojo{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", myEnum=" + myEnum +
            '}';
    }
}
