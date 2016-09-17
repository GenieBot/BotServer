package io.sponges.bot.server.database;

public enum Statements {

    // ddl
    CREATE_CLIENTS_TABLE("ddl/create_clients_table.sql"),
    CREATE_NETWORKS_TABLE("ddl/create_networks_table.sql"),
    CREATE_CHANNELS_TABLE("ddl/create_channels_table.sql"),
    CREATE_USERS_TABLE("ddl/create_users_table.sql"),
    CREATE_MODULES_TABLE("ddl/create_modules_table.sql"),
    CREATE_ENABLED_MODULES_TABLE("ddl/create_enabled_modules_table.sql"),
    CREATE_MODULE_DATA_TABLE("ddl/create_module_data_table.sql"),

    // dml
    SELECT_CLIENT_ID("dml/select/select_client_id.sql"),
    SELECT_NETWORK_ID("dml/select/select_network_id.sql"),
    SELECT_CHANNEL_ID("dml/select/select_channel_id.sql"),
    SELECT_USER_ID("dml/select/select_user_id.sql"),
    SELECT_MODULE_ID("dml/select/select_module_id.sql"),
    SELECT_MODULE_DATA("dml/select/select_module_data.sql"),

    INSERT_CLIENT("dml/insert/insert_client.sql"),
    INSERT_NETWORK("dml/insert/insert_network.sql"),
    INSERT_CHANNEL("dml/insert/insert_channel.sql"),
    INSERT_USER("dml/insert/insert_user.sql"),
    INSERT_MODULE("dml/insert/insert_module.sql"),
    INSERT_MODULE_DATA("dml/insert/insert_module_data.sql"),
    INSERT_ENABLED_MODULE("dml/insert/insert_enabled_module.sql"),

    UPDATE_MODULE_DATA("dml/update/update_module_data.sql"),

    DELETE_ENABLED_MODULE("dml/delete/delete_enabled_module.sql"),

    ;

    private final String file;

    private String content = null;

    Statements(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return content;
    }

    public String getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}