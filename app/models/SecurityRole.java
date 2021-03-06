/*
 * Copyright 2012 Steve Chaloner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import play.db.ebean.Model;
import be.objectify.deadbolt.core.models.Role;
import enums.MyRoles;

/**
 * @author Steve Chaloner (steve@objectify.be)
 */
@Entity
@Table(name="Security_Role")
public class SecurityRole extends Model implements Role {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id")
	public Long id;

	@Column(name="name")
	public String name;

	public static final Finder<Long, SecurityRole> find = new Finder<Long, SecurityRole>(
			Long.class, SecurityRole.class);

	@Override
	public String getName() {
		return name;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setName(MyRoles name) {
		this.name = name.toString();
	}
//	
//	public void setName(String name) {
//		this.name = name;
//	}

	public static SecurityRole findByRoleName(String roleName) {
		return find.where().eq("name", roleName).findUnique();
	}
}
