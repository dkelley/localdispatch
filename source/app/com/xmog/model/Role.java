package com.xmog.model;

/**
 * @author Transmogrify LLC.
 */
public class Role {
  public static final long ROLE_USER = 1;
  public static final long ROLE_ADMINISTRATOR = 2;

  private Long roleId;
  private String description;

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}