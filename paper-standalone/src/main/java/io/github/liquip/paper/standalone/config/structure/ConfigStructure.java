package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

public class ConfigStructure {
    private final List<String> items;
    private boolean customCraftingTable = false;
    private String craftingTable = "liquip:crafting_table";

    @JsonCreator
    public ConfigStructure(@JsonProperty(value = "items", required = true) List<String> items) {
        this.items = items;
    }

    public List<String> getItems() {
        return this.items;
    }

    public boolean hasCustomCraftingTable() {
        return this.customCraftingTable;
    }

    public String getCraftingTable() {
        return this.craftingTable;
    }

    @JsonSetter
    public void setCraftingTable(String craftingTable) {
        this.customCraftingTable = true;
        this.craftingTable = craftingTable;
    }
}
