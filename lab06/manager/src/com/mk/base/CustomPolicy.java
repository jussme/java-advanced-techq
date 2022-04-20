package com.mk.base;

import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Policy;
import java.util.Arrays;

public class CustomPolicy extends Policy{
	private static PermissionCollection perms = new CustomPermissionCollection();
	
	public CustomPolicy() {
		super();
		addPermissions();
	}
	
	@Override
	public PermissionCollection getPermissions(CodeSource codeSource) {
		return perms;
	}
	
	private void addPermissions() {
		var permList = Arrays.asList(new SocketPermission("*:1024-", "connect, listen, resolve"),
				new FilePermission("rmisslcert.jks", "read"), //new PropertyPermission("*", "read, write"),
				new RuntimePermission("*"));
		
		permList.forEach(perm -> perms.add(perm));
	}

}
