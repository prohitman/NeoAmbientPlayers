package com.prohitman.ambientplayers;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Config {
    public static final Server SERVER;
    static final ModConfigSpec SERVER_SPEC;

    public static final Client CLIENT;
    static final ModConfigSpec CLIENT_SPEC;

    static {
        Pair<Server, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER = serverSpecPair.getLeft();
        SERVER_SPEC = serverSpecPair.getRight();

        Pair<Client, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT = clientSpecPair.getLeft();
        CLIENT_SPEC = clientSpecPair.getRight();
    }

    public static class Server {
        public final ModConfigSpec.ConfigValue<List<? extends String>> names;

        Server(ModConfigSpec.Builder builder) {
            builder.push("Server");
            names = builder.defineList("namesForDisplay", List.of(), o -> true);
            builder.pop();
        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue displayNames;

        Client(ModConfigSpec.Builder builder) {
            builder.push("Client");
            displayNames = builder.define("displayNames", true);
            builder.pop();
        }
    }
}
