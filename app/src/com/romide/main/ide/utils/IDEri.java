package com.romide.main.ide.utils;

import java.io.*;
import java.util.*;

public class IDEri
{ 
    public static void main(String[] args) throws Exception { 

        if(args.length<=0){
            System.err.println("No File Input");
            System.exit(1);
        }

        BufferedReader reader = new BufferedReader(new FileReader(args[0])); 
        String line = ""; 
        List<String> list = new ArrayList<String>(); 
        while ((line = reader.readLine()) != null) {
            list.add(line);
        } 
        reader.close(); 

		String[] buf;
        String[] command = list.toArray(new String[list.size()]); 
        for(int i=0;i<command.length;i++){
            System.out.println("CommandLine="+command[i]);
			buf = command[i].split(" ");
			for(int j=0;j<buf.length;j++){
				System.out.println("Options:"+j+"="+buf[j]);
			}
        }
    } 
}

