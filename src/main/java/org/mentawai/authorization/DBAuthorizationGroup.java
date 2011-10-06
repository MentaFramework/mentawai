/*
 * Mentawai Web Framework http://mentawai.lohis.com.br/
 * Copyright (C) 2005  Sergio Oliveira Jr. (sergio.oliveira.jr@gmail.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.mentawai.authorization;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DBAuthorizationGroup that will load groups and/or permissions with exists from a database.
 *
 * All you have to do is provide:
 *
 * - the names of the key columns
 * - the names of the value columns
 * - the names of the tables
 * - the column to sort
 *
 * @author Leiber Wallace Bento de Sousa
 * @since 1.12
 *
 */
public class DBAuthorizationGroup {

	private Group group;

	private List<Group> list;

	private StringBuffer sql = null;

	//fields
	private String tableGroup = null, keyGroup = null, valueGroup = null;
	private String tableReference = null, keyPermissionReference = null, keyGroupReference = null;
	private String tablePermission = null, keyPermission = null, valuePermission = null;
	private String sqlWhere = null;

	/**
	 * Set the SQL command to load groups and permissions from data base.
	 *
	 * @param sql
	 */
	public DBAuthorizationGroup(String sql) {
		this.sql = new StringBuffer(sql);
		this.list = null;
	}

	/**
	 * Get a groups only.
	 *
	 * @param tableGroup name of table.
	 * @param keyGroup name of field contain a ID (key)
	 * @param valueGroup name of field contain a Name.
	 */
	public DBAuthorizationGroup(String tableGroup, String keyGroup, String valueGroup) {
		this.tableGroup = tableGroup;
		this.keyGroup = keyGroup;
		this.valueGroup = valueGroup;
		this.list = null;
	}

	/**
	 * Get a groups using a command SQL WHERE
	 *
	 * @param tableGroup name of table.
	 * @param keyGroup name of field contain a ID (key)
	 * @param valueGroup name of field contain a Name.
	 * @param sqlWhere contain a command SQL WHERE, Ex. WHERE group.id < 5.
	 */
	public DBAuthorizationGroup(String tableGroup, String keyGroup, String valueGroup, String sqlWhere) {
		this.tableGroup = tableGroup;
		this.keyGroup = keyGroup;
		this.valueGroup = valueGroup;
		this.sqlWhere = sqlWhere;
		this.list = null;
	}

	/**
	 * Get a groups and permissions (OneToMany)
	 *
	 * @param tableGroup name of table.
	 * @param keyGroup name of field contain a ID (key)
	 * @param valueGroup name of field contain a Name.
	 * @param tablePermission name of table contain the permissions.
	 * @param valuePermission name of field contain the permissions.
	 */
	public DBAuthorizationGroup(String tableGroup, String keyGroup, String valueGroup,
								String tablePermission, String valuePermission) {
		this.tableGroup = tableGroup;
		this.keyGroup = keyGroup;
		this.valueGroup = valueGroup;
		this.valuePermission = valuePermission;
		this.list = null;
	}

	/**
	 * Get a groups and permissions (OneToMany) using a command SQL WHERE
	 *
	 * @param tableGroup name of table.
	 * @param keyGroup name of field contain a ID (key)
	 * @param valueGroup name of field contain a Name.
	 * @param tablePermission name of table contain the permissions.
	 * @param valuePermission name of field contain the name of permission.
	 * @param sqlWhere contain a command SQL WHERE, Ex. WHERE group.id < 5 and permission < 2.
	 */
	public DBAuthorizationGroup(String tableGroup, String keyGroup, String valueGroup,
								String tablePermission, String valuePermission, String sqlWhere) {
		this.tableGroup = tableGroup;
		this.keyGroup = keyGroup;
		this.valueGroup = valueGroup;
		this.valuePermission = valuePermission;
		this.sqlWhere = sqlWhere;
		this.list = null;
	}

	/**
	 * Get a groups and permissions (ManyToMany)
	 *
	 * @param tableGroup name of table.
	 * @param keyGroup name of field contain a ID (key)
	 * @param valueGroup name of field contain a Name.
	 * @param tableReference name of table contain the IDs groups and permissions.
	 * @param keyPermissionReference name of field contain the permissions Id.
	 * @param keyGroupReference name of field contain the groups Id.
	 * @param tablePermission name of table contain permissions.
	 * @param keyPermission name of field contain the id of permission.
	 * @param valuePermission name of field contain the name of permission.
	 */
	//Tabela que usam IDs na tabela reference diferentes
	public DBAuthorizationGroup(String tableGroup, String keyGroup, String valueGroup,
								String tableReference, String keyPermissionReference, String keyGroupReference,
								String tablePermission, String keyPermission, String valuePermission) {
		this.tableGroup = tableGroup;
		this.keyGroup = keyGroup;
		this.valueGroup = valueGroup;
		this.tableReference = tableReference;
		this.keyPermissionReference = keyPermissionReference;
		this.keyGroupReference = keyGroupReference;
		this.tablePermission = tablePermission;
		this.keyPermission = keyPermission;
		this.valuePermission = valuePermission;
		this.list = null;
	}

	/**
	 * Get a groups and permissions (ManyToMany)
	 *
	 * @param tableGroup name of table.
	 * @param keyGroup name of field contain a ID (key)
	 * @param valueGroup name of field contain a Name.
	 * @param tableReference name of table contain the IDs groups and permissions.
	 * @param keyPermissionReference name of field contain the permissions Id.
	 * @param keyGroupReference name of field contain the groups Id.
	 * @param tablePermission name of table contain permissions.
	 * @param keyPermission name of field contain the id of permission.
	 * @param valuePermission name of field contain the name of permission.
	 * @param sqlWhere contain a command SQL WHERE, Ex. WHERE group_permission.group_id = 2.
	 */
	//Tabela que usam IDs na tabela reference diferentes
	public DBAuthorizationGroup(String tableGroup, String keyGroup, String valueGroup,
								String tableReference, String keyPermissionReference, String keyGroupReference,
								String tablePermission, String keyPermission, String valuePermission, String sqlWhere) {
		this.tableGroup = tableGroup;
		this.keyGroup = keyGroup;
		this.valueGroup = valueGroup;
		this.tableReference = tableReference;
		this.keyPermissionReference = keyPermissionReference;
		this.keyGroupReference = keyGroupReference;
		this.tablePermission = tablePermission;
		this.keyPermission = keyPermission;
		this.valuePermission = valuePermission;
		this.sqlWhere = sqlWhere;
		this.list = null;
	}

	/**
	 * Build the SQL command using the parameters from the DBAuthorizationGroup to get the
	 * groups and permissions (if exists) from database!
	 */
	protected String buildSQL() {

		if (this.sql == null) {

			//ADD REQUIRED VALUES TO SELECT
			this.sql = new StringBuffer("select "+tableGroup+"." + keyGroup + ", "+tableGroup+"." + valueGroup);

			//IF TABLE USE PERMISSION TABLE, ADD PERMISSION VALUE TO RETURN CLAUSELE
			if (tablePermission!=null) {
				this.sql.append(" , "+tablePermission+"." + valuePermission);
			}

			//ADD FROM TO SQL CLAUSE
			this.sql.append(" from "+tableGroup);

			//IF TABLE USE PERMISSION TABLE, ADD PERMISSION TABLE TO FROM CLAUSELE
			if (tablePermission!=null){
				this.sql.append(", "+tablePermission);
			}

			//IF TABLE USE MANY-TO-MANY, ADD REFERENCES TABLE TO FROM CLAUSELE
			if (tableReference!=null) {
				this.sql.append(" , " + tableReference +
						   " where "+ tablePermission+"." + keyPermission +" = "+ tableReference +"."+ keyPermissionReference +
						   " and "+ tableReference +"." + keyGroupReference + " = "+ tableGroup +"."+ keyGroup + (sqlWhere != null ? sqlWhere : "") +
						   " order by 1, 2, 3 asc");
			} else {
				this.sql.append((sqlWhere != null ? " "+sqlWhere : ""));
			}

		}

		return this.sql.toString();

	}

	/**
	 * Call this method passing a db connection to load the groups/permissions from database and
	 * adding in AuthorizationManager using the add(Group);
	 *
	 * The SQL statement that will be constructed:
	 *
	 * @param conn
	 * @throws SQLException
	 */
	public void load(Connection conn) throws SQLException {

		PreparedStatement stmt = null;
		ResultSet rset = null;

		String sql = buildSQL();

		try {
			stmt = conn.prepareStatement(sql);
			rset = stmt.executeQuery();

			while(rset.next()) {

				keyGroup = rset.getString(1);
				valueGroup = rset.getString(2);

				//only using permisisons or many to many
				if (tablePermission!=null) valuePermission = (rset.getString(3)!= null?rset.getString(3):null);

				//if first line create a new group (only first line) else add permission or new group
				if (rset.isFirst()) {
						addNewGroup(keyGroup, valueGroup, valuePermission);
				} else {
					//if group name is same the last group create and the permission exists, add new permission to group
					if (group.getName().equalsIgnoreCase(valueGroup) && valuePermission != null) {
						addPermission(valuePermission);
					//if is a new group, add the last group to the AuthorizationManager and create a new group
					}else{
						AuthorizationManager.addGroup(group);
						addNewGroup(keyGroup, valueGroup, valuePermission);
					}
				}
				//if rset is the last line, add the last group to the AuthorizationManager
				if ( rset.isLast() ) AuthorizationManager.addGroup(group);
			}

		} finally {

			try { if (rset != null) rset.close(); } catch(Exception e) { }
			try { if (stmt != null) stmt.close(); } catch(Exception e) { }
		}
	}

	/**
	 * Call this method if you need to get a list of groups and permissions (if exists)
	 *
	 * The SQL statement that will be constructed:
	 *
	 * @param conn
	 * @return List<Group> List of groups with permissions (if exists)
	 * @throws SQLException
	 */
	public List<Group> loadGroupList(Connection conn) throws SQLException {

		list = new ArrayList<Group>(1000);

		PreparedStatement stmt = null;
		ResultSet rset = null;

		String sql = buildSQL();

		try {
			stmt = conn.prepareStatement(sql);
			rset = stmt.executeQuery();

			while(rset.next()) {
				keyGroup = rset.getString(1);
				valueGroup = rset.getString(2);

				//only using permisisons or many to many
				if (tablePermission!=null) valuePermission = (rset.getString(3)!= null?rset.getString(3):null);

				//if first line create a new group (only first line) else add permission or new group
				if (rset.isFirst()) {
					addNewGroup(keyGroup, valueGroup, valuePermission);
				} else {
					//if group name is same the last group create and the permission exists, add new permission to group
					if (group.getName().equalsIgnoreCase(valueGroup) && valuePermission != null) {
						addPermission(valuePermission);

					//if is a new group, add the last group to the list and create a new group
					}else{
						addGroupToListGroup(group);
						addNewGroup(keyGroup, valueGroup, valuePermission);
					}

				}
				//if rset is the last line, add the last group to the list
				if ( rset.isLast() ) addGroupToListGroup(group);

			}
			return this.list;

		} finally {
			try { if (rset != null) rset.close(); } catch(Exception e) { }
			try { if (stmt != null) stmt.close(); } catch(Exception e) { }
		}
	}

	/**
	 * Internal command to create or set groups
	 *
	 * @param kGroup
	 * @param vGroup
	 * @param vPermission
	 *
	 */
	private void addNewGroup(String kGroup, String vGroup, String vPermission) {

		if (vGroup==null && vPermission==null){
			//if SQL not return group ids (null), create a groups using only name of groups
			group = new Group(vGroup);

		}else if (vPermission==null) {
			//if SQL not return permission(null), create a groups using only id and names of groups
			group = new Group(Integer.parseInt(kGroup), vGroup);

		}else{
			//if SQL return id and name of group and permission, create a groups using only id, names of groups and permission
			group = new Group(Integer.parseInt(kGroup), vGroup).addPermission(vPermission);

		}

	}

	/**
	 * Internal command to set permissions on group
	 *
	 * @param kGroup
	 * @param vGroup
	 * @param vPermission
	 *
	 */
	private void addPermission(String vPermission) {
		if (vPermission==null) group.addPermission(vPermission); //if not null add new permission to group
	}

	/**
	 * Internal command to set groups on list
	 *
	 * @param kGroup
	 * @param vGroup
	 * @param vPermission
	 *
	 */
	private void addGroupToListGroup(Group g) {
		if (g!=null) this.list.add(g); //if not null add new group to list
	}

}
