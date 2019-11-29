package ru.aleynikov.blogcamp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegularExpressionTest {

    @Autowired
    @Qualifier("regularExpForUsername")
    private Pattern regularExpUsername;

    @Autowired
    @Qualifier("regularExpForPassword")
    private Pattern regularExpPassword;

    @Test
    public void testUsername() {
        String username01 = "drewlakee";
        String username02 = "drew_lake";
        String username03 = "asd@asd%asd%^a";
        String username04 = "drew lake";
        String username05 = "drew9923";
        String username06 = " ";
        String username07 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";   // 30 length
        String username08 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";  // 31 length

        Assert.assertTrue(regularExpUsername.matcher(username01).matches());
        Assert.assertTrue(regularExpUsername.matcher(username02).matches());
        Assert.assertTrue(regularExpUsername.matcher(username05).matches());
        Assert.assertTrue(regularExpUsername.matcher(username07).matches());

        Assert.assertFalse(regularExpUsername.matcher(username03).matches());
        Assert.assertFalse(regularExpUsername.matcher(username04).matches());
        Assert.assertFalse(regularExpUsername.matcher(username06).matches());
        Assert.assertFalse(regularExpUsername.matcher(username08).matches());
    }

    @Test
    public void testPassword() {
        String password01 = "12345678";
        String password02 = "abc123bdc";
        String password03 = "asb sas asd";
        String password04 = "asdas^7123*9#@";
        String password05 = "12345";
        String password06 = "0123456789012345678901234567890"; // 31 length

        Assert.assertTrue(regularExpPassword.matcher(password01).matches());
        Assert.assertTrue(regularExpPassword.matcher(password02).matches());

        Assert.assertFalse(regularExpPassword.matcher(password05).matches());
        Assert.assertFalse(regularExpPassword.matcher(password03).matches());
        Assert.assertFalse(regularExpPassword.matcher(password04).matches());
        Assert.assertFalse(regularExpPassword.matcher(password06).matches());
    }

}
