package com.eventbase.androidcodetest;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Bluebery on 7/30/2015.
 * Modified from http://www.androidbegin.com/tutorial/android-parse-com-listview-images-and-texts-tutorial/
 */
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
