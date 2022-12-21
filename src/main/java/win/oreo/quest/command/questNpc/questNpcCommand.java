package win.oreo.quest.command.questNpc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import win.oreo.quest.util.quest.npc.questNpcUtil;
import win.oreo.quest.util.quest.questUtil;

import java.util.UUID;

public class questNpcCommand implements CommandExecutor {
    private questNpcUtil questNpcUtil;
    private questUtil questUtil;

    public questNpcCommand() {
        this.questNpcUtil = new questNpcUtil();
        this.questUtil = new questUtil();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            player.sendMessage("hello!");
            if (args.length == 1) {//todo list
                player.sendMessage(win.oreo.quest.util.quest.npc.questNpcUtil.questNpcList.toString());
            }
            if (args.length == 3) {//todo msg / error check
                switch (args[0]) {
                    case "add":
                        if (questNpcUtil.getQuestNpc(args[1]) == null) {
                            questNpcUtil.createQuest(args[1], questUtil.getQuestByID(UUID.fromString(args[2])));
                            return false;
                        }
                        questNpcUtil.addQuest(args[1], questUtil.getQuestByID(UUID.fromString(args[2])));
                        break;
                    case "remove":
                        if (questNpcUtil.getQuestNpc(args[1]) == null) {
                            return false;
                        }
                        if (args[2].matches("[0-9]+")) {
                            questNpcUtil.removeQuest(args[1], Integer.parseInt(args[2]));
                        }
                        questNpcUtil.removeQuest(args[1], questUtil.getQuestByID(UUID.fromString(args[2])));
                        break;
                }
            }
            if (args.length == 4) {
                switch (args[0]) {
                    case "set":
                        if (questNpcUtil.getQuestNpc(args[1]) == null) {
                            return false;
                        }
                        questNpcUtil.setQuest(args[1], Integer.parseInt(args[2]), questUtil.getQuestByID(UUID.fromString(args[2])));
                }
            }
        }
        return false;
    }
}
