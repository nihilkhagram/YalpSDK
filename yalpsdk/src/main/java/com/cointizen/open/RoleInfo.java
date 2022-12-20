package com.cointizen.open;

public class RoleInfo {
    private String serverId = "";
    private String serverName = "";
    private String roleName = "";
    private String roleLevel = "";
    private String roleCombat = "";
    private String playerReserve = "";

    public String getRoleCombat() {
        return roleCombat;
    }

    public void setRoleCombat(String roleCombat) {
        this.roleCombat = roleCombat;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    private String roleId;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(String roleLevel) {
        this.roleLevel = roleLevel;
    }

    public String getPlayerReserve() {
        return playerReserve;
    }

    public void setPlayerReserve(String playerReserve) {
        this.playerReserve = playerReserve;
    }
}
