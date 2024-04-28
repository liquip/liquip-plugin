package io.github.liquip.paper.standalone.config.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record ConfigStructure(@JsonProperty(value = "items", required = true) List<String> items,
                              @JsonProperty("craftingTable") @Nullable String craftingTable) {
}
