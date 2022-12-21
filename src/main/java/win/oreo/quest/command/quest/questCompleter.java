package win.oreo.quest.command.quest;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import win.oreo.quest.util.quest.Quest;
import win.oreo.quest.util.quest.questType;
import win.oreo.quest.util.quest.questUtil;
import static win.oreo.quest.util.quest.questUtil.questList;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class questCompleter implements TabCompleter {
    private questUtil questUtil;

    public questCompleter() {
        this.questUtil = new questUtil();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        switch (args.length) {
            case 1:
                completions.add("add");
                completions.add("edit");
                completions.add("list");
                completions.add("remove");
                break;
            case 2:
                switch (args[0]) {
                    case "edit":
                        for (Quest quest : questList) {
                            completions.add(quest.getQuestID().toString());
                        }
                        break;
                    case "remove":
                        completions.add("all");
                        for (Quest quest : questList) {
                            completions.add(quest.getQuestID().toString());
                        }
                        break;
                }
                break;
            case 3:
                switch (args[0]) {
                    case "add":
                        for (questType type : questType.values()) {
                            completions.add(type.toString());
                        }
                        break;
                    case "edit":
                        completions.add("name");
                        completions.add("type");
                        completions.add("goal");
                        completions.add("reward");
                        completions.add("target");
                        completions.add("description");
                        break;
                }
                break;
            case 4:
                switch (args[0]) {
                    case "add":
                        switch (questType.valueOf(args[2])) {
                            case HUNT:
                                for (EntityType type : EntityType.values()) {
                                    completions.add(type.toString());
                                }
                                break;
                            case COLLECT:
                                for (Material material : Material.values()) {
                                    completions.add(material.toString());
                                }
                                break;
                        }
                        break;
                    case "edit":
                        switch (args[2]) {
                            case "type":
                                for (questType type : questType.values()) {
                                    completions.add(type.toString());
                                }
                                break;
                            case "target":
                                switch (questUtil.getQuestByID(UUID.fromString(args[1])).getQuestType()) {
                                    case HUNT:
                                        for (EntityType type : EntityType.values()) {
                                            completions.add(type.toString());
                                        }
                                        break;
                                    case COLLECT:
                                        for (Material material : Material.values()) {
                                            completions.add(material.toString());
                                        }
                                        break;
                                }
                                break;
                            case "name":
                                completions.add(questUtil.getQuestByID(UUID.fromString(args[1])).getQuestName());
                                break;
                            case "goal":
                                completions.add(String.valueOf(questUtil.getQuestByID(UUID.fromString(args[1])).getQuestGoal()));
                                break;
                            case "description":
                                completions.add(questUtil.getQuestByID(UUID.fromString(args[1])).getQuestDescription());
                                break;
                            case "reward":
                                for (Material material : Material.values()) {
                                    completions.add(material.toString());
                                }
                                break;
                        }
                        break;
                }
                break;
        }
        return completions;
    }
}
