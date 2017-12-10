package com.main;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<byte[]> l = new ArrayList<>();
		
		for( ; ;) {
			l.add(new byte[1024*1024]);
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
