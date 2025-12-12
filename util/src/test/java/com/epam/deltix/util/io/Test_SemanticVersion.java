package com.epam.deltix.util.io;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Alex Karpovich on 9/21/2018.
 */
public class Test_SemanticVersion {

    @Test
    public void test() {
        SemanticVersion v = new SemanticVersion("tools", "");
        Assert.assertEquals("tools", v.getId());
        Assert.assertEquals("[tools]", v.toString());
        Assert.assertEquals("tools", v.getName());

        v = new SemanticVersion("taglibs-standard-impl-1.0.5", "");
        Assert.assertEquals("taglibs;standard;impl", v.getId());
        Assert.assertEquals("taglibs-standard-impl", v.getName());

        v = new SemanticVersion("taglibs1 standard impl 1.2.5", "");
        Assert.assertEquals("taglibs1;standard;impl", v.getId());
        Assert.assertEquals("taglibs1 standard impl", v.getName());

        v = new SemanticVersion("taglibs-standard3-impl-1.2.5-SNApSHOT", "");
        Assert.assertEquals("taglibs;standard3;impl", v.getId());
        Assert.assertEquals("taglibs-standard3-impl", v.getName());

        v = new SemanticVersion("taglibs5-standard-1.0.0-alpha+001", "");
        Assert.assertEquals("taglibs5;standard", v.getId());
        Assert.assertEquals("taglibs5-standard", v.getName());

        v = new SemanticVersion("taglibs5-standard-1-0-1.2.5-alpha+001", "");
        Assert.assertEquals("taglibs5;standard;1;0", v.getId());
        Assert.assertEquals("taglibs5-standard-1-0", v.getName());
        Assert.assertEquals("[taglibs5, standard, 1, 1, 2, 5, alpha, 1]", v.toString());

        v = new SemanticVersion("test-me-1.0.0-x.7.z.92", "");
        Assert.assertEquals("test;me", v.getId());
        Assert.assertEquals("test-me", v.getName());
        Assert.assertEquals("[test, me, 1, x.7.z.92]", v.toString());

        v = new SemanticVersion("objecttrading.data-22.80-SNAPSHOT+190722113743.46c7154.jar", "");
        Assert.assertEquals("objecttrading.data", v.getId());
    }
}
