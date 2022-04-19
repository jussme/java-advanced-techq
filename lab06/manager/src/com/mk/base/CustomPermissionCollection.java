package com.mk.base;

import java.security.Permission;
import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class CustomPermissionCollection extends PermissionCollection {
	private static final long serialVersionUID = 1L;

	private List<Permission> perms = new ArrayList<>();
	
	@Override
	public void add(Permission permission) {
		perms.add(permission);
	}

	@Override
	public boolean implies(Permission permission) {
		return perms.stream().anyMatch(it -> it.implies(permission));
	}

	@Override
	public Enumeration<Permission> elements() {
		return Collections.enumeration(perms);
	}
	
	public boolean isReadOnly() {
		return false;
	}

}
