package cn.jerry.android.camera;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by JieGuo on 1/6/16.
 */
public class UserTest {

  private User user;

  @Before public void setup() {

    user = new User();
  }

  @After public void terDown() {

    user = null;
  }

  @Test
  public void testUser() {
    Assert.assertNotEquals(user.name, "jerry");
  }
}
