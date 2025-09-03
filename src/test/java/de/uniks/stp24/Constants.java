package de.uniks.stp24;

public class Constants {
    public static final String DISTRICTS_PRESETS = "[\n" +
            "  {\n" +
            "    \"id\": \"city\",\n" +
            "    \"chance\": {},\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 5,\n" +
            "      \"consumer_goods\": 2\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"credits\": 12\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"energy\",\n" +
            "    \"chance\": {\n" +
            "      \"energy\": 5,\n" +
            "      \"ancient_technology\": 4,\n" +
            "      \"ancient_industry\": 3,\n" +
            "      \"ancient_military\": 3,\n" +
            "      \"default\": 2\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 75\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 2\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"energy\": 30\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"mining\",\n" +
            "    \"chance\": {\n" +
            "      \"mining\": 5,\n" +
            "      \"ancient_industry\": 4,\n" +
            "      \"ancient_technology\": 3,\n" +
            "      \"ancient_military\": 3,\n" +
            "      \"default\": 2\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 50,\n" +
            "      \"energy\": 25\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 4,\n" +
            "      \"fuel\": 2\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"minerals\": 24\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"agriculture\",\n" +
            "    \"chance\": {\n" +
            "      \"agriculture\": 5,\n" +
            "      \"default\": 2\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 75\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 2,\n" +
            "      \"fuel\": 3\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"food\": 16\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"industry\",\n" +
            "    \"chance\": {},\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 6,\n" +
            "      \"minerals\": 8\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"alloys\": 5,\n" +
            "      \"consumer_goods\": 5,\n" +
            "      \"fuel\": 5\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"research_site\",\n" +
            "    \"chance\": {\n" +
            "      \"ancient_technology\": 5,\n" +
            "      \"ancient_military\": 2,\n" +
            "      \"ancient_industry\": 2,\n" +
            "      \"default\": 0\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 15,\n" +
            "      \"consumer_goods\": 5\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"research\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"ancient_foundry\",\n" +
            "    \"chance\": {\n" +
            "      \"ancient_military\": 5,\n" +
            "      \"ancient_industry\": 3,\n" +
            "      \"ancient_technology\": 2,\n" +
            "      \"default\": 0\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 10,\n" +
            "      \"energy\": 5\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"alloys\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"ancient_factory\",\n" +
            "    \"chance\": {\n" +
            "      \"ancient_industry\": 5,\n" +
            "      \"ancient_technology\": 3,\n" +
            "      \"ancient_military\": 2,\n" +
            "      \"default\": 0\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 5,\n" +
            "      \"minerals\": 10\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"consumer_goods\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"ancient_refinery\",\n" +
            "    \"chance\": {\n" +
            "      \"ancient_industry\": 5,\n" +
            "      \"ancient_military\": 3,\n" +
            "      \"ancient_technology\": 2,\n" +
            "      \"default\": 0\n" +
            "    },\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 5,\n" +
            "      \"energy\": 10\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"fuel\": 10\n" +
            "    }\n" +
            "  }\n" +
            "]";
    public static final String Buildings_Preset = "[\n" +
            "  {\n" +
            "    \"id\": \"exchange\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 5,\n" +
            "      \"consumer_goods\": 5\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"credits\": 20\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"power_plant\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 75\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 2\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"energy\": 20\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"mine\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 50,\n" +
            "      \"energy\": 25\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 2,\n" +
            "      \"fuel\": 2\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"minerals\": 24\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"farm\",\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 75\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 2,\n" +
            "      \"fuel\": 2\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"food\": 24\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"research_lab\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"consumer_goods\": 5\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"research\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"foundry\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 15,\n" +
            "      \"energy\": 10\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"alloys\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"factory\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 15,\n" +
            "      \"energy\": 10\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"consumer_goods\": 10\n" +
            "    }\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": \"refinery\",\n" +
            "    \"cost\": {\n" +
            "      \"minerals\": 100\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"minerals\": 10,\n" +
            "      \"energy\": 15\n" +
            "    },\n" +
            "    \"production\": {\n" +
            "      \"fuel\": 10\n" +
            "    }\n" +
            "  }\n" +
            "]";
    public static final String SYSTEM_UPGRADES = "{\n" +
            "  \"unexplored\": {\n" +
            "    \"id\": \"string\",\n" +
            "    \"pop_growth\": 0,\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"capacity_multiplier\": 0\n" +
            "  },\n" +
            "  \"explored\": {\n" +
            "    \"id\": \"string\",\n" +
            "    \"pop_growth\": 0,\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"capacity_multiplier\": 0\n" +
            "  },\n" +
            "  \"colonized\": {\n" +
            "    \"id\": \"string\",\n" +
            "    \"pop_growth\": 0,\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"capacity_multiplier\": 0\n" +
            "  },\n" +
            "  \"upgraded\": {\n" +
            "    \"id\": \"string\",\n" +
            "    \"pop_growth\": 0,\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"capacity_multiplier\": 0\n" +
            "  },\n" +
            "  \"developed\": {\n" +
            "    \"id\": \"string\",\n" +
            "    \"pop_growth\": 0,\n" +
            "    \"cost\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"upkeep\": {\n" +
            "      \"energy\": 10,\n" +
            "      \"minerals\": 20\n" +
            "    },\n" +
            "    \"capacity_multiplier\": 0\n" +
            "  }\n" +
            "}";
}
