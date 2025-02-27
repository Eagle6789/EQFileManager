package com.naga.filemanager.utils.files;

import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CryptUtilTest {

    private Context context;

    public void setUp(){
        context = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        String password = "hackme";
        String encrypted = CryptUtil.encryptPassword(context, password);
        assertEquals(password, CryptUtil.decryptPassword(context, encrypted));
    }
}
