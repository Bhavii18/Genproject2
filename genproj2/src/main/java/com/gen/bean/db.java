package com.gen.bean;
import java.sql.Connection;
import java.sql.SQLException;

import com.gen.util.*;
public class db {

	public static void main(String[] args) throws SQLException {
		Connection n = DbUtil.getConnection();
		System.out.print("connected");
		// TODO Auto-generated method stub

	}

}