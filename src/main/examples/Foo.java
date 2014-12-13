import org.apache.commons.lang3.StringUtils;

public class Foo {
    public void foo() {
        bar();
        new Bar().alfa();
        StringUtils.capitalize("foo");
    }

    public void bar() {
        goo();
    }

    private void goo() {

    }
}
