package com.qq1312952829.myclasses;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import com.person.Person;

public class ReflectUtil {
	/**
	 * this method can get the full name of classes in the same package of param.
	 * @param c any Class object under the current package.
	 * @return all the classes name(package+name).
	 */
	@SuppressWarnings("rawtypes")
	public static ArrayList<String> getClassesNameOfCurrentPackage(Class c) {
		ClassLoader cl = c.getClassLoader();
		
		String[] packages = c.getName().split("\\.");
		StringBuilder b = new StringBuilder();
		for(int i=0; i<packages.length-1; i++) //最后一个.class文件没必要拼接
			b.append(packages[i]+"/");
		
		URL url = cl.getResource(b.toString());
		try {
			File[] fs = new File(url.toURI()).listFiles();
			ArrayList<String> out = new ArrayList<>();
			for(int i=0; i<fs.length; i++) {
				if(!fs[i].getName().endsWith(".class"))
					continue;
				
				out.add(fs[i].getName());
				return out;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getClassesNameOfCurrentPackage(Person.class));
	}
}
