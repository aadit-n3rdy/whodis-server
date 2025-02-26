package com.whodis.whodis;

import java.io.*;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        Master master = new Master(8765, 10);
        master.run();
    }
}
