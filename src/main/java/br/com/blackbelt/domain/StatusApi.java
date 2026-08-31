package br.com.blackbelt.domain;

public class StatusApi {

    private String status;
    private String application;
    private String version;

    public StatusApi(String status, String application, String version) {
        this.status = status;
        this.application = application;
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public String getApplication() {
        return application;
    }

    public String getVersion() {
        return version;
    }
}