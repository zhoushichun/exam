package com.garm.common.redis.redissession;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("abapi.cloud.redisson")
public class RedissonProperties {
    public static final String PREFIX = "abapi.cloud.redisson";
    private Boolean enabled = false;
    private String host;
    private Integer port;
    private String password;
    private String clusterNodes;

    public RedissonProperties() {
    }

    public Boolean getEnabled() {
        return this.enabled;
    }

    public String getHost() {
        return this.host;
    }

    public Integer getPort() {
        return this.port;
    }

    public String getPassword() {
        return this.password;
    }

    public String getClusterNodes() {
        return this.clusterNodes;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RedissonProperties)) {
            return false;
        } else {
            RedissonProperties other = (RedissonProperties)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$enabled = this.getEnabled();
                    Object other$enabled = other.getEnabled();
                    if (this$enabled == null) {
                        if (other$enabled == null) {
                            break label71;
                        }
                    } else if (this$enabled.equals(other$enabled)) {
                        break label71;
                    }

                    return false;
                }

                Object this$host = this.getHost();
                Object other$host = other.getHost();
                if (this$host == null) {
                    if (other$host != null) {
                        return false;
                    }
                } else if (!this$host.equals(other$host)) {
                    return false;
                }

                label57: {
                    Object this$port = this.getPort();
                    Object other$port = other.getPort();
                    if (this$port == null) {
                        if (other$port == null) {
                            break label57;
                        }
                    } else if (this$port.equals(other$port)) {
                        break label57;
                    }

                    return false;
                }

                Object this$password = this.getPassword();
                Object other$password = other.getPassword();
                if (this$password == null) {
                    if (other$password != null) {
                        return false;
                    }
                } else if (!this$password.equals(other$password)) {
                    return false;
                }

                Object this$clusterNodes = this.getClusterNodes();
                Object other$clusterNodes = other.getClusterNodes();
                if (this$clusterNodes == null) {
                    if (other$clusterNodes == null) {
                        return true;
                    }
                } else if (this$clusterNodes.equals(other$clusterNodes)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RedissonProperties;
    }

    public int hashCode() {
//        int PRIME = true;
        int result = 1;
        Object $enabled = this.getEnabled();
        result = result * 59 + ($enabled == null ? 43 : $enabled.hashCode());
        Object $host = this.getHost();
        result = result * 59 + ($host == null ? 43 : $host.hashCode());
        Object $port = this.getPort();
        result = result * 59 + ($port == null ? 43 : $port.hashCode());
        Object $password = this.getPassword();
        result = result * 59 + ($password == null ? 43 : $password.hashCode());
        Object $clusterNodes = this.getClusterNodes();
        result = result * 59 + ($clusterNodes == null ? 43 : $clusterNodes.hashCode());
        return result;
    }

    public String toString() {
        return "RedissonProperties(enabled=" + this.getEnabled() + ", host=" + this.getHost() + ", port=" + this.getPort() + ", password=" + this.getPassword() + ", clusterNodes=" + this.getClusterNodes() + ")";
    }
}
