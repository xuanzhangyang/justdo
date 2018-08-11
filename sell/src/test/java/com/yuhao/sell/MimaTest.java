package com.yuhao.sell;

import com.yuhao.sell.utils.PasswordEncoder;

public class MimaTest {
	public static void main(String[] args){
		PasswordEncoder passwordEncoder = new PasswordEncoder();

		System.out.println(passwordEncoder.encodePassword("333333","admin"));
	}

}
