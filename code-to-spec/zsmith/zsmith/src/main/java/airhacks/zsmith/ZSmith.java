package airhacks.zsmith;

import airhacks.zsmith.agent.boundary.Agent;

interface ZSmith {

    static void main(String... args) {
        IO.println("ZSmith version: " + Agent.version);
    }

}
